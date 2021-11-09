trait Printable[A] { self =>
  def format(value: A): String

  def contramap[B](func: B => A): Printable[B] =
    new Printable[B] {
      def format(value: B) = self.format(func(value))
    }
}

def format[A](value: A)(implicit p: Printable[A]): String =
  p.format(value)

implicit val stringPrintable: Printable[String] =
  new Printable[String] {
    def format(value: String) = s"'$value'"
  }

implicit val booleanPrintable: Printable[Boolean] =
  new Printable[Boolean] {
    def format(value: Boolean) = if (value) "yes" else "no"
  }

format("hello")
// val res0: String = 'hello'
format(true)
// val res1: String = yes

// Exercise: define Printable for the following:
final case class Box[A](value: A)

implicit def boxPrintable[A](implicit printable: Printable[A]): Printable[Box[A]] =
  printable.contramap[Box[A]](_.value)

format(Box("hello world"))
// val res2: String = 'hello world'
format(Box(true))
// val res3: String = yes

// Undefined Printable fails to compile:
// format(Box(123))
// could not find implicit value for parameter p: Printable[Box[Int]]
