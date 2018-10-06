package hackerrank

object ex6 {

  // Complete the aVeryBigSum function below.
  def f(arr:List[Int]):List[Int] = {
    arr.indices.filter(n => n % 2 != 0).map(arr(_)).toList
  }

  def main(args: Array[String]) {
    val stdin = scala.io.StdIn


    val arCount = stdin.readLine.trim.toInt

    val ar: List[Int] = stdin.readLine.split(" ").map(_.trim.toInt).toList
    val result = f(ar)

    println(result)


  }
}
