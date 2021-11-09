// Examples
import cats.Monad
import cats.instances.option._ // for Monad
import cats.instances.list._ // for Monad

val opt1 = Monad[Option].pure(3)
// val opt1: Option[Int] = Some(3)
val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
// val opt2: Option[Int] = Some(5)
val opt3 = Monad[Option].map(opt2)(a => 100 * a)
// val opt3: Option[Int] = Some(500)

val list1 = Monad[List].pure(3)
// val list1: List[Int] = List(3)
val list2 = Monad[List].flatMap(List(1, 2, 3))(a => List(a, a * 10))
// val list2: List[Int] = List(1, 10, 2, 20, 3, 30)
val list3 = Monad[List].map(list2)(a => a + 123)

// Default instances
import cats.instances.option._ // for Monad
Monad[Option].flatMap(Option(1))(a => Option(a * 2))
// val res0: Option[Int] = Some(2)

import cats.instances.list._ // for Monad
Monad[List].flatMap(List(1, 2, 3))(a => List(a, a * 10))
// val res1: List[Int] = List(1, 10, 2, 20, 3, 30)

import cats.instances.vector._ // for Monad
Monad[Vector].flatMap(Vector(1, 2, 3))(a => Vector(a, a * 10))
// val res2: Vector[Int] = Vector(1, 10, 2, 20, 3, 30)

// Monad syntax
import cats.syntax.applicative._
1.pure[Option]
// val res3: Option[Int] = Some(1)
1.pure[List]
// val res4: List[Int] = List(1)

// Generic .flatMap and .map invocation
import cats.syntax.functor._
import cats.syntax.flatMap._

def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
  a.flatMap(x => b.map(y => x*x + y*y))

sumSquare(Option(3), Option(4))
// val res6: Option[Int] = Some(25)
sumSquare(List(1, 2, 3), List(4, 5))
// val res7: List[Int] = List(17, 26, 20, 29, 25, 34)

// Now with for comprehensions
def sumSquareFor[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
  for {
    x <- a
    y <- b
  } yield x*x + y*y

sumSquareFor(Option(3), Option(4))
// val res7: Option[Int] = Some(25)
sumSquareFor(List(1, 2, 3), List(4, 5))
// val res8: List[Int] = List(17, 26, 20, 29, 25, 34)