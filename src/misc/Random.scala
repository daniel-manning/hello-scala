package misc

object Random {
  def main(args: Array[String]): Unit = {
    val r = scala.util.Random
    println(r.nextInt)
    println(r.nextInt(100))
    println(r.nextFloat)
    println(r.nextDouble)
    println("-1------------")

    val range = 0 to r.nextInt(10)
    range.foreach(println)
    println("-2------------")

    val list = for(i<- 0 to r.nextInt(10)) yield i*2
    list.foreach(println)
    println("-3------------")
  }
}
