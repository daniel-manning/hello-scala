import cats.Semigroupal

// Futures:
import cats.instances.future._ // for Semigroupal
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

val futurePair: Future[(String, Int)] =
  Semigroupal[Future].product(Future("Hello"), Future(123))

Await.result(futurePair, 1.second)
// val res0: (String, Int) = (Hello,123)

// The two Futures start executing the moment we create them,
// so they are already calculationg results by the time we call product.

// apply syntax
import cats.syntax.apply._ // for mapN

case class Cat(
    name: String,
    yearOfBirth: Int,
    favoriteFoods: List[String]
)

val futureCat = (
  Future("Garfield"),
  Future(1978),
  Future(List("Lasagne"))
).mapN(Cat.apply)

// Woah!!
Await.result(futureCat, 1.second)
// val res1: Cat = Cat(Garfield,1978,List(Lasagne))

// Lists:
// Actually returns the cartesian product of their elements
import cats.instances.list._ // for Semigroupal

Semigroupal[List].product(List(1, 2), List(3, 4))
// val res2: List[(Int, Int)] = List((1,3), (1,4), (2,3), (2,4))

// Either:
// It still fails fast but it is possible to examine the other failures:
import cats.instances.either._ // for Semigroupal

type ErrorOr[A] = Either[Vector[String], A]

Semigroupal[ErrorOr].product(
  Left(Vector("Error 1")),
  Left(Vector("Error 2"))
)
// val res3: ErrorOr[(Nothing, Nothing)] = Left(Vector(Error 1))

// So... since monads extend semigroups we get those weird products
// Even the Future example is just a trick of the light, since it is equivalent to doing:
val a = Future("Future 1")
val b = Future("Future 2")

for {
  x <- a
  y <- b
} yield (x, y)

// We must then explore Semigroupals that are not Monads!