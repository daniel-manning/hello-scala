package hackerrank.polygon

import hackerrank.polygon.AreaOfAPolygonFunctions._

import scala.io.StdIn

object AreaOfAPolygon {

  def main(args: Array[String]): Unit = {
    /** First argument is number of vertices*/
    val numberOfSides = StdIn.readLine().toInt

    /** The following arguments are the coordinates of the vertices*/
    var ar: List[Point] = List()
    for (_ <- 0 until numberOfSides) {
      val aux: Array[Int] = StdIn.readLine().split(" ").map(_.trim.toInt)
      ar = ar ::: List((aux(0), aux(1)))
    }

    /** Calculation of the area of the polygon */

  }


  def catalanTriangulation(vertex: Point, vertices: Seq[Point]) = {
    val n: Int = vertices.length

  }
}

class Triangle(a :(Int, Int), b: (Int, Int), c: (Int, Int)) {

  def makeATheOrigin(t: Triangle): Triangle ={
    val (x, y) = t.a
    val newB = (b._1 - x, b._2 - y)
    val newC = (c._1 - x, c._2 - y)
    new Triangle(origin, newB, newC)
  }

  def makeABTheBase(t: Triangle): Triangle ={
    makeATheOrigin(t)
    val xb : Point = (b._1 , 0)
    if (xb == origin){
      val newB = (-b._2, b._1)
      val newC = (-c._2, c._1)
      new Triangle(a, newB, newC)
    }
    else{
      val alpha = Math.acos(xb._1/f(origin, xb))
/** TODO: CALCULATE ROTATION OF A TRIANGLE */
    }
  }
}