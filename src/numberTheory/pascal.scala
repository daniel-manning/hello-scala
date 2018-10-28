package numberTheory

import scala.io.StdIn

object pascal {
  def pascalTriangle(n:Int): Unit ={
    for (i <- Range(0, n)){
     println(pascalRow(i).mkString(" "))
    }
  }

  def factorial (n: Int): Int = if (n == 0) 1 else n * factorial(n-1)

  def pascalCalc(row: Int, column: Int): Int = {
    factorial(row)/(factorial(column)* factorial(row-column))
  }

  def pascalRow(row: Int): List[Int] = {
    Range(0, row+1).toList.foldLeft(List(0)){ (acc, e) => acc ::: List(pascalCalc(row, e))}.tail
  }

  def main(args: Array[String]) {
    pascalTriangle(StdIn.readLine().toInt)
  }
}
