import cats.MonadError
import cats.instances.either._ // for MonadError

type ErrorOr[A] = Either[String, A]

val monadError = MonadError[ErrorOr, String]

val success = monadError.pure(42)
// val success: ErrorOr[Int] = Right(42)
val failure = monadError.raiseError("Badness")
// val failure: ErrorOr[Nothing] = Left(Badness)

// handling one error
monadError.handleErrorWith(failure) {
  case "Badness" => monadError.pure("It is ok")
  case _         => monadError.raiseError("It is not ok")
}
// val res0: ErrorOr[String] = Right(It is ok)

// handling all errors
monadError.handleError[Int](failure) {
  case "Badness" => 42
  case _         => -1
}
// val res1: ErrorOr[Int] = Right(42)

monadError.ensure(success)("Number too low!")(_ > 1000)
// val res2: ErrorOr[Int] = Left(Number too low!)

// Error syntax!
import cats.syntax.applicative._ // for pure
import cats.syntax.applicativeError._ // for raiseError etc
import cats.syntax.monadError._ // for ensure

val success2 = 42.pure[ErrorOr]
// val success2: ErrorOr[Int] = Right(42)
val failure2 = "Badness".raiseError[ErrorOr, Int]
// val failure2: ErrorOr[Int] = Left(Badness)
failure2.handleErrorWith {
  case "Badness" => 256.pure
  case _         => "It's not ok".raiseError
}
// val res3: ErrorOr[Int] = Right(256)
success2.ensure("Number too low!")(_ > 1000)
// val res4: ErrorOr[Int] = Left(Number too low!)

// Instances of MonadError
import scala.util.Try
import cats.instances.try_._ // for MonadError

val exn: Throwable = new RuntimeException("It's all gone wrong")

exn.raiseError[Try, Int]
// val res5: scala.util.Try[Int] = Failure(java.lang.RuntimeException: It's all gone wrong)

// Exercise:
def validateAdult[F[_]](age: Int)(implicit
    me: MonadError[F, Throwable]
): F[Int] = {
  val exn: Throwable = new IllegalArgumentException(
    "Age must be greater than or equal to 18"
  )
  me.ensure(age.pure[F])(exn)(_ >= 18)
}

validateAdult[Try](18)
// val res6: scala.util.Try[Int] = Success(18)
validateAdult[Try](8)
// val res7: scala.util.Try[Int] = Failure(java.lang.IllegalArgumentException: Age must be greater than or equal to 18)
type ExceptionOr[A] = Either[Throwable, A]
validateAdult[ExceptionOr](-1)
// val res8: ExceptionOr[Int] = Left(java.lang.IllegalArgumentException: Age must be greater than or equal to 18)

// Book solution
def validateAdult[F[_]](
    age: Int
)(implicit me: MonadError[F, Throwable]): F[Int] =
  if (age >= 18) age.pure[F]
  else
    new IllegalArgumentException("Age must be greater than or equal to 18")
      .raiseError[F, Int]
