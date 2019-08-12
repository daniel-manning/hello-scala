package hackerrank

object ListsAndGCD {

  def main(args: Array[String]) {
    val stdIn = scala.io.StdIn
    val q = stdIn.readInt

    def parse(array: Array[Int], map: Map[Int, Int]): (Array[Int], Map[Int, Int]) = {
      array.length match {
        case x if x >= 3 => parse(array.tail.tail, map + (array.head -> array(1)))
        case x if x >= 1 => (Array(), map + (array.head -> array(1)))
        case x if x == 0 => (Array(), map)
      }
    }

    val factorizations: Seq[Map[Int, Int]] = for (_ <- 1 to q) yield {
      val line = stdIn.readLine.split(' ').map(_.toInt)
      val (_, factorization) = parse(line, Map())
      factorization
    }

    import scala.math.min

    val result = factorizations
      .reduce((m1, m2) => m1.keySet.intersect(m2.keySet).map(x => x -> min(m1(x), m2(x))).toMap)
      .toArray.sortBy( t => t._1)
      .map(t => t._1 + " " + t._2).mkString(" ")

    println(result)
  }
}
