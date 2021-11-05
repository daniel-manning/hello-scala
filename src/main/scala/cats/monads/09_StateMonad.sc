import cats.data.State

import scala.util.Try
import scala.util.control.Exception

val a = State[Int, String] { state =>
  (state, s"The state is $state")
}

// Get the state and the result:
val (state, result) = a.run(10).value
// val state: Int = 10
// val result: String = The state is 10

// Get the state, ignore the result:
val justTheState = a.runS(10).value
// val justTheState: Int = 10

// Get the result, ignore the state:
val justTheResult = a.runA(10).value
// val justTheResult: String = The state is 10

// Composing and transforming state
val step1 = State[Int, String] { num =>
  val ans = num + 1
  (ans, s"Result of step1: $ans")
}

val step2 = State[Int, String] { num =>
  val ans = num * 2
  (ans, s"Result of step2: $ans")
}

val both = for {
  a <- step1
  b <- step2
} yield (a, b)

val (state, result) = both.run(20).value
// val state: Int = 42
// val result: (String, String) = (Result of step1: 21,Result of step2: 42)

// get extracts the state as the result
val getDemo = State.get[Int]
// val getDemo: cats.data.State[Int,Int] = cats.data.IndexedStateT@8d68810
getDemo.run(10).value
// val res0: (Int, Int) = (10,10)

// set updates the state and returns unit as the result
val setDemo = State.set[Int](30)
// val setDemo: cats.data.State[Int,Unit] = cats.data.IndexedStateT@25696e57
setDemo.run(10).value
// val res1: (Int, Unit) = (30,())

// pure ignores the state and returns a supplied result
val pureDemo = State.pure[Int, String]("Result")
// val pureDemo: cats.data.State[Int,String] = cats.data.IndexedStateT@7dc6c6f0
pureDemo.run(10).value
// val res2: (Int, String) = (10,Result)

// inspect extracts the state via a transformation function
val inspectDemo = State.inspect[Int, String](x => s"$x!")
// val inspectDemo: cats.data.State[Int,String] = cats.data.IndexedStateT@c7b3ec2
inspectDemo.run(10).value
// val res3: (Int, String) = (10,10!)

// modify updates the state using an update function.
val modifyDemo = State.modify[Int](_ + 1)
// modifyDemo: State[Int, Unit] = cats.data.IndexedStateT@44ddcbfc
modifyDemo.run(10).value
// val res4: (Int, Unit) = (11,())

// Those are the building blocks for for comprehensions
// We typically ignore the result of steps that only transform the state
import State._
val program: State[Int, (Int, Int, Int)] = for {
  a <- get[Int]
  _ <- set[Int](a + 1)
  b <- get[Int]
  _ <- modify[Int](_ + 1)
  c <- inspect[Int, Int](_ * 1000)
} yield (a, b, c)

val (state, result) = program.run(1).value
// val state: Int = 3
// val result: (Int, Int, Int) = (1,2,3000)

// Trying the same with nested flat maps
val program2: State[Int, (Int, Int, Int)] = get[Int].flatMap { a =>
  set[Int](a + 1).flatMap { _ =>
    get[Int].flatMap { b =>
      modify[Int](_ + 1).flatMap { _ =>
        inspect[Int, Int](_ * 1000).map { c =>
          (a, b, c)
        }
      }
    }
  }

}

val (state2, result2) = program2.run(1).value
// val state2: Int = 3
// val result2: (Int, Int, Int) = (1,2,3000)

// Exercise
type CalcState[A] = State[List[Int], A]

def operand(num: Int): CalcState[Int] =
  State[List[Int], Int](stack => (num :: stack, num))

def operator(f: (Int, Int) => Int): CalcState[Int] =
  State[List[Int], Int] {
    case a :: b :: tail =>
      val ans = f(a, b)
      (ans :: tail, ans)
    case _ => sys.error("Fail!")
  }

def evalOne(sym: String): CalcState[Int] = sym match {
  case "+" => operator(_ + _)
  case "-" => operator(_ - _)
  case "*" => operator(_ * _)
  case "/" => operator(_ / _)
  case num => operand(num.toInt)
}

evalOne("42").runA(Nil).value
// val res5: Int = 42

// Most of the work goes on the stack so we can ignore results for numbers
val program = for {
  _ <- evalOne("1")
  _ <- evalOne("2")
  ans <- evalOne("+")
} yield ans

program.runA(Nil).value
// val res6: Int = 3

// Part 2: evalAll implementation
import cats.syntax.applicative._

def evalAll(input: List[String]): CalcState[Int] =
  input.foldLeft(0.pure[CalcState]) { (acc, e) =>
    acc.flatMap(_ => evalOne(e))
  }

val multistageProgram = evalAll(List("1", "2", "+", "3", "*"))

multistageProgram.runA(Nil).value

// example
val biggerProgram = for {
  _ <- evalAll(List("1", "2", "+"))
  _ <- evalAll(List("3", "4", "+"))
  ans <- evalOne("*")
} yield ans
// val biggerProgram: cats.data.IndexedStateT[cats.Eval,List[Int],List[Int],Int] = cats.data.IndexedStateT@5ed9817c

biggerProgram.runA(Nil).value
// val res8: Int = 21

def evalInput(input: String) =
  evalAll(input.split(" ").toList).runA(Nil).value

evalInput("1 2 + 3 4 + *")
// val res9: Int = 21