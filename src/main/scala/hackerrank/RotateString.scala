package main.scala.hackerrank

object RotateString {

  def main(args: Array[String]): Unit = {

    val stdIn = scala.io.StdIn

    def rotate(s: String): String = s.tail + s.head

    def rotateNTimes(s: String, n: Int): String = if (n <= 0) s else rotateNTimes(rotate(s), n-1)

    def getRotations(s: String) = {
      (1 to s.length).map( n => rotateNTimes(s, n)).mkString(" ")
    }

    val T = stdIn.readInt()
    for (_ <- 1 to T) {
      val line = stdIn.readLine().trim
      println(getRotations(line))
    }
  }
}