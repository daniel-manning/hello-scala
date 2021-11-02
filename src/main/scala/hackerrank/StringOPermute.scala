package main.scala.hackerrank

object StringOPermute {

  def main(args: Array[String]): Unit = {
    val stdin = scala.io.StdIn

    val i = stdin.readInt()

    for(_ <- 0 until i){
      val line = stdin.readLine()
      val (a, b) = line.zipWithIndex.partition(_._2 % 2 == 0)
      val result = (b zip a).map(e => Seq(e._1._1, e._2._1).mkString).mkString
      println(result)
    }
  }
}
