package cats.scalawithcats.part2.asynchronous
import scala.concurrent.Future

trait RealUptimeClient extends UptimeClient[Future] {
  override def getUptime(hostname: String): Future[Int]
}
