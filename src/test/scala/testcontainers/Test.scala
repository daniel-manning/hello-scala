package testcontainers

import cats.implicits._
import cats.effect.{ExitCode, IO}
import com.dimafeng.testcontainers._
import fs2.kafka.vulcan.{Auth, AvroSettings, SchemaRegistryClientSettings}
import org.apache.kafka.clients.admin._
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.testcontainers.containers.Network
import vulcan.Codec

import java.time.LocalDateTime
import java.util.Properties
import scala.concurrent.duration._
import scala.jdk.CollectionConverters._

class Test extends AnyFlatSpec with ForAllTestContainer with Matchers {

  val kafkaVersion = "6.1.1"
  val brokerId = 1
  val hostName = s"kafka$brokerId"
  val topicName = "test"

  val network: Network = Network.newNetwork()

  val kafkaContainer: KafkaContainer =
    KafkaContainer.Def(kafkaVersion).createContainer()
  val schemaRegistryContainer: GenericContainer = SchemaRegistryContainer
    .Def(network, hostName, kafkaVersion)
    .createContainer()

  kafkaContainer.container
    .withNetwork(network)
    .withNetworkAliases(hostName)
    .withEnv(
      Map[String, String](
        "KAFKA_BROKER_ID" -> brokerId.toString,
        "KAFKA_HOST_NAME" -> hostName,
        "KAFKA_AUTO_CREATE_TOPICS_ENABLE" -> "false"
      ).asJava
    )

  override val container: MultipleContainers =
    MultipleContainers(kafkaContainer, schemaRegistryContainer)

  def getKafkaAddress: String = kafkaContainer.bootstrapServers

  def getSchemaRegistryAddress: String =
    s"http://${schemaRegistryContainer.container.getHost}:${schemaRegistryContainer.container
      .getMappedPort(SchemaRegistryContainer.defaultSchemaPort)}"

  "Schema registry container" should "be started" in {

    val adminProperties = new Properties()
    adminProperties.put(
      AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
      getKafkaAddress
    )

    val adminClient = AdminClient.create(adminProperties)
    val createTopicResult = adminClient.createTopics(
      List(new NewTopic(topicName, 1, 1.toShort)).asJava
    )
    createTopicResult.values().get(topicName).get()

    val properties = new Properties()
    properties.put("bootstrap.servers", getKafkaAddress)
    properties.put("group.id", "consumer-tutorial")
    properties.put("key.deserializer", classOf[StringDeserializer])
    properties.put("value.deserializer", classOf[StringDeserializer])

    val kafkaConsumer = new KafkaConsumer[String, String](properties)
    val topics = kafkaConsumer.listTopics()

    assert(topics.containsKey(topicName))
    assert(topics.containsKey("_schemas"))
  }

  "Message" should "be read" in {

    implicit val messageCodec: Codec[Message] =
      Codec
        .record[Message](
          name = "Message",
          namespace = "testcontainers"
        ) { field =>
          (
            field("id", _.id),
            field("content", _.content)
          ).mapN(Message)
        }


    val avroSettings = AvroSettings {
      SchemaRegistryClientSettings[IO](getSchemaRegistryAddress)
        .withAuth(Auth.Basic("username", "password"))
    }

    import fs2.kafka.vulcan.{avroDeserializer, avroSerializer}
    import fs2.kafka.{RecordDeserializer, RecordSerializer}

    implicit val personSerializer: RecordSerializer[IO, Message] =
      avroSerializer[Message].using(avroSettings)

    implicit val personDeserializer: RecordDeserializer[IO, Message] =
      avroDeserializer[Message].using(avroSettings)

    import fs2.kafka._

    val consumerSettings =
      ConsumerSettings[IO, String, Message]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers("localhost:9092")
        .withGroupId("group")

    val producerSettings =
      ProducerSettings[IO, String, Message]
        .withBootstrapServers("localhost:9092")

    def processRecord(record: ConsumerRecord[String, Message]): IO[(String, Message)] =
      IO.pure(record.key -> Message(LocalDateTime.now().toString, record.key))

    val stream =
      fs2.kafka.KafkaConsumer.stream(consumerSettings)
        .subscribeTo("topic")
        .records
        .mapAsync(25) { committable =>
          processRecord(committable.record)
            .map { case (key, value) =>
              val record = ProducerRecord("topic", key, value)
              ProducerRecords.one(record, committable.offset)
            }
        }
        .through(KafkaProducer.pipe(producerSettings))
        .map(_.passthrough)
        .through(commitBatchWithin(500, 15.seconds))

    stream.compile.drain.as(ExitCode.Success)
  }
}
