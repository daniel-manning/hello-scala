import cats.Eq
import cats.instances.int._ // for Eq

val eqInt = Eq[Int]

eqInt.eqv(123, 123)
// val res0: Boolean = true
eqInt.eqv(123, 321)
// val res1: Boolean = false

// eqInt.eqv(123, "234")
// type mismatch;
// found   : String("234")
// required: Int
// eqInt.eqv(123, "234")

import cats.syntax.eq._ // for === and =!=

123 === 123
// val res2: Boolean = true

123 =!= 321
// val res3: Boolean = true

// 123 === "123"
// type mismatch;
// found   : String("123")
// required: Int
// 123 === "123"

import cats.instances.option._ // for Eq

// Some(1) === None
// value === is not a member of Some[Int]
// Some(1) === None

// Some[Int] is not the same type as Option[Int]!

(Some(1): Option[Int]) === (None: Option[Int])
// val res4: Boolean = false

// We can use Option.apply and Option.empty methods
Option(1) === Option.empty[Int]
// val res5: Boolean = false

// Or import more syntax for option

import cats.syntax.option._ // for some and none

1.some === none[Int]
// val res6: Boolean = false
1.some =!= none[Int]
// val res7: Boolean = true

// Custom Types
import java.util.Date
import cats.instances.long._

implicit val dateEq: Eq[Date] =
  Eq.instance[Date] { (date1, date2) =>
    date1.getTime === date2.getTime
  }

val x = new Date() // now
val y = new Date() // a bit later than now

x === x
// val res8: Boolean = true
x === y
// val res9: Boolean = false