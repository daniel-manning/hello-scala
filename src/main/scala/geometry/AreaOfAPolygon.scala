package main.scala.geometry

import scala.io.StdIn

object AreaOfAPolygon {

  type Point = (Double, Double)

  def main(args: Array[String]): Unit = {


    /** First argument is number of vertices */
    val numberOfSides = StdIn.readLine().toInt

    /** The following arguments are the coordinates of the vertices */
    var ar: List[Point] = List()
    for (_ <- 0 until numberOfSides) {
      val aux: Array[Double] = StdIn.readLine().split(" ").map(_.trim.toDouble)
      ar = ar ::: List((aux(0), aux(1)))
    }

    /** Calculation of the area of the polygon */
    val triangleList: Seq[(Point, Point, Point)] = catalanTriangulation(ar)
    val result = triangleList.foldLeft(0D){
      (acc, t) => acc + getAreaOfTriangle(t._1, t._2, t._3)
    }
    println(result)
  }

  /** Descomposition of a non-self-intersecting polygon in triangles */
  def catalanTriangulation(vertices: Seq[Point]): Seq[(Point, Point, Point)] = {
    val n: Int = vertices.length
    var triangleList: List[(Point, Point, Point)] = List()
    for (i <- 1 until n - 1) {
      val triangle = (vertices.head, vertices(i), vertices(i+1))
      triangleList = triangleList ::: List(triangle)
    }
    triangleList
  }

  /** Area of a triangle using the 3 vertices formula */
  def getAreaOfTriangle(a: Point, b: Point, c: Point): Double = {
    (a._1 * (b._2 - c._2) + b._1*(c._2 - a._2) + c._1*(a._2 - b._2)) / 2
  }

}


