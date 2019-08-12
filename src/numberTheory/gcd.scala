package numberTheory

import scala.io.StdIn

object gcd {

  def gcd(x: Int, y: Int): Int =
  {
    if (x == y) x
    else if (x > y) gcd(x-y, y)
    else gcd(y-x, x)
  }

  def acceptInputAndComputeGCD(pair:List[Int]): Unit = {
    println(gcd(pair.head,pair.reverse.head))
  }

  def main(args: Array[String]) {
    acceptInputAndComputeGCD(StdIn.readLine().trim().split(" ").map(x=>x.toInt).toList)

  }
}