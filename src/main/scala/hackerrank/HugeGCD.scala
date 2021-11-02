package hackerrank

object HugeGCD {

  def main(args: Array[String]): Unit = {

    val modulo = 1000000007

    def gcd(x: BigInt, y: BigInt): BigInt = if (x == 0) y else gcd(y % x, x)

    val f = () => scala.io.StdIn.readLine()
    val product: String => BigInt = s => s.trim.split(' ').map(BigInt(_)).product

    val (_, la, _, lb) = (f(), product(f()), f(), product(f()))
    println(gcd(la, lb) % modulo)


  }
}