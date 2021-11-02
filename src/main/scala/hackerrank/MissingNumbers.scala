package main.scala.hackerrank

import scala.collection.immutable.ArraySeq

object MissingNumbers {

  def main(args: Array[String]): Unit = {
    def count(list: Seq[Int], acc: Map[Int, Int] = Map()): Map[Int, Int] = list.toList match {
      case Nil => acc
      case head :: _ => count(list.filterNot(_ == head), acc + (head -> list.count(_ == head)))
      case x => acc + (x.head -> list.count(_ == x.head))
    }

    val f = () => scala.io.StdIn.readLine()
    val readList: String => Seq[Int] = s => ArraySeq.unsafeWrapArray(s.trim.split(' ').map(_.toInt))
    val go = () => count(readList(f()))

    val (_, mapa, _, mapb) = (f(), go(), f(), go())
    val result = (mapa.toSet diff mapb.toSet).toMap.keySet.toList.sorted
    println(result.mkString(" "))
  }
}