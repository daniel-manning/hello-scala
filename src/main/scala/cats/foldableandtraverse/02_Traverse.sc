// Scala standard library example
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

val hostnames = List(
  "alpha.example.com",
  "beta.example.com",
  "gamma.demo.com"
)

def getUptime(hostname: String): Future[Int] =
  Future(hostname.length * 60) // just for demonstration

// If we want all uptimes we need to reduce to a single future:
val allUptimes: Future[List[Int]] =
  hostnames.foldLeft(Future(List.empty[Int])) { (acc, host) =>
    val uptime = getUptime(host)
    for {
      acc <- acc
      uptime <- uptime
    } yield acc :+ uptime
  }

Await.result(allUptimes, 1.second)
// val res0: List[Int] = List(1020, 960, 840)

// This is the idea behind Future.traverse
val allUptimes: Future[List[Int]] =
  Future.traverse(hostnames)(getUptime)

Await.result(allUptimes, 1.second)
// val res1: List[Int] = List(1020, 960, 840)

// Now with Applicatives!
import cats.Applicative
import cats.instances.future._ // for Applicative
import cats.syntax.applicative._ // for pure

List.empty[Int].pure[Future]

// And now our combinator is equivalent to Semigroupal.combine:
import cats.syntax.apply._ // for mapN

// Combining accumulator and hostname using an Applicative:
def newCombine(acc: Future[List[Int]], host: String): Future[List[Int]] =
  (acc, getUptime(host)).mapN(_ :+ _)

// By substituting these snippets back into the definition of traverse
// we can generalise it to work with any Applicative:
def listTraverse[F[_]: Applicative, A, B](
    list: List[A]
)(f: A => F[B]): F[List[B]] =
  list.foldLeft(List.empty[B].pure[F]) { (acc, e) =>
    (acc, f(e)).mapN(_ :+ _)
  }

def listSequence[F[_]: Applicative, B](list: List[F[B]]): F[List[B]] =
  listTraverse(list)(identity)

val totalUptime = listTraverse(hostnames)(getUptime)

Await.result(totalUptime, 1.second)
// val res3: List[Int] = List(1020, 960, 840)

// Exercise
import cats.instances.vector._ // for Applicative

listSequence(List(Vector(1, 2), Vector(3, 4)))
// val res4: Vector[List[Int]] = Vector(List(1, 3), List(1, 4), List(2, 3), List(2, 4))

listSequence(List(Vector(1, 2), Vector(3, 4), Vector(5, 6)))
// val res5: Vector[List[Int]] = Vector(List(1, 3, 5), List(1, 3, 6), List(1, 4, 5), List(1, 4, 6), List(2, 3, 5), List(2, 3, 6), List(2, 4, 5), List(2, 4, 6))

// Exercise
import cats.instances.option._ // for Applicative

def process(inputs: List[Int]): Option[List[Int]] =
  listTraverse(inputs)(n => if(n % 2 == 0) Some(n) else None)

process(List(2, 4, 6))
// val res6: Option[List[Int]] = Some(List(2, 4, 6))
process(List(1, 2, 3))
// val res7: Option[List[Int]] = None

// Exercise
import cats.data.Validated
import cats.instances.list._ // for Monoid

type ErrorsOr[A] = Validated[List[String], A]

def process(inputs: List[Int]): ErrorsOr[List[Int]] =
  listTraverse(inputs) { n =>
    if(n % 2 == 0) {
      Validated.valid(n)
    } else {
      Validated.invalid(List(s"$n is not even"))
    }
  }

process(List(2, 4, 6))
// val res8: ErrorsOr[List[Int]] = Valid(List(2, 4, 6))
process(List(1, 2, 3))
// val res9: ErrorsOr[List[Int]] = Invalid(List(1 is not even, 3 is not even))

// Cats Traverse
import cats.Traverse
import cats.instances.future._ // for Applicative
import cats.instances.list // for Traverse

val totalUptime: Future[List[Int]] =
  Traverse[List].traverse(hostnames)(getUptime)

Await.result(totalUptime, 1.second)
// val res10: List[Int] = List(1020, 960, 840)

val numbers = List(Future(1), Future(2), Future(3))

val numbers2 = Traverse[List].sequence(numbers)

Await.result(numbers2, 1.second)
// val res11: List[Int] = List(1, 2, 3)

// Traverse syntax
import cats.syntax.traverse._ // for sequence and traverse

Await.result(hostnames.traverse(getUptime), 1.second)
// val res12: List[Int] = List(1020, 960, 840)
Await.result(numbers.sequence, 1.second)
// val res13: List[Int] = List(1, 2, 3)

