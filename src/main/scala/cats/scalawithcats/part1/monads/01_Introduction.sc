import scala.util.Try

// A monad is a mechanism for sequencing computations.
// It takes care of the complication allowing us to flatMap again and again.
// For example, Option allows us to sequence computations that may not return values.

def parseInt(string: String): Option[Int] =
  Try(string.toInt).toOption

def divide(a: Int, b: Int): Option[Int] =
  if(b == 0) None else Some(a / b)//ç

// These methods may fail by returning None.
// We can ignore this with flatMap.

def stringDivideBy(aString: String, bString: String): Option[Int] =
  parseInt(aString).flatMap{ aNum =>
    parseInt(bString).flatMap { bNum =>
      divide(aNum, bNum)
    }
  }

// Can fail on any step
stringDivideBy("6", "2")
// val res0: Option[Int] = Some(3)
stringDivideBy("6", "0")
// val res1: Option[Int] = None
stringDivideBy("6", "foo")
// val res2: Option[Int] = None
stringDivideBy("bar", "2")
// val res3: Option[Int] = None

// Every monad is also a functor. We can define map by using flatMap and pure
trait Monad[F[_]] {
  def pure[A](a: A): F[A]

  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

  def map[A, B](value: F[A])(func: A => B): F[B] = flatMap(value)(func andThen pure[B])

}