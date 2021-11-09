// We query the number of available CPUs on our machine:
Runtime.getRuntime.availableProcessors()
// val res0: Int = 8

// We can partition a sequence using grouped
(1 to 10).toList.grouped(3).toList
// val res1: List[List[Int]] = List(
//   List(1, 2, 3),
//   List(4, 5, 6),
//   List(7, 8, 9),
//   List(10)
// )

// Exercise: parallelFoldMap
import cats.Monoid
import cats.Traverse
import cats.instances.int._ // for Monoid
import cats.instances.future._ // for Applicative and Monad
import cats.instances.vector._ // for Foldable and Traverse
import cats.syntax.semigroup._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

// My solution
def parallelFoldMap[A, B: Monoid](va: Vector[A])(f: A => B): Future[B] = {

  val threads = Runtime.getRuntime.availableProcessors()
  val elementsPerThread = (va.size.toDouble / threads).ceil.toInt
  val partitions = va.grouped(elementsPerThread).toVector

  Traverse[Vector]
    .traverse(partitions) { p =>
      Future(p.foldLeft(Monoid[B].empty)(_ |+| f(_)))
    }
    .map(_.fold(Monoid.empty[B])(_ |+| _))
}

val myResult: Future[Int] = parallelFoldMap((1 to 1000000).toVector)(identity)

// Book solution
def parallelFoldMap[A, B: Monoid](
    values: Vector[A]
)(func: A => B): Future[B] = {
  // Calculate the number of items to pass to each CPU:
  val numCores = Runtime.getRuntime.availableProcessors()
  val groupSize = (1.0 * values.size / numCores).ceil.toInt

  // Create one group for each CPU:
  val groups: Iterator[Vector[A]] = values.grouped(groupSize)

  // Create a future to foldMap each group:
  val futures: Iterator[Future[B]] =
    groups map { group =>
      Future {
        group.foldLeft(Monoid[B].empty)(_ |+| func(_))
      }
    }

  // foldMap over the groups to calculate a final result:
  Future.sequence(futures) map { iterable =>
    iterable.foldLeft(Monoid[B].empty)(_ |+| _)
  }
}

val result: Future[Int] = parallelFoldMap((1 to 1000000).toVector)(identity)

Await.result(myResult, 1.second)
// val res2: Int = 1784293664

Await.result(result, 1.second)
// val res3: Int = 1784293664

// Book solution using Cats
import cats.syntax.foldable._ // for combineAll and foldMap
import cats.syntax.traverse._ // for traverse

def parallelFoldMap[A, B: Monoid](values: Vector[A])(func: A => B): Future[B] = {
  val numCores = Runtime.getRuntime.availableProcessors()
  val groupSize = (1.0 * values.size / numCores).ceil.toInt

  values
    .grouped(groupSize)
    .toVector
    .traverse(group => Future(group.foldMap(func)))
    .map(_.combineAll)
}

val future: Future[Int] = parallelFoldMap((1 to 1000).toVector)(_ * 1000)

Await.result(future, 1.second)
// val res4: Int = 500500000