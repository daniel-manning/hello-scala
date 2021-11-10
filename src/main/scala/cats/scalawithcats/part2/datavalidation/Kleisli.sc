import cats.data.Kleisli
import cats.implicits.catsSyntaxTuple2Semigroupal
import cats.instances.list._ // for Monad

val step1: Kleisli[List, Int, Int] =
  Kleisli(x => List(x + 1, x - 1))

val step2: Kleisli[List, Int, Int] =
  Kleisli(x => List(x, -x))

val step3: Kleisli[List, Int, Int] =
  Kleisli(x => List(x * 2, x / 2))

val pipeline = step1 andThen step2 andThen step3

pipeline.run(20)
// val res0: List[Int] = List(42, 10, -42, -10, 38, 9, -38, -9)

import cats.data.NonEmptyList
import cats.instances.either._ // for Semigroupal
import cats.scalawithcats.part2.datavalidation.Predicate

type Errors = NonEmptyList[String]

def error(s: String): NonEmptyList[String] =
  NonEmptyList(s, Nil)

type Result[A] = Either[Errors, A]

type Check[A, B] = Kleisli[Result, A, B]

def check[A, B](func: A => Result[B]): Check[A, B] =
  Kleisli(func)

def checkPred[A](pred: Predicate[Errors, A]): Check[A, A] =
  Kleisli[Result, A, A](pred.run)

// Base definitions:
def longerThan(n: Int): Predicate[Errors, String] =
  Predicate.lift(
    error(s"Must be longer than $n characters"),
    str => str.length > n
  )
val alphanumeric: Predicate[Errors, String] =
  Predicate.lift(
    error(s"Must be all alphanumeric characters"),
    str => str.forall(_.isLetterOrDigit)
  )
def contains(char: Char): Predicate[Errors, String] = Predicate.lift(
  error(s"Must contain the character $char"),
  str => str.contains(char)
)
def containsOnce(char: Char): Predicate[Errors, String] = Predicate.lift(
  error(s"Must contain the character $char only once"),
  str => str.count(c => c == char) == 1
)

val checkUsername: Check[String, String] =
  checkPred(longerThan(3) and alphanumeric)

val splitEmail: Check[String, (String, String)] =
  check(_.split('@') match {
    case Array(name, domain) => Right((name, domain))
    case _                   => Left(error("Must contain a single @ character"))
  })

val checkLeft: Check[String, String] =
  checkPred(longerThan(0))

val checkRight: Check[String, String] =
  checkPred(longerThan(3) and contains('.'))

val joinEmail: Check[(String, String), String] =
  check { case (l, r) =>
    (checkLeft(l), checkRight(r)).mapN(_ + "@" + _)
  }

val checkEmail: Check[String, String] =
  splitEmail andThen joinEmail

// Result
final case class User(username: String, email: String)
def createUser(username: String, email: String): Either[Errors, User] = (
  checkUsername.run(username),
  checkEmail.run(email)
).mapN(User)

createUser("Noel", "noel@underscore.io")
// val res1: Either[Errors,User] =
//   Right(User(Noel,noel@underscore.io))
createUser("", "dave@underscore.io@io")
// val res2: Either[Errors, User] = Left(
//   NonEmptyList(Must be longer than 3 characters)
// )
