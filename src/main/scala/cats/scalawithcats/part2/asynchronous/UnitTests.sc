import cats.scalawithcats.part2.asynchronous.{TestUptimeClient, UptimeService}

def testTotalUptime(): Unit = {
  val hosts = Map("host1" -> 10, "host2" -> 6)
  val client = new TestUptimeClient {
    override def getUptime(hostname: String) = hosts.getOrElse(hostname, 0)
  }
  val service = new UptimeService(client)
  val actual = service.getTotalUptime(hosts.keys.toList)
  val expected = hosts.values.sum
  assert(actual == expected)
}

testTotalUptime()
println("Success!")