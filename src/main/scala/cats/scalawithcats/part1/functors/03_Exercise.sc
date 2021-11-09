// Write a functor for the following binary tree data type.
// Verify that the code works as expected on instances of Branch and Leaf:

sealed trait Tree[+A] extends Product

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

import cats.Functor

implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
  def map[A, B](tree: Tree[A])(f: A => B) = tree match {
    case Branch(left, right) =>
      Branch(
        map(left)(f),
        map(right)(f)
      )
    case Leaf(value) => Leaf(f(value))
  }
}

// The problem of invariance arises again:
// Branch(Leaf(10), Leaf(20)).map(_ * 2)
// value map is not a member of Branch[Int]

object Tree {
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

  def leaf[A](value: A): Tree[A] = Leaf(value)
}

// Now we can use our Functor properly!
import cats.syntax.functor._

Tree.leaf(100).map(_ * 2)
// val res0: Tree[Int] = Leaf(200)

Tree.branch(Tree.leaf(10), Tree.leaf(20)).map(_ * 2)
// val res1: Tree[Int] = Branch(Leaf(20),Leaf(40))