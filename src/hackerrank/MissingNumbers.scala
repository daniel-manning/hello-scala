package hackerrank

object MissingNumbers {

  def main(args: Array[String]) {
    def count(list: Seq[Int], acc: Map[Int, Int] = Map()): Map[Int, Int] = list.toList match {
      case Nil => acc
      case head :: _ => count(list.filterNot(_ == head), acc + (head -> list.count(_ == head)))
      case x => acc + (x.head -> list.count(_ == x.head))
    }

    val f = () => scala.io.StdIn.readLine()
    val readList: String => Seq[Int] = s => s.trim.split(' ').map(_.toInt)
    val go = () => count(readList(f()))

    val (_, mapa, _, mapb) = (f(), go(), f(), go())
    val result = (mapa.toSet diff mapb.toSet).toMap.keySet.toList.sorted
    println(result.mkString(" "))
  }
}