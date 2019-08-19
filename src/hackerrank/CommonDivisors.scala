package hackerrank

object CommonDivisors {

  def main(args: Array[String]) {

    val stdIn = scala.io.StdIn

    def getDivisors(n: Int) = (1 to n).filter(n % _ == 0).toSet

    import scala.math.sqrt
    def getDivisorsOptimized(n: Int): Set[Int] = {
      val set = (1 to sqrt(n).ceil.toInt).filter(n % _ == 0).toSet
      set ++ set.map(n / _)
    }

    val T = stdIn.readInt()
    for (_ <- 1 to T) {
      val line = stdIn.readLine().split(' ')
      val M = line.head.toInt
      val L = line(1).toInt

      val divm = getDivisorsOptimized(M)
      val divl = getDivisorsOptimized(L)

      val result = (divm intersect divl).size
      println(result)
    }

  }
}