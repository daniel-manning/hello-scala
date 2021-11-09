import cats.Eq
import cats.syntax.eq._ // for ===

import cats.instances.int._ // for Eq
import cats.instances.string._ // for Eq

import cats.scalawithcats.part1.introduction.equality.exercise.Cat

// Define a cat:
val cat1 = Cat("Garfield", 38, "orange and black")
val cat2 = Cat("Heathcliff", 33, "orange and black")

val optionCat1 = Option(cat1)
val optionCat2 = Option.empty[Cat]

implicit val catEq: Eq[Cat] =
  Eq.instance[Cat] { (cat1, cat2) =>
    (cat1.name === cat2.name) &&
    (cat1.age === cat2.age) &&
    (cat1.color === cat2.color)
  }

// Compare the cats!
cat1 === cat2
cat1 =!= cat2
// val res0: Boolean = false
// val res1: Boolean = true

// Compare the options!
import cats.instances.option._ // for Eq
optionCat1 === optionCat2
optionCat1 =!= optionCat2
// val res2: Boolean = false
// val res3: Boolean = true