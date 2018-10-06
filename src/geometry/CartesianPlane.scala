package geometry

import java.lang.Math.{pow, sqrt}

case class CartesianPlane() {
  /** Points in the cartesian plane*/
  type Point = (Int, Int)

  val origin: Point = (0, 0)
}

object CartesianPlaneFunctions {
  val plane = new CartesianPlane()

  /** Calculate the length of a segment */
  def f(a: plane.Point, b: plane.Point): Double = {
    val d = (a._1 - b._1, a._2 - b._2)
    sqrt(pow(d._1, 2) + pow(d._2, 2))
  }
}
