package cats.scalawithcats.part2.asynchronous

trait UptimeClient[F[_]] {
  def getUptime(hostname: String): F[Int]
}
