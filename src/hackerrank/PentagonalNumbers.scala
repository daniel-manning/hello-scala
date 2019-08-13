package hackerrank

object PentagonalNumbers {

  def main(args: Array[String]) {

    val stdIn = scala.io.StdIn

    val T = stdIn.readInt()
    for (_ <- 1 to T) {
      val n = stdIn.readLong()
      println((3*n-1)*n/2)
    }
  }
}
