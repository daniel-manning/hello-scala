package cats.monoidsandsemigroups

import cats.Monoid
import cats.Semigroup
import cats.instances.int._ // for Monoid and Semigroup
import cats.syntax.semigroup._ // for |+|

object SuperAdder {

  object Part1 {

    // My solution
    def add(items: List[Int]): Int =
      Semigroup[Int].combineAllOption(items).getOrElse(0)

    // Native scala solution
    def add_ns(items: List[Int]): Int =
      items.sum // alternatively: .foldLeft(0)(_ + _)

    // Cats solution
    def add_cats(items: List[Int]): Int =
      items.foldLeft(Monoid[Int].empty)(_ |+| _)

    // My revised solution
    def add_revised(items: List[Int]): Int = Monoid[Int].combineAll(items)

  }

  object Part2 {

    // My solution (turns out this was not what was asked)
    import cats.instances.option._
    def add_mine(items: List[Option[Int]]): Option[Int] =
      Monoid[Option[Int]].combineAll(items)

    // Cats solution
    def add_cats[A](items: List[A])(implicit monoid: Monoid[A]): A =
      items.foldLeft(monoid.empty)(_ |+| _)

    // Context bound syntax solution
    def add[A: Monoid](items: List[A]): A =
      items.foldLeft(Monoid[A].empty)(_ |+| _)
  }

}
