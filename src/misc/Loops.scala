package misc

object Loops {
  def main(args: Array[String]): Unit = {

    val a = Array("apple", "banana", "orange", "strawberry")
    for((e, count) <- a.zipWithIndex){
      println(s"$count is $e")
    }
    println("-1----------")

    val players = Map(5 -> "Garnett",
      8 -> "Bryant",
      13 -> "Chamberlain",
      16 -> "Gasol",
      21 -> "Duncan",
      23 -> "Jordan",
      24 -> "Bryant",
      32 -> "Johnson",
      33 -> "Abdul-Jabbar",
      34 -> "O'Neal")

    for((k,v) <- players) println(f"Number: $k%2d, player: $v")
    println("-2----------")

    val array = Array.ofDim[Int](2,2)
    array(0)(0) = 0
    array(0)(1) = 1
    array(1)(0) = 2
    array(1)(1) = 3

    for{
      i <- 0 to 1
      j <- 0 to 1
    } println(s"array($i)($j) = ${array(i)(j)}")
  }
}
