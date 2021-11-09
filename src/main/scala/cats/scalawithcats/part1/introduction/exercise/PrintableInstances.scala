package cats.scalawithcats.part1.introduction.exercise

object PrintableInstances {

  implicit val printableString: Printable[String] = new Printable[String] {
    def format(value: String): String = value
  }

  implicit val printableInt: Printable[Int] = new Printable[Int] {
    def format(value: Int): String = value.toString
  }

  implicit val printableCat: Printable[Cat] = new Printable[Cat] {
    def format(cat: Cat): String = {
      val name = Printable.format(cat.name)
      val age = Printable.format(cat.age)
      val color = Printable.format(cat.color)
      s"$name is a $age year-old $color cat."
    }
  }
}
