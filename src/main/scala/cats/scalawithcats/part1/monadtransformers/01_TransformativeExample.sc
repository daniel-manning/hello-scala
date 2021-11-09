import cats.data.OptionT

type ListOption[A] = OptionT[List, A]

import cats.instances.list._ // for Monad
import cats.syntax.applicative._ // for pure

val result1: ListOption[Int] = OptionT(List(Option(10)))
// val result1: ListOption[Int] = OptionT(List(Some(10)))
val result2: ListOption[Int] = 32.pure[ListOption]
// val result2: ListOption[Int] = OptionT(List(Some(32)))

result1.flatMap { (x: Int) =>
  result2.map { (y: Int) =>
    x + y
  }
}
// val res0: cats.data.OptionT[List,Int] = OptionT(List(Some(42)))

// Monad transformers have only one free type parameter on the type constructor
// So we need a type alias to use things like Either

// Alias Either to a type constructor with one parameter:
type ErrorOr[A] = Either[String, A]

// Build our final monad stack
type ErrorOrOption[A] = OptionT[ErrorOr, A]

// ErrorOrOption is a monad, just like ListOption
import cats.instances.either._ // for Monad

val a = 10.pure[ErrorOrOption]
// val a: ErrorOrOption[Int] = OptionT(Right(Some(10)))
val b = 32.pure[ErrorOrOption]
// val b: ErrorOrOption[Int] = OptionT(Right(Some(32)))
val c = a.flatMap(x => b.map(y => x + y))
// val c: cats.data.OptionT[ErrorOr,Int] = OptionT(Right(Some(42)))

// What if we want to stack three or more monads?
import scala.concurrent.Future
import cats.data.{EitherT, OptionT}

type FutureEither[A] = EitherT[Future, String, A]
type FutureEitherOption[A] = OptionT[FutureEither, A]

// Now we can compose three monads and use monadic transformations
import cats.instances.future._ // for Monad
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

val futureEitherOr: FutureEitherOption[Int] =
  for {
    a <- 10.pure[FutureEitherOption]
    b <- 32.pure[FutureEitherOption]
  } yield a + b

// Constructing and unpacking instances

// We can create transformed monad stacks using apply
val errorStack1 = OptionT[ErrorOr, Int](Right(Some(10)))
// val errorStack1: cats.data.OptionT[ErrorOr,Int] = OptionT(Right(Some(10)))
// Or using pure:
val errorStack2 = 32.pure[ErrorOrOption]
// val errorStack2: ErrorOrOption[Int] = OptionT(Right(Some(32)))

// Extracting the untransformed monad stack:
errorStack1.value
// val res1: ErrorOr[Option[Int]] = Right(Some(10))

// Mapping over the Either in the stack:
errorStack2.value.map(_.getOrElse(-1))
// val res2: scala.util.Either[String,Int] = Right(32)

// Each call to value unpacks a single monad transformer:
futureEitherOr
// val res3: FutureEitherOption[Int] = OptionT(EitherT(Future(Success(Right(Some(42))))))
val intermediate = futureEitherOr.value
// val intermediate: FutureEither[Option[Int]] = EitherT(Future(Success(Right(Some(42)))))
val stack = intermediate.value
// val stack: scala.concurrent.Future[Either[String,Option[Int]]] = Future(Success(Right(Some(42))))
Await.result(stack, 1.second)
val res4: Either[String,Option[Int]] = Right(Some(42))