import cats.Monoid
import cats.instances.int._ // for Monoid
import cats.instances.invariant._ // for Semigroupal
import cats.instances.list._ // for Monoid
import cats.instances.string._ // for Monoid
import cats.syntax.apply._ // for imapN

final case class Cat(
    name: String,
    yearOfBirth: Int,
    favoriteFoods: List[String]
)

val tupleToCat: (String, Int, List[String]) => Cat =
  Cat.apply

val catToTuple: Cat => (String, Int, List[String]) =
  cat => (cat.name, cat.yearOfBirth, cat.favoriteFoods)

// Woah!!
implicit val catMonoid: Monoid[Cat] = (
  Monoid[String],
  Monoid[Int],
  Monoid[List[String]]
).imapN(tupleToCat)(catToTuple)

// Now we can use the |+| operator!
import cats.syntax.semigroup._

val garfield = Cat("Garfield", 1978, List("Lasagne"))
val heathcliff = Cat("Heathcliff", 1988, List("Junk Food"))

garfield |+| heathcliff
// val res0: Cat = Cat(GarfieldHeathcliff,3966,List(Lasagne, Junk Food))