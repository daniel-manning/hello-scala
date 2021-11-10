import cats.data.{NonEmptyList, Validated}
import cats.scalawithcats.part2.datavalidation.{Check, Predicate}

type Errors = NonEmptyList[String]

def error(s: String): NonEmptyList[String] = NonEmptyList(s, Nil)

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

import cats.syntax.apply._ // for mapN
import cats.syntax.validated._ // for valid and invalid

// A username must contain at least four characters
// and consist entirely of alphanumeric characters

val checkUsername: Check[Errors, String, String] =
  Check(longerThan(3) and alphanumeric)

// An email address must contain a single `@` sign.
// Split the string at the `@`.
// The string to the left must not be empty.
// The string to the right must be
// at least three characters long and contain a dot.
val splitEmail: Check[Errors, String, (String, String)] =
  Check(_.split('@') match {
    case Array(name, domain) => (name, domain).validNel[String]
    case _                   => "Must contain a single @ character".invalidNel[(String, String)]
  })

val checkLeft: Check[Errors, String, String] =
  Check(longerThan(0))

val checkRight: Check[Errors, String, String] =
  Check(longerThan(3) and contains('.'))

val joinEmail: Check[Errors, (String, String), String] =
  Check { case(l, r) =>
    (checkLeft(l), checkRight(r)).mapN(_ + "@" + _)
  }

val checkEmail: Check[Errors, String, String] =
  splitEmail andThen joinEmail

  // Final test
final case class User(username: String, email: String)

def createUser(username: String, email: String): Validated[Errors, User] =
  (checkUsername(username), checkEmail(email)).mapN(User)

createUser("Noel", "noel@underscore.io")
// val res0: cats.data.Validated[Errors,User] =
//  Valid(User(Noel,noel@underscore.io))
createUser("", "dave@underscore.io@io")
// val res1: cats.data.Validated[Errors,User] =
//   Invalid(NonEmptyList(
//     Must be longer than 3 characters,
//     Must contain a single @ character
// ))