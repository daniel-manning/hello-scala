import cats.scalawithcats.part1.introduction.json.JsonWriterInstances._
import cats.scalawithcats.part1.introduction.json.JsonSyntax._
import cats.scalawithcats.part1.introduction.json.Person

Person("Dave", "dave@example.com").toJson
// val res0: cats.scalawithcats.part1.introduction.Json = JsObject(Map(name -> JsString(Dave), email -> JsString(dave@example.com)))