import cats.Monoid
import cats.syntax.semigroup._

def foldMap[A, B: Monoid](va: Vector[A])(f: A => B): B =
  va.foldLeft(Monoid[B].empty)(_ |+| f(_))

import cats.instances.int._ // for Monoid

foldMap(Vector(1, 2, 3))(identity)
// val res0: Int = 6

import cats.instances.string._ // for Monoid

// Mapping to a String uses the concatenation monoid:
foldMap(Vector(1, 2, 3))(_.toString + "! ")
// val res1: String = "1! 2! 3! "

foldMap("Hello world!".toVector)(_.toString.toUpperCase)
// val res2: String = HELLO WORLD!

