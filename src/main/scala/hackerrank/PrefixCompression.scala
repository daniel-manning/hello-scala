package main.scala.hackerrank

object PrefixCompression {

  def main(args: Array[String]): Unit = {
    val stdin = scala.io.StdIn

    val s1 = stdin.readLine()
    val s2 = stdin.readLine()

    val R = (s1 zip s2).zipWithIndex.filter(x => x._1._1 == x._1._2).zipWithIndex.filter( x => x._2 == x._1._2).map( x => x._1._1._1).mkString
    val l = R.length

    val r1 = s1.substring(l)
    val r2 = s2.substring(l)

    println(s"$l $R")
    println(s"${r1.length} $r1")
    println(s"${r2.length} $r2")
  }
}
