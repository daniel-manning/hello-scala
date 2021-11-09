import cats.Semigroupal
import cats.instances.option._ // for semigroupal

Semigroupal[Option].product(Some(123), Some("abc"))
// val res0: Option[(Int, String)] = Some((123,abc))

// What happens if one of the parameters is None?
Semigroupal[Option].product(None, Some("abc"))
// val res1: Option[(Nothing, String)] = None
Semigroupal[Option].product(Some(123), None)
// val res2: Option[(Int, Nothing)] = None

// Many arities in the Semigroupal companion object (up to 22)
Semigroupal.tuple3(Option(1), Option(2), Option(3))
// val res3: Option[(Int, Int, Int)] = Some((1,2,3))
Semigroupal.tuple3(Option(1), Option(2), Option.empty[Int])
// val res4: Option[(Int, Int, Int)] = None

// Same thing for map 2-22
Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _)
// val res5: Option[Int] = Some(6)
Semigroupal.map2(Option(1), Option.empty[Int])(_ + _)
// val res6: Option[Int] = None

// There are also contramaps and imaps for Contravariant and Invariant instances

// Apply Syntax
import cats.syntax.apply._ // for tupled and mapN

// tupled:
(Option(123), Option("abc")).tupled
// val res7: Option[(Int, String)] = Some((123,abc))
// up to 22 values of arity:
(Option(123), Option("abc"), Option(true)).tupled
// val res8: Option[(Int, String, Boolean)] = Some((123,abc,true))

// mapN with an implicit functor and a function of the correct arity:
final case class Cat(name: String, born: Int, color: String)

(
  Option("Garfield"),
  Option(1978),
  Option("Orange & black")
).mapN(Cat.apply)
// val res9: Option[Cat] = Some(Cat(Garfield,1978,Orange & black))

// type checked:
val add: (Int, Int) => Int = (a, b) => a + b

// (Option(1), Option(2), Option(3)).mapN(add)
// won't compile

// (Option("cats"), Option(true)).mapN(add)
// won't compile

