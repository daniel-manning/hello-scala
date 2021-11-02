package hackerrank

object ex5 {

  // Complete the aVeryBigSum function below.
  def f(delim:Int,arr:List[Int]):List[Int] = {
    var result :List[Int] = List()
    arr.foreach(e => if (e < delim) result = result :+ e)
    result
  }

  def main(args: Array[String]): Unit = {
    val stdin = scala.io.StdIn


    val arCount = stdin.readLine().trim.toInt

    val ar: List[Int] = stdin.readLine().split(" ").map(_.trim.toInt).toList
    val result = f(arCount, ar)

    println(result)


  }
}
