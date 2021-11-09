import cats.scalawithcats.part1.introduction.exercise.{Cat, Printable}
import cats.scalawithcats.part1.introduction.exercise.PrintableInstances._
import cats.scalawithcats.part1.introduction.exercise.PrintableSyntax._

// Define a cat:
val cat = Cat("Wiwi", 3, "brown")

// Print the cat!
Printable.print(cat)

// Print the cat with syntax!
cat.print