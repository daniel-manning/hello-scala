package hackerrank

object PentagonalNumbers {

  def main(args: Array[String]) {

    val stdIn = scala.io.StdIn
    val cache: collection.mutable.TreeMap[Int, BigInt] = collection.mutable.TreeMap()

    def pentagonal(n: Int, m: BigInt): (Int, BigInt) = n match {
        case 1                            => (0, 1 + m)
        case x if cache.exists(_._1 == x) => (0, cache(x) + m)
        case x                            => pentagonal(x-1, m + 3*x - 2)
    }

    def go(n: Int): BigInt = {
      val pent = pentagonal(n, 0)
      val result = pent._1 + pent._2
      if(!cache.exists(_._1 == n)) cache += (n -> result)
      result
    }

    val T = stdIn.readInt()
    for (_ <- 1 to T) {
      val n = stdIn.readInt()
      println(go(n))
    }
  }
}
