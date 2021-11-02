package cats.introduction

object JsonOps {

  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)

}
