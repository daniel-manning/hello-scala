package main.scala.numberTheory

import scala.io.StdIn
import scala.language.implicitConversions

class Rational(n: Int, d: Int){
  require(d != 0)

  private val g = gcd(n.abs, d.abs)
  val numerator: Int = n / g
  val denominator: Int = d / g

  def this(n: Int) = this(n, 1)

  override def toString: String = s"$numerator/$denominator"


  def +(that: Rational): Rational =
    new Rational(
      numerator * that.denominator + that.numerator * denominator,
      denominator * that.denominator
    )
  def +(i: Int): Rational = new Rational(numerator + i * denominator, denominator)

  def -(that: Rational): Rational =
    new Rational(
      numerator * that.denominator - that.numerator * denominator,
      denominator * that.denominator
    )
  def -(i: Int): Rational = new Rational(numerator - i * denominator, denominator)

  def *(that: Rational): Rational = new Rational(numerator * that.numerator, denominator * that.denominator)
  def *(i: Int): Rational = new Rational(numerator * i, denominator)

  def / (that: Rational): Rational = new Rational(numerator * that.denominator, denominator * that.numerator)
  def / (i: Int): Rational = new Rational(numerator, denominator * i)

  def lessThan(that: Rational): Boolean = this.numerator * that.denominator < that.numerator * this.denominator

  def max(that: Rational): Rational = if (lessThan(that)) that else this // see, it understands lessThan(that) as this.lessThan(that)

  private def gcd(a: Int, b: Int): Int = if (b==0) a else gcd(b, a % b)
}

object Main {
  def main(args: Array[String]): Unit = {
    val a = new Rational(1, 2)
    val b = new Rational(2, 3)
    println(a + b)
    println(a * b)
    println(a + a * b)
    println(new Rational(5))
    println("-------------------------------")
    val x = new Rational(2, 3)
    println(x * x)
    println(x * 2)
    println("-------------------------------")
    implicit def intToRational(x: Int): Rational = new Rational(x)
    val r = new Rational(2, 5)
    println(2 * r)


  }
}
