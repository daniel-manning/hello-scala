import cats.introduction.json.JsonWriterInstances._
import cats.introduction.json.{JsonOps, Person}

JsonOps.toJson(Person("Dave", "dave@example.com"))
// val res0: cats.introduction.Json = JsObject(Map(name -> JsString(Dave), email -> JsString(dave@example.com)))