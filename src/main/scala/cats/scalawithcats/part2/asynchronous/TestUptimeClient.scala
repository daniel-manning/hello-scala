package cats.scalawithcats.part2.asynchronous

import cats.Id

trait TestUptimeClient extends UptimeClient[Id] {
  override def getUptime(hostname: String): Int
}
