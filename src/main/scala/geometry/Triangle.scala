package main.scala.geometry


case class Triangle(a: (Int, Int), b: (Int, Int), c: (Int, Int))

object Triangle {

  val plane = new CartesianPlane()

  def makeATheOrigin(t: Triangle): Triangle = {
    val (x, y) = t.a
    val newB = (t.b._1 - x, t.b._2 - y)
    val newC = (t.c._1 - x, t.c._2 - y)
    new Triangle(plane.origin, newB, newC)
  }

  def makeABTheBase(t: Triangle): Triangle = {
    makeATheOrigin(t)
    val xb: plane.Point = (t.b._1, 0)
    if (xb == plane.origin) {
      val newB = (-t.b._2, t.b._1)
      val newC = (-t.c._2, t.c._1)
      new Triangle(t.a, newB, newC)
    }
    else {
      val alpha = Math.acos(xb._1 / a(plane.origin, xb))

      /** TODO: CALCULATE ROTATION OF A TRIANGLE */
    }
    t //borrar
  }
  def a(point: (Int, Int), point1: (Int, Int)): Int ={ //borrar
    2
  }
}
