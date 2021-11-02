package main.scala.hackerrank
import scala.io._

object ex2 {

  // Complete the compareTriplets function below.
  def compareTriplets(a: Array[Int], b: Array[Int]): Array[Int] = {
    getScore(a.zip(b).map(i => scorePoints(i._1, i._2)))
  }

  def scorePoints(x: Int, y: Int): String = {
    if (x < y) "B"
    else if (x > y) "A"
    else "0"
  }

def getScore(A: Array[String]): Array[Int] = {
  val a = A.filter(_ == "A").length
  val b = A.filter(_ == "B").length
  Array(a, b)
}

def main(args: Array[String]): Unit = {

  val a = StdIn.readLine().replaceAll("\\s+$", "").split(" ").map(_.trim.toInt)

  val b = StdIn.readLine().replaceAll("\\s+$", "").split(" ").map(_.trim.toInt)
  val result = compareTriplets(a, b)

  println(result.mkString(" "))

}
}
