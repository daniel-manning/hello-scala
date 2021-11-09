import cats.Monoid
import cats.instances.string._ // for Monoid
import cats.syntax.invariant._ // for imap
import cats.syntax.semigroup._ // for |+|

implicit val symbolMonoid: Monoid[Symbol] =
  Monoid[String].imap(Symbol.apply)(_.name)

Monoid[Symbol].empty
// val res0: Symbol = Symbol()

Symbol("a") |+| Symbol("few") |+| Symbol("words")
// val res1: Symbol = Symbol(afewwords)
