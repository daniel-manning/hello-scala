package hackerrank.polygon

import java.lang.Math.{pow, sqrt}

import scala.io.StdIn

object PerimeterOfAPolygon {

  def main(args: Array[String]): Unit = {
    val numberOfSides = StdIn.readLine().toInt
    var ar: List[(Int, Int)] = List()
    for (_ <- 0 until numberOfSides) {
      val aux: Array[Int] = StdIn.readLine().split(" ").map(_.trim.toInt)
      ar = ar ::: List((aux(0), aux(1)))
    }
    ar = ar ::: List(ar.head)

    var P: Double = 0
    for (i <- 0 until numberOfSides) {
      P += f(ar(i), ar(i + 1))
    }
    println(P)
  }

  def f(a: (Int, Int), b: (Int, Int)): Double = {
    val d = (a._1 - b._1, a._2 - b._2)
    sqrt(pow(d._1, 2) + pow(d._2, 2))
  }
}
