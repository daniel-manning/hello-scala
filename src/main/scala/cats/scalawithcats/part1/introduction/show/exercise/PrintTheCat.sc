import cats.Show
import cats.instances.int._    // for Show
import cats.instances.string._ // for Show
import cats.syntax.show._      // for show
import cats.scalawithcats.part1.introduction.show.exercise.Cat

// Define a cat:
val cat = Cat("Wiwi", 3, "brown")

implicit val catShow = Show.show[Cat](cat => {
  val name = cat.name.show
  val age = cat.age.show
  val color = cat.color.show
  s"$name is a $age year-old $color cat."
})

// Show the cat with syntax!
cat.show
// val res0: String = Wiwi is a 3 year-old brown cat.