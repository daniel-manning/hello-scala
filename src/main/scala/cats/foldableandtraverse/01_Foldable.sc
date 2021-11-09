// Exercise
val ints = List(1, 2, 3)
ints.foldLeft[List[Int]](Nil)(_.::(_))
// val res0: List[Int] = List(3, 2, 1)
ints.foldRight[List[Int]](Nil)(_ :: _)
// val res1: List[Int] = List(1, 2, 3)

import cats.Foldable
import cats.instances.list._ // for Foldable

Foldable[List].foldLeft(ints, 0)(_ + _)
// val res2: Int = 6

import cats.instances.option._ // for Foldable

val maybeInt = Option(123)

Foldable[Option].foldLeft(maybeInt, 10)(_ * _)
// val res3: Int = 1230

// foldRight is implemented using the Eval monad so it is stack safe
import cats.Eval
import cats.Foldable

def bigData = (1 to 100000).to(LazyList)

bigData.foldRight(0L)(_ + _)
// If the stack is small this will give StackOverflowError

// Using Foldable forces us to use stack safe operations:
import cats.instances.lazyList._ // for Foldable

val eval: Eval[Long] =
  Foldable[LazyList].foldRight(bigData, Eval.now(0L)) {
    (num, eval) => eval.map(_ + num)
  }

eval.value
// val res5: Long = 5000050000

// Foldable provides similar methods to the standard library
Foldable[Option].nonEmpty(Option(42))
// val res6: Boolean = true
Foldable[List].find(List(1, 2, 3))(_ % 2 == 0)
// val res7: Option[Int] = Some(2)

// combineAll (fold) combines all elements using a Monoid
import cats.instances.int._ // for Monoid

Foldable[List].combineAll(List(1, 2, 3))
// val res8: Int = 6

// foldMap converts then applies Monoid
import cats.instances.string._ // for Monoid
Foldable[List].foldMap(List(1, 2, 3))(_.toString)

// compose supports deep traversal of nested sequences
import cats.instances.vector._ // for Monoid

val nested = List(Vector(1, 2, 3), Vector(4, 5, 6))

(Foldable[List] compose Foldable[Vector]).combineAll(nested)
// val res10: Int = 21

// syntax for foldable
import cats.syntax.foldable._ // for combineAll and foldMap

List(1, 2, 3).combineAll
// val res11: Int = 6
List(1, 2, 3).foldMap(_.toString)
// val res12: String = 123

