package main.scala.cookbook

object Loops extends App {
    val a = Array("apple", "banana", "orange", "strawberry")
    for((e, count) <- a.zipWithIndex){
      println(s"$count is $e")
    }
    println("-1----------")

    val players = Map(5 -> "Garnett",
      8 -> "Bryant",
      13 -> "Chamberlain",
      16 -> "Gasol",
      21 -> "Duncan",
      23 -> "Jordan",
      24 -> "Bryant",
      32 -> "Johnson",
      33 -> "Abdul-Jabbar",
      34 -> "O'Neal")

    for((k,v) <- players) println(f"Number: $k%2d, player: $v")
    println("-2----------")

    val array = Array.ofDim[Int](2,2)
    array(0)(0) = 0
    array(0)(1) = 1
    array(1)(0) = 2
    array(1)(1) = 3

    for{
      i <- 0 to 1
      j <- 0 to 1
    } println(s"array($i)($j) = ${array(i)(j)}")
    println("-3----------")

    val names = Array("chris", "ed", "maurice")
    val capNames = for (e <- names) yield e.capitalize
    capNames.foreach(println)
}

import scala.util.control.Breaks
import util.control.Breaks._

//I'm not a big fan of break and continue but this is for learning's sake
object BreakAndContinue extends App {

  println("Break Example")
  breakable {
    for (i <- 1 to 10) {
      println(i)
      if (i > 4) break()
    }
  }

  println("Continue Example")
  val searchMe = "peter piper picked a peck of pickled peppers"
  var numPs = 0
  for (i <- 0 until searchMe.length) {
    breakable {
      if (searchMe.charAt(i) != 'p') {
        break()
      } else {
        numPs += 1
      }
    }
  }
  println("Found " + numPs + " p's in the string")

  println("Labeled Breaks")
  val Inner = new Breaks
  val Outer = new Breaks

  Outer.breakable {
    for (i <- 1 to 5) {
      Inner.breakable {
        for (j <- 'a' to 'e') {
          if (i == 1 && j == 'c') Inner.break() else println(s"i: $i, j $j")
          if (i == 2 && j == 'b') Outer.break()
        }
      }
    }
  }
}

import scala.annotation.switch

object MatchAndSwitch extends App {

  println("Switch example")
  val i = 1
  val x = (i: @switch) match {
    case 1 => "One"
    case 2 => "Two"
    case _ => "Other"
  }
  println(x)

  println("Match Or example")
  val j = 5
  j match {
    case 1 | 3 | 5 | 7 | 9 => println("odd")
    case 2 | 4 | 6 | 8 | 10 => println("even")
  }

  println("Match Or Object example")

  trait Command
  case object Start extends Command
  case object Go extends Command
  case object Stop extends Command
  case object Whoa extends Command

  def executeCommand(cmd: Command): Unit = cmd match {
    case Start | Go => println("Engine started")
    case Stop | Whoa => println("Engine stopped")
  }

  executeCommand(Start)

  println("Match With List example")
  def listToString(list: List[String]): String = list match{
    case s :: rest => s + " " + listToString(rest)
    case Nil => ""
  }

  val fruits = "Apples" :: "Bananas" :: "Oranges" :: Nil
  println(listToString(fruits))
}

object MatchExample extends App{
  println("Obscure Match example")
  case class Person(name: String, age: Int)

  def echoMatches(a: Any): String = a match {
    case Person("Enrique", _) => "Enrique"
    case x: Person if x.age < 18 => "No minors allowed"
    case Person(name, age) => s"$name is $age years old"
    case List(_, _, _) => "A list of three elements"
    case List(1, _*) => "A list starting by one"
    case Vector(1, _*) => "A vector starting by one"
    case (a, b, c) => s"This is a triple ($a, $b, $c)"
    case (a, _) => "This is a pair where the first element is " + a
    case list @ List(2, _*) => s"The list $list"
    case list: List[_] => "A list!"
    case x @ Some(_) => x.toString
    case a if 0 to 9 contains a => "This is a small number"
    case _ => "I don't know what this is"
  }

  println(echoMatches(Person("Enrique", 24)))
  println(echoMatches(Person("Junior", 16)))
  println(echoMatches(Person("Alicia", 24)))
  println(echoMatches(List(0,1,2)))
  println(echoMatches(List(1,2,3,4,5,6)))
  println(echoMatches(Vector(1,2,3,4)))
  println(echoMatches(("Hola", 24, 0.3)))
  println(echoMatches(("Hey", "Yay!")))
  println(echoMatches(List("a","b","c", "d")))
  println(echoMatches(List(2,1,0,-1,-2,-3)))
  println(echoMatches(Some("Hello")))
  println(echoMatches(3))
  println(echoMatches({r: Int => 2*r}))
}

