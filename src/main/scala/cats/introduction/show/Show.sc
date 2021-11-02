import cats.Show

// val showInt = Show.apply[Int]
// could not find implicit value for parameter instance: cats.Show[Int]
// val showInt = Show.apply[Int]

import cats.instances.int._
import cats.instances.string._

val showInt: Show[Int] = Show.apply[Int]
val showString: Show[String] = Show.apply[String]

val intAsString: String = showInt.show(123)
// val intAsString: String = 123

val stringAsString: String = showString.show("abc")
// val stringAsString: String = abc

import cats.syntax.show._ // for show

val shownInt = 123.show
// val shownInt: String = 123

val shownString = "abc".show
// val shownString: String = abc

import java.util.Date

implicit val dateShow: Show[Date] = new Show[Date] {
  def show(date: Date): String =
    s"${date.getTime}ms since the epoch."
}

new Date().show
// val res0: String = 1635859903597ms since the epoch.

implicit val dateShow: Show[Date] = Show.show(date => s"${date.getTime}ms since the epoch.")
new Date().show
// val res1: String = 1635860894131ms since the epoch.