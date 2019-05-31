package hackerrank

object StringMingling {

  def main(args: Array[String]) {
    val stdin = scala.io.StdIn

    val P = stdin.readLine
    val Q = stdin.readLine

    val R = (P zip Q).map(e => Seq(e._1, e._2).mkString).mkString
    println(R)
  }
}
