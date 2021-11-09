package cats.scalawithcats.part1.introduction.exercise

trait Printable[A] {
  def format(value: A): String
}

object Printable {

  def format[A](value: A)(implicit printable: Printable[A]): String =
    printable.format(value)

  def print[A](value: A)(implicit printable: Printable[A]): Unit =
    println(format(value))
}
