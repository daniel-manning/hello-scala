package cats.scalawithcats.part1.monoidsandsemigroups

import cats.Monoid
import cats.instances.double._ // for Monoid
import cats.syntax.monoid._ // for |+|

case class Order(totalCost: Double, quantity: Double)

object Order {

  implicit val orderMonoid: Monoid[Order] = new Monoid[Order] {

    def empty: Order = Order(0, 0)

    override def combine(x: Order, y: Order): Order = {
      val totalCost = x.totalCost |+| y.totalCost
      val quantity = x.quantity |+| y.quantity
      Order(totalCost, quantity)
    }

  }
}
