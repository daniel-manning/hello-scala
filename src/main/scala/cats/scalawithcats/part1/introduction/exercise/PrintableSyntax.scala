package cats.scalawithcats.part1.introduction.exercise

object PrintableSyntax {

  implicit class PrintableOps[A](value: A) {

    def format(implicit printable: Printable[A]): String = printable.format(value)

    def print(implicit printable: Printable[A]): Unit = println(format(printable))

  }

}
