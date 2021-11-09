import cats.arrow.FunctionK

object optionToList extends FunctionK[Option, List] {
  override def apply[A](fa: Option[A]): List[A] = fa match {
    case None => List.empty
    case Some(a) => List(a)
  }
}

optionToList(Some(1))
// val res0: List[Int] = List(1)
optionToList(None)
// val res1: List[Nothing] = List()
