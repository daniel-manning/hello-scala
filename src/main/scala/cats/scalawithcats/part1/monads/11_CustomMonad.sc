sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)
def leaf[A](value: A): Tree[A] = Leaf(value)

// Let's write a Tree Monad!
import cats.Monad
import scala.annotation.tailrec

// Easy solution
implicit val treeMonad: Monad[Tree] = new Monad[Tree] {
  def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = fa match {
    case Branch(left, right) => Branch(flatMap(left)(f), flatMap(right)(f))
    case Leaf(value)         => f(value)
  }

  // @tailrec
  def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] =
    flatMap(f(a)) {
      case Left(value)  => tailRecM(value)(f)
      case Right(value) => Leaf(value)
    }

  def pure[A](x: A) = leaf(x)
}

// Tailrec solution
/** def tailRecM[A, B](arg: A)
  *      (func: A => Tree[Either[A, B]]): Tree[B] = {
  * @tailrec
  * def loop(
  *          open: List[Tree[Either[A, B]]],
  * closed: List[Option[Tree[B]]]): List[Tree[B]] = open match {
  *        case Branch(l, r) :: next =>
  *          loop(l :: r :: next, None :: closed)
  *        case Leaf(Left(value)) :: next =>
  *          loop(func(value) :: next, closed)
  *        case Leaf(Right(value)) :: next =>
  *          loop(next, Some(pure(value)) :: closed)
  * case Nil =>
  * closed.foldLeft(Nil: List[Tree[B]]) { (acc, maybeTree) =>
  *            maybeTree.map(_ :: acc).getOrElse {
  *              val left :: right :: tail = acc
  *              branch(left, right) :: tail
  * } }
  * }
  *    loop(List(func(arg)), Nil).head
  *  }
  */

// Now we can use our Monad to flatMap and map on Trees!
import cats.syntax.functor._ // for map
import cats.syntax.flatMap._ // for flatMap

branch(leaf(100), leaf(200))
  .flatMap(x => branch(leaf(x - 1), leaf(x + 1)))
// val res1: Tree[Int] = Branch(Branch(Leaf(99),Leaf(101)),Branch(Leaf(199),Leaf(201)))

// We can also transform Trees using for comprehensions:
for {
  a <- branch(leaf(100), leaf(200))
  b <- branch(leaf(a - 10), leaf(a + 10))
  c <- branch(leaf(b - 1), leaf(b + 1))
} yield c
// val res2: Tree[Int] =
//   Branch(
//     Branch(
//       Branch(Leaf(89), Leaf(91)),
//       Branch(Leaf(109), Leaf(111))
//     ),
//     Branch(
//       Branch(Leaf(189), Leaf(191)),
//       Branch(Leaf(209), Leaf(211))
//     )
//   )
