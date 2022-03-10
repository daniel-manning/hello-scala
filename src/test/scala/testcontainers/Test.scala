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
import org.testcontainers.utility.DockerImageName
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
    KafkaContainer(DockerImageName.parse(s"confluentinc/cp-kafka:$kafkaVersion"))

  val mysqlContainer: MySQLContainer = MySQLContainer()

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
    MultipleContainers(mysqlContainer, kafkaContainer)

  def getKafkaAddress: String = kafkaContainer.bootstrapServers

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

    import fs2.kafka._

    implicit val keyDeserializer: RecordDeserializer[IO, String] = RecordDeserializer.lift(Deserializer.string)
    implicit val valueDeserializer: RecordDeserializer[IO, String] = RecordDeserializer.lift(Deserializer.string)
    implicit val keySerializer: RecordSerializer[IO, String] = RecordSerializer.lift(Serializer.string)
    implicit val valueSerializer: RecordSerializer[IO, String] = RecordSerializer.lift(Serializer.string)

    val consumerSettings =
      ConsumerSettings[IO, String, String](keyDeserializer, valueDeserializer)
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers("localhost:9092")
        .withGroupId("group")

    val producerSettings =
      ProducerSettings[IO, String, String](keySerializer, valueSerializer)
        .withBootstrapServers("localhost:9092")

    def processRecord(record: ConsumerRecord[String, String]): IO[(String, String)] =
      IO.pure(record.key -> s"${LocalDateTime.now()}: ${record.key}")

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
