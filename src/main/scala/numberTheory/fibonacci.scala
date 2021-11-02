package numberTheory

import scala.io.StdIn

object fibonacci {

  def fibonacci(x:Int):Int = {
    if (x == 1) 0
    else if (x==2) 1
    else fibonacci(x-1) + fibonacci(x-2)
  }

  def main(args: Array[String]): Unit = {
    println(fibonacci(StdIn.readInt()))

  }
}
