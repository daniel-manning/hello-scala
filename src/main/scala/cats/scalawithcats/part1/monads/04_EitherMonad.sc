import cats.syntax.either._  // for asRight

val a = 3.asRight[String]
// val a: Either[String,Int] = Right(3)
val b = 4.asRight[String]
// val b: Either[String,Int] = Right(4)

for {
  x <- a
  y <- b
} yield x*x + y*y
// val res0: scala.util.Either[String,Int] = Right(25)

// asRight avoids inference problems because:
// 1) it has a return type of Either
// 2) allows us to specify the other type with a type parameter

def countPositive(nums: List[Int]) =
  nums.foldLeft(0.asRight[String]) { (acc, e) =>
    if(e > 0) acc.map(_ + 1)
    else Left("Negative. Stopping!")
  }

countPositive(List(1, 2, 3))
// val res1: Either[String,Int] = Right(3)
countPositive(List(1, -2, 3))
// val res2: Either[String,Int] = Left(Negative. Stopping!)

// Methods for capturing Exceptions as instances of Either:
Either.catchOnly[NumberFormatException]("foo".toInt)
// val res3: Either[NumberFormatException,Int] = Left(java.lang.NumberFormatException: For input string: "foo")
Either.catchNonFatal(sys.error("Badness"))
// val res4: Either[Throwable,Nothing] = Left(java.lang.RuntimeException: Badness)

// Methods for creating an Either from other data types:
Either.fromTry(scala.util.Try("foo".toInt))
// val res6: Either[Throwable,Int] = Left(java.lang.NumberFormatException: For input string: "foo")
Either.fromOption[String, Int](None, "Badness")
// val res7: Either[String,Int] = Left(Badness)

// get methods
"Error".asLeft[Int].getOrElse(0)
// val res8: Int = 0
"Error".asLeft[Int].orElse(2.asRight[String])
// val res9: scala.util.Either[String,Int] = Right(2)

// The ensure method allows us to check whether Right satisfies a predicate:
(-1).asRight[String].ensure("Must be non-negative!")(_ > 0)
// val res9: Either[String,Int] = Left(Must be non-negative!)

// recover and recoverWith for error handling
"error".asLeft[Int].recover {
  case _: String => -1
}
// val res10: Either[String,Int] = Right(-1)
"error".asLeft[Int].recoverWith {
  case _: String => Right(-1)
}
// val res11: Either[String,Int] = Right(-1)

// leftMap and bimap methods complement map:
"foo".asLeft[Int].leftMap(_.reverse)
// val res12: Either[String,Int] = Left(oof)
6.asRight[String].bimap(_.reverse, _ * 7)
// val res13: Either[String,Int] = Right(42)
"bar".asLeft[String].bimap(_.reverse, _ * 7)
// val res14: Either[String,String] = Left(rab)

// swap
123.asRight[String]
// val res15: Either[String,Int] = Right(123)
123.asRight[String].swap
// val res16: Either[Int,String] = Left(123)