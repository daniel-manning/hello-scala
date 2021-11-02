package hackerrank

import scala.collection.mutable.ArrayBuffer

object functionCheck {

  def main(args: Array[String]): Unit = {
    val stdin = scala.io.StdIn
    var testCases: ArrayBuffer[(Int, Int)] =  new ArrayBuffer()

    /* We get the number of functions to test */
    val numberOfFunctions = stdin.readLine().toInt

    for (_ <- 1 to numberOfFunctions){

      /* We get the number of tests for this function */
      val n = stdin.readLine().toInt

      for(_ <- 1 to n){
        val newLine = stdin.readLine().split(" ").map(_.trim.toInt)
        val pair = (newLine(0), newLine(1))
        testCases += pair
      }

      /* Main logic */
      val a = testCases.map(
        D => {
          val checkFunction = testCases.filter( d => d._1 == D._1 && d._2 != D._2)
          checkFunction.isEmpty
        }
      ).forall(_ == true)

      /* Output: is the test a function? */
      if(a) println("YES")
      else println("NO")

      testCases = new ArrayBuffer[(Int, Int)]()

    }
  }
}