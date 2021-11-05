import cats.Id
import cats.data.{Writer, WriterT}
import cats.instances.vector._ // for Monoid

Writer(
  Vector(
    "It was the best of times",
    "it was the worst of times"
  ),
  1859
)
// val res0: cats.data.WriterT[cats.Id, Vector[String],Int] =
//  WriterT((Vector(It was the best of times, it was the worst of times),1859))

// We can create Writers specifying only the log or the result
// For results we can use the pure syntax
// We must have a Monoid[W] in scope so Cats can produce an empty log
import cats.syntax.applicative._ // for pure
type Logged[A] = Writer[Vector[String], A]

123.pure[Logged]
// val res1: Logged[Int] = WriterT((Vector(),123))

// For logs without results we can create a Writer[Unit]
import cats.syntax.writer._ // for tell and writer

Vector("msg1", "msg2", "msg3").tell
// val res2: cats.data.Writer[Vector[String],Unit] =
//  WriterT((Vector(msg1, msg2, msg3),()))

// For both
val a: WriterT[Id, Vector[String], Int] =
  Writer(Vector("msg1", "msg2", "msg3"), 123)
// val a: cats.data.WriterT[cats.Id,Vector[String],Int] =
//  WriterT((Vector(msg1, msg2, msg3),123))
val b: Writer[Vector[String], Int] =
  123.writer(Vector("msg1", "msg2", "msg3"))
// val b: cats.data.Writer[Vector[String],Int] =
// WriterT((Vector(msg1, msg2, msg3),123))

// Extracting result and log methods
val aResult = a.value
// val aResult: cats.Id[Int] = 123
val aLog = a.written
// val aLog: cats.Id[Vector[String]] = Vector(msg1, msg2, msg3)

// At the same time!
val (log, result) = b.run
// val log: Vector[String] = Vector(msg1, msg2, msg3)
//  val result: Int = 123

// We often use Vector for log type because of efficient append and concatenate
val writer1 = for {
  a <- 10.pure[Logged]
  _ <- Vector("a", "b", "c").tell
  b <- 32.writer(Vector("x", "y", "z"))
} yield a + b
// val writer1: cats.data.WriterT[cats.Id,Vector[String],Int] =
//  WriterT((Vector(a, b, c, x, y, z),42))

writer1.run
// val res3: cats.Id[(Vector[String], Int)] =
//  (Vector(a, b, c, x, y, z),42)

// Transforming the log:
val writer2 = writer1.mapWritten(_.map(_.toUpperCase()))
// val writer2: cats.data.WriterT[cats.Id,Vector[String],Int] =
//  WriterT((Vector(A, B, C, X, Y, Z),42))

writer2.run
// val res4: cats.Id[(Vector[String], Int)] =
// (Vector(A, B, C, X, Y, Z),42)

// Transforming both
val writer3 = writer1.bimap(
  log => log.map(_.toUpperCase),
  res => res * 100
)
// val writer3: cats.data.WriterT[cats.Id,Vector[String],Int] =
//  WriterT((Vector(A, B, C, X, Y, Z),4200))
writer3.run
// val res5: cats.Id[(Vector[String], Int)] =
//  (Vector(A, B, C, X, Y, Z),4200)

val writer4 = writer1.mapBoth { (log, res) =>
  val log2 = log.map(_ + "!")
  val res2 = res * 1000
  (log2, res2)
}
// val writer4: cats.data.WriterT[cats.Id,Vector[String],Int] =
//  WriterT((Vector(a!, b!, c!, x!, y!, z!),42000))
writer4.run
// val res6: cats.Id[(Vector[String], Int)] =
//  (Vector(a!, b!, c!, x!, y!, z!),42000)

// Clear the log with reset
val writer5 = writer1.reset
// val writer5: cats.data.WriterT[cats.Id,Vector[String],Int] = WriterT((Vector(),42))
writer5.run
// val res7: cats.Id[(Vector[String], Int)] = (Vector(),42)

// Swap positions with swap
val writer6 = writer1.swap
// val writer6: cats.data.WriterT[cats.Id,Int,Vector[String]] =
//  WriterT((42,Vector(a, b, c, x, y, z)))
writer6.run
// val res8: cats.Id[(Int, Vector[String])] =
//  (42,Vector(a, b, c, x, y, z))

// Exercise 4.7.3
def slowly[A](body: => A) =
  try body
  finally Thread.sleep(100)

/**
def factorial(n: Int): Int = {
  val ans = slowly(if (n == 0) 1 else n * factorial(n - 1))
  println(s"fact $n $ans")
  ans
}

If we started several calculations in parallel,
logs would interleave.
Rewrite factorial using the Writer monad.
*/

def factorial(n : Int): Logged[Int] =
  for {
    ans <- if(n == 0) {
      1.pure[Logged]
    } else {
      slowly(factorial(n - 1).map(_ * n): Logged[Int])
    }
    _ <- Vector(s"fact $n $ans").tell
  } yield ans

val (log, res) = factorial(5).run
