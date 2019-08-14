package hackerrank

import scala.collection.mutable


object DifferentWays {

  def main(args: Array[String]) {

    val stdIn = scala.io.StdIn
    val modulo = 100000007

    def getTable(n: Int = 1001, k: Int = 1001): Array[Array[Long]] = {
      val mutableTable: mutable.ArrayBuffer[Array[Long]] = collection.mutable.ArrayBuffer()
      mutableTable += 1L +: Array.fill(n-1)(0L)
      for (i <- 1 until k) {
        val a1 = mutableTable(i-1) :+ 0L
        val a2 = 0L +: mutableTable(i-1)
        mutableTable += (0 until n).map( e => (a1(e) + a2(e)) % modulo).toArray
      }
      mutableTable.toArray
    }
    val table = getTable()

    val T = stdIn.readInt()
    for (_ <- 1 to T) {
      val line = stdIn.readLine().split(' ')
      val N = line.head.toInt
      val K = line(1).toInt
      println(table(N)(K))
    }

  }
}