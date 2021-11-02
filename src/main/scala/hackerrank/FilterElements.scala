package hackerrank

object FilterElements {

  def main(args: Array[String]): Unit = {
    val stdin = scala.io.StdIn
    for(_ <- 0 until stdin.readInt()){
      def readArray: Array[Int] = stdin.readLine().trim.split(' ').map(_.toInt)

      val args = readArray
      val array = readArray

      val map = array.groupBy(identity).view.mapValues(_.length)
      val result = array.distinct.filter(map(_) >= args(1))

      println(if (result.isEmpty) "-1" else result.mkString(" "))
    }
  }
}
