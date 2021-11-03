import cats.instances.int._
import cats.instances.option._
import cats.monoidsandsemigroups.SuperAdder

SuperAdder.Part2.add(List(1, 2, 3))
// val res0: Int = 6

SuperAdder.Part2.add(List(Some(1), None, Some(2), None, Some(3)))
// val res1: Option[Int] = Some(6)