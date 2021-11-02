import cats.introduction.json.JsonWriterInstances._
import cats.introduction.json.JsonSyntax._
import cats.introduction.json.Person

Person("Dave", "dave@example.com").toJson
// val res0: cats.introduction.Json = JsObject(Map(name -> JsString(Dave), email -> JsString(dave@example.com)))