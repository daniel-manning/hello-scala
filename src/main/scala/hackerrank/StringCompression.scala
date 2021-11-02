package hackerrank

object StringCompression {

  def main(args: Array[String]): Unit = {
    val line = scala.io.StdIn.readLine().toSeq

    def add(head: Char, count: Int) = if (count == 1) head.toString else s"$head${count.toString}"

    def go(line: Seq[Char], result: Seq[Char], count: Int): (Seq[Char], Seq[Char], Int) = {
      lazy val ys = line.tail
      line match {
        case Nil                    => ("", result, count)
        case x if ys.isEmpty        => go(ys, result ++ add(x.head, count), count)
        case x if x.head == ys.head => go(ys, result, count + 1)
        case x                      => go(ys, result ++ add(x.head, count), 1)
      }
    }

    val (_, result, _) = go(line, "", 1)
    println(result.mkString(""))
  }
}
