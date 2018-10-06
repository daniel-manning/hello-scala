package hackerrank


object Solution {

  // Complete the aVeryBigSum function below.
  def f(num: Int, arr: List[Int]): List[Int] = {
    var result: List[Int] = List()
    val lista: Seq[(Int, Int)] = for {i <- arr.indices
                                      j <- 1 to num
    } yield (i, j)
    lista.foreach(t => result = result :+ arr(t._1))
    result
  }

  def main(args: Array[String]) {
    val stdin = scala.io.StdIn


    val arCount = stdin.readLine.trim.toInt

    val ar: List[Int] = stdin.readLine.split(" ").map(_.trim.toInt).toList
    val result = f(arCount, ar)

    println(result)


  }
}
