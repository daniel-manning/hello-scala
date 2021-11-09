import cats.scalawithcats.part1.introduction.json.JsonWriterInstances._
import cats.scalawithcats.part1.introduction.json.{JsonOps, Person}

JsonOps.toJson(Person("Dave", "dave@example.com"))
// val res0: cats.scalawithcats.part1.introduction.Json = JsObject(Map(name -> JsString(Dave), email -> JsString(dave@example.com)))