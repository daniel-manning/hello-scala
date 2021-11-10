package cats.scalawithcats.part2.crdts

import cats.instances.list._ // for Monoid
import cats.instances.map._ // for Monoid
import cats.kernel.CommutativeMonoid
import cats.syntax.foldable._ // for combineAll
import cats.syntax.semigroup._ // for |+|

import cats.scalawithcats.part2.crdts.KeyValueStore.KvsOps

trait GCounter[F[_, _], K, V] {

  def increment(f: F[K, V])(k: K, v: V)(implicit
      m: CommutativeMonoid[V]
  ): F[K, V]

  def merge(f1: F[K, V], f2: F[K, V])(implicit
      b: BoundedSemiLattice[V]
  ): F[K, V]

  def total(f: F[K, V])(implicit m: CommutativeMonoid[V]): V
}

object GCounter {

  def apply[F[_, _], K, V](implicit
      counter: GCounter[F, K, V]
  ): GCounter[F, K, V] = counter

  implicit def gCounterInstance[F[_, _], K, V](implicit
      kvs: KeyValueStore[F],
      km: CommutativeMonoid[F[K, V]]
  ): GCounter[F, K, V] =
    new GCounter[F, K, V] {

      def increment(
          f: F[K, V]
      )(k: K, v: V)(implicit m: CommutativeMonoid[V]): F[K, V] = {
        val total = f.getOrElse(k, m.empty) |+| v
        f.put(k, total)
      }

      def merge(f1: F[K, V], f2: F[K, V])(implicit
          b: BoundedSemiLattice[V]
      ): F[K, V] = f1 |+| f2

      def total(f: F[K, V])(implicit m: CommutativeMonoid[V]): V =
        f.values.combineAll
    }
}
