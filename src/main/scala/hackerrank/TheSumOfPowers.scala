package hackerrank

import scala.annotation.tailrec

object TheSumOfPowers {

  def main(args: Array[String]): Unit = {

    val stdin = scala.io.StdIn

    val x = stdin.readInt()
    val n = stdin.readInt()

    val top = n match {
      case 2 => math.sqrt(x)
      case _ => math.cbrt(x)
    }

    val range: Seq[Int] = (1 to math.floor(top).toInt).map(math.pow(_, n).toInt).filter(_ <= x)
    val entry: Seq[(Seq[Int], Int)] = range.map(e => (Seq(e), e))

    @tailrec
    def step(factorizations: Seq[(Seq[Int], Int)], found: Seq[Seq[Int]]): (Seq[(Seq[Int], Int)], Seq[Seq[Int]]) = {
      factorizations match {
        case f if f.isEmpty => (factorizations, found)
        case f if f.nonEmpty =>
          val (done, todo) = factorizations
            .filter(_._2 <= x) //Already controlled input but whatever
            .partition(_._2 == x)
          val nextStep: Seq[(Seq[Int], Int)] = for (
            e <- todo;
            r <- range if !(e._1 contains r) && (e._1.sum + r) <= x
          ) yield {
            (e._1 :+ r, e._2 + r)
          }
          val processedNextStep = nextStep.filter(_._2 <= x).map( l => (l._1.sorted, l._2)).distinct
          val newFound: Seq[Seq[Int]] = found ++ done.map(_._1.sorted).distinct
          step(processedNextStep, newFound)
      }
    }

    val result = step(entry, Seq())._2.size
    println(result)

  }
}
