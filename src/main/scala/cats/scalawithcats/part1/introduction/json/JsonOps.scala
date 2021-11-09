package cats.scalawithcats.part1.introduction.json

object JsonOps {

  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)

}
