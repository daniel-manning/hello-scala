// Let's implement Option as a Monad
import cats.Monad
import scala.annotation.tailrec

val optionMonad = new Monad[Option] {
  def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
    fa flatMap f

  def pure[A](x: A): Option[A] = Some(x)

  // optimisation used in Cats to limit the amount of
  // stack space consumed by nested calls to flatMap
  @tailrec
  def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] =
    f(a) match {
      case None           => None
      case Some(Left(a1)) => tailRecM(a1)(f)
      case Some(Right(b)) => Some(b)
    }
}

// To understand tailRecM
// We want to write a method that calls a function until the function indicates it should stop.
import cats.syntax.flatMap._ // For flatMap

def retry[F[_]: Monad, A](start: A)(f: A => F[A]): F[A] =
  f(start).flatMap { a => retry(a)(f) }

// We can see that it is not stack-safe. It works for small input:
import cats.instances.option._

retry(100)(a => if (a == 0) None else Some(a - 1))
// val res1: Option[Int] = None

// But for large input..
// retry(100000)(a => if(a == 0) None else Some(a - 1))
// java.lang.StackOverflowError[...]

// Now let's try with tailRecM
import cats.syntax.functor._ // for map
def retryTailRecM[F[_]: Monad, A](start: A)(f: A => F[A]): F[A] =
  Monad[F].tailRecM(start) { a =>
    f(a).map(a2 => Left(a2))
  }

// Now it runs successfully no matter how many times we recurse
retryTailRecM(100000)(a => if (a == 0) None else Some(a - 1))
// val res4: Option[Int] = None

// We have to EXPLICITLY call tailRecM
// There are several utilities in the Monad type class
// For example, iterateWhileM
import cats.syntax.monad._ // for iterateWhileM

def retryM[F[_]: Monad, A](start: A)(f: A => F[A]): F[A] =
  start.iterateWhileM(f)(a => true)

retryM(100000)(a => if (a == 0) None else Some(a - 1))
// val res5: Option[Int] = None
