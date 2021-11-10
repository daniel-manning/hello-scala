package cats.scalawithcats.part2.crdts

import cats.instances.list._ // for Monoid
import cats.instances.map._ // for Monoid
import cats.kernel.CommutativeMonoid
import cats.syntax.foldable._ // for combineAll
import cats.syntax.semigroup._ // for |+|

final case class GCounter[A](counters: Map[String, A]) {

  def increment(machine: String, a: A)(implicit
      m: CommutativeMonoid[A]
  ): GCounter[A] = {
    val value: A = a |+| counters.getOrElse(machine, m.empty)
    GCounter(counters + (machine -> value))
  }

  def merge(that: GCounter[A])(implicit b: BoundedSemiLattice[A]): GCounter[A] =
    GCounter(this.counters |+| that.counters)

  def total(implicit m: CommutativeMonoid[A]): A =
    counters.values.toList.combineAll
}
