package main.scala.hackerrank

object Fibonacci {

  def main(args: Array[String]): Unit = {

    val stdIn = scala.io.StdIn
    val modulo = 100000007

    def fibonacci(n: Int): Long = {
      def fibonacciTail(n: Int, a: Long, b: Long): Long = n match {
        case 0 => a
        case _ => fibonacciTail(n-1, b, (a+b) % modulo)
      }
      fibonacciTail(n, 0, 1)
    }

    val T = stdIn.readInt()
    for (_ <- 1 to T) {
      val n = stdIn.readInt()
      println(fibonacci(n))
    }
  }
}
