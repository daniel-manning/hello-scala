import cats.introduction.JsonWriterInstances._
import cats.introduction.JsonSyntax._
import cats.introduction.Person

Person("Dave", "dave@example.com").toJson
// val res0: cats.introduction.Json = JsObject(Map(name -> JsString(Dave), email -> JsString(dave@example.com)))