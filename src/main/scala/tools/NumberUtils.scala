package main.scala.tools

object NumberUtils{
  //Compares two Doubles up to a given precision epsilon
  def equalWithinTolerance(x: Double, y: Double, epsilon: Double): Boolean = if((x-y).abs < epsilon) true else false
}

object NumberUtilsTest{
  def main(args: Array[String]): Unit = {
    val d1 = 1.0000001
    val d2 = 1.0000002
    val epsilon = 0.00001
    val result = NumberUtils.equalWithinTolerance(d1, d2, epsilon)
    println(result)
    println(NumberUtils.equalWithinTolerance(1, 2, epsilon))
  }
}
