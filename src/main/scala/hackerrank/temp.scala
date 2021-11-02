package hackerrank

import java.io._
import java.math._
import java.security._
import java.text._
//import java.util._
import java.util.concurrent._
import java.util.function._
import java.util.regex._
import java.util.stream._

object temp {

  def createList(num: Int): List[Int] = {
    var list: List[Int] = List()
    for (i <- 1 to num) {
      list = list :+ 1
    }
    return list
  }

  def reverse(arr: List[Int]): List[Int] = {
    arr.foldRight(List(): List[Int]) { (e, acc) =>
      acc ::: List(e)
    }
  }

  def sumOdd(arr: List[Int]): Int = {
    arr.filter(_.abs % 2 == 1).sum
  }

  def count(arr: List[Int]): Int = {
    arr.fold(0) {
      (acc, _) => acc + 1
    }
  }

  def abs(arr: List[Int]): List[Int] = {
    arr.foldLeft(List[Int]()) {
      (acc, e) =>
        acc ::: List(
          if (e > 0) e else -e
        )
    }
  }

  def factorial(n: Int): Int = {
    if (n > 0) n * factorial(n - 1) else 1
  }

  def sequence(x: Double, n: Int): Double = {
    if(n > 0) scala.math.pow(x, n)/factorial(n) + sequence(x, n - 1) else 1
  }

  def main(args: Array[String]): Unit = {
    val stdin = scala.io.StdIn
    val n = stdin.readLine().trim.toInt
    val x = stdin.readLine().trim.toDouble

    println(sequence(x, n))
  }
}

