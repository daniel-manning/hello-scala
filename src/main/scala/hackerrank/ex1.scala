package hackerrank

object ex1 {

  /*
   * Complete the simpleArraySum function below.
   */
  def simpleArraySum(ar: Array[Int]): Int = {
    var sum = 0
    for (i <- ar.indices) {
      sum = sum + ar(i)
    }
    sum
  }

  def main(args: Array[String]): Unit = {
    val stdin = scala.io.StdIn

    val arCount = stdin.readLine().trim.toInt

    val ar = stdin.readLine().split(" ").map(_.trim.toInt)
    val result = simpleArraySum(ar)

    println(result)

  }
}

