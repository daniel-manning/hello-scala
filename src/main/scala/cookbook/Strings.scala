package main.scala.cookbook

object Strings extends App {
  println("scala".drop(2).take(2).capitalize)
  println("scala".slice(2, 4).capitalize)
  println("-1---------")

  val s1 = "Hello world"
  val s2 = "H" + "ello world"
  println(s1 == s2)
  println("-2---------")

  val s = "eggs, milk, butter, Coco Puffs"
  s.split(",").map(_.trim).foreach(println)
  println("-3---------")

  val name = "Enrique"
  val age = 24
  val weight = 95.00
  println(s"$name is $age years old and weighs $weight kilograms.")
  println(s"Age next year: ${age + 1}")
  println("-4---------")

  case class Employee(name: String, age: Int)

  val enrique = Employee(name, age)
  println(s"${enrique.name} is ${enrique.age} years old")
  println("-5---------")

  println(f"$name is $age years old and weighs $weight%.2f kilograms")
  println(f"$name is $age years old and weighs $weight%.0f kilograms")
  println("-6---------")

  val numPattern = "[0-9]+".r
  val address = "123 Main Street Suite 101"

  val match1 = numPattern.findFirstIn(address)
  match1.foreach(e => println(s"Found a match: $e"))
  println("-7---------")

  val matches = numPattern.findAllIn(address).toArray
  matches foreach println
  println("-8---------")

  val pattern = "([0-9]+) ([A-Za-z]+)".r
  val pattern(count, fruit) = "100 bananas"
  println(s"We have found a quantity of $count items of the fruit $fruit")
  println("-9---------")

  implicit class StringImprovements(s: String) {
    def increment: String = s.map(c => (c + 1).toChar)

    def decrement: String = s.map(c => (c - 1).toChar)

    def hideAll: String = s.replaceAll(".", "*")
  }

  println("Dmqhptd".increment)
  println("Fosjrvf".decrement)
  println("Enrique".hideAll)
  println("-10--------")
}
