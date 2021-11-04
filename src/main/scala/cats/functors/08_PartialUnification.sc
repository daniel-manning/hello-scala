import cats.Functor
import cats.instances.function._ // for Functor
import cats.syntax.functor._ // for map

val func1 = (x: Int) => x.toDouble
val func2 = (y: Double) => y * 2

val func3 = func1.map(func2)

// Function has 2 type parameters but Functor only one
// The compiler fixes the first one (Int) and passes the second one

// This works in most scenarios:
val either: Either[String, Int] = Right(123)
// val either: Either[String,Int] = Right(123)
either.map(_ + 1)
// val res0: scala.util.Either[String,Int] = Right(124)

// But it is not always the correct choice
// An example of this is the Contravariant functor for Function1

// These three implementations are equivalent:
val func3a: Int => Double =
  a => func2(func1(a))

val func3b: Int => Double =
  func2.compose(func1)

// Hypothetical example. Doesn't compile
import cats.syntax.contravariant._ // for contramap
// val func3c: Int => Double =
//  func2.contramap(func1)
// value contramap is not a member of Double => Double

// Contravariant for Function1 is fixing the return type
// and leaving the parameter type varying.
// This requires the compiler to eliminate type parameters from right to left
// But it fails because of its left to right bias.
type F[A] = A => Double

// We can prove this by creating a type alias that flips the parameters on Function1
type <=[B, A] = A => B
type F[A] = Double <= A

// We reset the required order of elimination:
val func2b: Double <= Double = func2
val func3c = func2b.contramap(func1)

// It works! The difference is merely syntactic :O