import cats.instances.int._
import cats.instances.map._
import cats.scalawithcats.part2.crdts.GCounter
import cats.scalawithcats.part2.crdts.GCounter._

val g1 = Map("a" -> 7, "b" -> 3)
val g2 = Map("a" -> 2, "b" -> 5)

val counter = GCounter[Map, String, Int]

val merged = counter.merge(g1, g2)
// val merged: Map[String,Int] = Map(a -> 9, b -> 8)
val total = counter.total(merged)
// val total: Int = 17