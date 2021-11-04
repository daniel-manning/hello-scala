// We have already seen functors:
List(1, 2, 3).map(n => n + 1)
// val res0: List[Int] = List(2, 3, 4)

// Because map leaves the structure unchanged we can call it repeatedly
List(1, 2, 3)
  .map(n => n + 1)
  .map(n => n * 2)
  .map(n => s"${n}!")
// val res1: List[String] = List(4!, 6!, 8!)

// Example with futures (they are not referentially transparent!)
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

val future: Future[String] =
  Future(123)
    .map(n => n + 1)
    .map(n => n * 2)
    .map(n => s"${n}!")

Await.result(future, 1.second)
// val res2: String = 248!

// Functions!?
import cats.instances.function._
import cats.syntax.functor._

val func1: Int => Double = x => x.toDouble
val func2: Double => Double = y => y * 2

(func1 map func2)(1) // composition using map
// val res3: Double = 2.0
(func1 andThen func2)(1) // composition using andThen
// res4: Double = 2.0
func2(func1(1)) // composition written out by hand
// val res5: Double = 2.0

// Function queuing is similar to Future
val func = ((x: Int) => x.toDouble)
  .map(x => x + 1)
  .map(x => x * 2)
  .map(x => s"$x!")

func(123)
// val res6: String = 248.0!

