package hackerrank.polygon

import java.lang.Math._

object AreaOfAPolygonFunctions {

  /** Points in the cartesian plane*/
  type Point = (Int, Int)

  val origin: Point = (0, 0)

  /** Calculate the length of a segment*/
  def f(a: Point, b: Point): Double = {
    val d = (a._1 - b._1, a._2 - b._2)
    sqrt(pow(d._1, 2) + pow(d._2, 2))
  }

}
