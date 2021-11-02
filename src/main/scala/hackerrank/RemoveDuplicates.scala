package hackerrank

object RemoveDuplicates {

  def main(args: Array[String]): Unit = {
    val stdIn = scala.io.StdIn
    val line = stdIn.readLine().trim.toCharArray.foldLeft("")( (acc, c) => if (acc.exists(_==c)) acc else acc :+ c)
    println(line)
  }
}