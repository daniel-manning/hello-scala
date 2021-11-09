import cats.Monoid
import cats.instances.string._ // for Monoid

Monoid[String].combine("Hi ", "there")
// val res0: String = Hi there

Monoid[String].empty
// val res1: String = ""

// This is equivalent to:
Monoid.apply[String].combine("Hi ", "there")
// val res2: String = Hi there
Monoid.apply[String].empty
// val res3: String = ""

// If we don't need empty, we can
import cats.Semigroup

Semigroup[String].combine("Hi ", "there")
// val res4: String = Hi there

// Example for Int
import cats.instances.int._ // for Monoid

Monoid[Int].combine(32, 10)
// val res5: Int = 42

// Example for Option[Int]
import cats.instances.option._ // for Monoid
val a = Option(22)
// val a: Option[Int] = Some(22)i
val b = Option(20)
// val b: Option[Int] = Some(20)

Monoid[Option[Int]].combine(a, b)
// val res6: Option[Int] = Some(42)

// Monoid Syntax
import cats.syntax.semigroup._ // for |+|

val stringResult = "Hi " |+| "there" |+| Monoid[String].empty
// val stringResult: String = Hi there

val intResult = 1 |+| 2 |+| Monoid[Int].empty
// val intResult: Int = 3

// Conclusion
val swc = "Scala" |+| " with " |+| "Cats"
// val res7: String = Scala with Cats

val o = Option(1) |+| Option(2)
// val o: Option[Int] = Some(3)

import cats.instances.map._ // for Monoid
val map1 = Map("a" -> 1, "b" -> 2)
val map2 = Map("b" -> 3, "d" -> 4)

val map3 = map1 |+| map2
// val map3: scala.collection.immutable.Map[String,Int] = Map(b -> 5, d -> 4, a -> 1)

import cats.instances.tuple._ // for Monoid

val tuple1 = ("hello", 123)
val tuple2 = ("world", 321)

val tuple3 = tuple1 |+| tuple2
// val tuple3: (String, Int) = (helloworld,444)

// Generic addition
def addAll[A](values: List[A])(implicit monoid: Monoid[A]): A =
  values.foldRight(monoid.empty)(_ |+| _)

val addAll1 = addAll(List(1, 2, 3))
// val addAll1: Int = 6
val addAll2 = addAll(List(None, Some(1), Some(2)))
// val addAll2: Option[Int] = Some(3)
