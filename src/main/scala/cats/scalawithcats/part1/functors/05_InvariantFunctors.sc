trait Codec[A] { self =>
  def encode(value: A): String
  def decode(value: String): A
  def imap[B](dec: A => B, enc: B => A): Codec[B] =
    new Codec[B] {
      def encode(value: B) = self.encode(enc(value))
      def decode(value: String) = dec(self.decode(value))
    }
}

def encode[A](value: A)(implicit c: Codec[A]): String =
  c.encode(value)

def decode[A](value: String)(implicit c: Codec[A]): A =
  c.decode(value)

// Starting from a simple Codec:
implicit val stringCodec: Codec[String] = new Codec[String] {
  def encode(value: String) = value
  def decode(value: String) = value
}

// We can construct many useful ones using imap:
implicit val intCodec: Codec[Int] =
  stringCodec.imap(_.toInt, _.toString)

implicit val booleanCodec: Codec[Boolean] =
  stringCodec.imap(_.toBoolean, _.toString)

// Note that the decode method doesn't account for failures
// If we wanted to do so, we can take a look at lenses and optics
// Shameless HablaApps plug: https://github.com/hablapps/DontFearTheProfunctorOptics/blob/master/Optics.md

implicit val doubleCodec: Codec[Double] =
  stringCodec.imap(_.toDouble, _.toString)

final case class Box[A](value: A)

implicit def boxCodec[A](implicit c: Codec[A]): Codec[Box[A]] =
  c.imap[Box[A]](Box(_), _.value)

encode(123.4)
// val res0: String = 123.4
decode[Double]("123.4")
// val res1: Double = 123.4
encode(Box(123.4))
// val res2: String = 123.4
decode[Box[Double]]("123.4")
// val res3: Box[Double] = Box(123.4)