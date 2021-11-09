package cats.scalawithcats.part1.introduction.json

object JsonSyntax {

  implicit class JsonWriterOps[A](value: A) {

    def toJson(implicit w: JsonWriter[A]): Json = w.write(value)

  }

}
