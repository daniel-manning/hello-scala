import cats.Monad
import cats.syntax.functor._ // for map
import cats.syntax.flatMap._ // for flatMap

def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
  for {
    x <- a
    y <- b
  } yield x*x + y*y

// We can't call it passing in plain values
// sumSquare(3, 4)
// no type parameters for method sumSquare: (a: F[Int], b: F[Int])(implicit evidence$1: cats.Monad[F]): F[Int] exist so that it can be applied to arguments (Int, Int)
// --- because ---
// argument expression's type is not compatible with formal parameter type;
// found   : 3
// required: ?F[Int]

// We can use the Identity monad!
import cats.Id

sumSquare(3: Id[Int], 4: Id[Int])
// val res1: cats.Id[Int] = 25
"Dave": Id[String]
// val res2: cats.Id[String] = Dave
123: Id[Int]
// val res3: cats.Id[Int] = 123
List(1, 2, 3): Id[List[Int]]
// val res4: cats.Id[List[Int]] = List(1, 2, 3)

val a = Monad[Id].pure(3)
// val a: cats.Id[Int] = 3
val b = Monad[Id].flatMap(a)(_ + 1)
// val b: cats.Id[Int] = 4

import cats.syntax.functor._ // for map
import cats.syntax.flatMap._ // for flatMap

for {
  x <- a
  y <- b
} yield x + y
// val res4: cats.Id[Int] = 7

// Exercise: implement pure, map and flatMap for Id
def pure[A](a: A): Id[A] = a
def map[A, B](ida: Id[A])(f: A => B): Id[B] = f(ida)
def flatMap[A, B](ida: Id[A])(f: A => Id[B]): Id[B] = f(ida)

pure(123)
// val res5: cats.Id[Int] = 123
map(123)(_ * 2)
// val res6: cats.Id[Int] = 246
flatMap(123)(_ * 2)
// val res7: cats.Id[Int] = 246

