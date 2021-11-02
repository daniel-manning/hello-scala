package hackerrank

import java.lang.Math._

object integral {


  // This function will be used while invoking "Summation" to compute
  // The area under the curve.
  def f(coefficients: List[Int], powers: List[Int], x: Double): Double = {
    //Fill Up this function body
    // To compute the value of the function
    // For the given coefficients, powers and value of x
    var sum: Double = 0
    for(i <- coefficients.indices) {
      sum = sum + coefficients(i) * pow(x, powers(i))
    }
    sum
  }

  // This function will be used while invoking "Summation" to compute
  // The Volume of revolution of the curve around the X-Axis
  // The 'Area' referred to here is the area of the circle obtained
  // By rotating the point on the curve (x,f(x)) around the X-Axis
  def area(coefficients: List[Int], powers: List[Int], x: Double): Double = {
    //Fill Up this function body
    // To compute the area of the circle on revolving the point
    // (x,f(x)) around the X-Axis
    // For the given coefficients, powers and value of x
    PI * pow(f(coefficients, powers, x), 2)
  }

  // This is the part where the series is summed up
  // This function is invoked once with func = f to compute the area 	     // under the curve
  // Then it is invoked again with func = area to compute the volume
  // of revolution of the curve
  def summation(func: (List[Int], List[Int], Double) => Double, upperLimit: Int, lowerLimit: Int, coefficients: List[Int], powers: List[Int]): Double = {
    // Fill up this function
    var i : Double = lowerLimit
    var result :Double = 0
    while (i < upperLimit){
      result = result + func(coefficients, powers, i)*0.001
      i = i + 0.001
    }
    result
  }

  // The Input-Output functions will be handled by us. You only need to concentrate your effort on the function bodies above.
  def main(args: Array[String]): Unit = {
    val w = List(1 , 2)
    val y = List(0, 1)
    val z = (2, 20)

    println(summation(f, z._2, z._1, w, y))
    println(summation(area, z._2, z._1, w, y))
  }
}