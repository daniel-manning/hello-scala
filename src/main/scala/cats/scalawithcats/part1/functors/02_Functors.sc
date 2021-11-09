import cats.Functor
import cats.instances.list._
import cats.instances.option._

import scala.concurrent.ExecutionContext // for Functor

val list1 = List(1, 2, 3)
// val list1: List[Int] = List(1, 2, 3)
val list2 = Functor[List].map(list1)(_ * 2)
// val list2: List[Int] = List(2, 4, 6)

val option1 = Option(123)
// val option1: Option[Int] = Some(123)
val option2 = Functor[Option].map(option1)(_.toString)
// val option2: Option[String] = Some(123)

// Lift
val func = (x: Int) => x + 1
// val func: Int => Int = <function>

val liftedFunc = Functor[Option].lift(func)
// val liftedFunc: Option[Int] => Option[Int] = cats.Functor<function>

liftedFunc(Option(1))
// val res0: Option[Int] = Some(2)

Functor[List].as(list1, "As")
// val res1: List[String] = List(As, As, As)

// Functor Syntax
import cats.instances.function._ // for Functor
import cats.syntax.functor._ // for map

val func1 = (a: Int) => a + 1
val func2 = (a: Int) => a * 2
val func3 = (a: Int) => s"$a!"
val func4 = func1.map(func2).map(func3)

func4(123)
// val res2: String = 248!

// Generic example
def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]) =
  start.map(n => n + 1 * 2)

import cats.instances.option._ // for Functor
import cats.instances.list._ // for Functor

doMath(Option(20))
// val res3: Option[Int] = Some(22)
doMath(List(1, 2, 3))
// val res4: List[Int] = List(3, 4, 5)

// Custom Types
import scala.concurrent.{Future, ExecutionContext}

implicit val ec: ExecutionContext = ExecutionContext.global

implicit def futureFunctor(implicit ec: ExecutionContext): Functor[Future] =
  new Functor[Future] {
    def map[A, B](fa: Future[A])(f: A => B) = fa.map(f)
  }

// We write this:
Functor[Future]

// The compiler expands to this first:
Functor[Future](futureFunctor)

// And then to this:
Functor[Future](futureFunctor(ec))
