
import cats.Semigroupal
import cats.instances.either._ // for Semigroupal

type ErrorOr[A] = Either[Vector[String], A]
val error1: ErrorOr[Int] = Left(Vector("Error 1"))
val error2: ErrorOr[Int] = Left(Vector("Error 2"))

// Boring normal product (fail-fast):
Semigroupal[ErrorOr].product(error1, error2)
// val res0: ErrorOr[(Int, Int)] = Left(Vector(Error 1))

import cats.syntax.apply._ // for tupled
import cats.instances.vector._ // for Semigroup on Vector

// A bit cooler normal product syntax:
(error1, error2).tupled
// val res1: ErrorOr[(Int, Int)] = Left(Vector(Error 1))

// The Parallel type class accesses alternate semantics for certain monads
import cats.syntax.parallel._ // for parTupled

// Cool Parallel implementation, both error are returned!
(error1, error2).parTupled
// res2: ErrorOr[(Int, Int)] = Left(Vector("Error 1", "Error 2"))

// Example with List
import cats.instances.list._ // for Semigroup on List

type ErrorOrList[A] = Either[List[String], A]
val errStr1: ErrorOrList[Int] = Left(List("error 1"))
val errStr2: ErrorOrList[Int] = Left(List("error 2"))

(errStr1, errStr2).parTupled
// val res3: ErrorOrList[(Int, Int)] = Left(List(error 1, error 2))

// There are many syntax methods provided by Parallel.
// Here is an example of parMapN in an error handling situation.
val success1: ErrorOr[Int] = Right(1)
val success2: ErrorOr[Int] = Right(2)
val addTwo = (x: Int, y: Int) => x + y

(error1, error2).parMapN(addTwo)
// val res4: ErrorOr[Int] = Left(Vector(Error 1, Error 2))
(success1, success2).parMapN(addTwo)
// val res5: ErrorOr[Int] = Right(3)

// Exercise:
// Does List have a Parallel instance?
import cats.instances.list._
(List(1, 2), List(3, 4)).tupled
// val res6: List[(Int, Int)] = List((1,3), (1,4), (2,3), (2,4))
(List(1, 2), List(3, 4)).parTupled
// val res7: List[(Int, Int)] = List((1,3), (2,4))
