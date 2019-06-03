package hackerrank

object SequenceFullOfColors {

  def main(args: Array[String]) {
    val stdin = scala.io.StdIn

    for(_ <- 0 until stdin.readInt){
      val line = stdin.readLine

      def checkPrefix(chars: List[Char], rg: Int, yb: Int): Boolean = {
        chars match {
          case Nil => rg == 0 && yb == 0
          case _ if rg.abs > 1 || yb.abs > 1 => false
          case s :: xs => s match {
            case 'R' => checkPrefix(xs, rg + 1, yb)
            case 'G' => checkPrefix(xs, rg - 1, yb)
            case 'Y' => checkPrefix(xs, rg, yb + 1)
            case 'B' => checkPrefix(xs, rg, yb - 1)
          }
        }
      }

      println(if (checkPrefix(line.toList, 0, 0)) "True" else "False")
    }
  }
}
