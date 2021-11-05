import cats.data.Reader

final case class Cat(name: String, favoriteFood: String)

val catName: Reader[Cat, String] = Reader(cat => cat.name)
// val catName: cats.data.Reader[Cat,String] = Kleisli(<function>)

catName.run(Cat("Garfield", "lasagne"))
// val res0: cats.Id[String] = Garfield

// mapping of Reader for sequencing operations
val greetKitty: Reader[Cat, String] =
  catName.map(name => s"Hello $name")

greetKitty.run(Cat("Heathcliff", "junk food"))
// val res1: cats.Id[String] = Hello Heathcliff

// flatMap of Reader
val feedKitty: Reader[Cat, String] =
  Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

val greetAndFeed: Reader[Cat, String] =
  for {
    greet <- greetKitty
    feed <- feedKitty
  } yield s"$greet. $feed."

greetAndFeed(Cat("Garfield", "lasagne"))
// val res2: cats.Id[String] = Hello Garfield. Have a nice bowl of lasagne.
greetAndFeed(Cat("Heathcliff", "junk food"))
// val res3: cats.Id[String] = Hello Heathcliff. Have a nice bowl of junk food.

// Exercise:
final case class Db(
    usernames: Map[Int, String],
    passwords: Map[String, String]
)

type DbReader[A] = Reader[Db, A]

def findUsername(userId: Int): DbReader[Option[String]] =
  Reader(db => db.usernames.get(userId))

def checkPassword(username: String, password: String): DbReader[Boolean] =
  Reader { db =>
    val maybeUser = db.usernames.values.find(_ == username)
    maybeUser.flatMap(db.passwords.get).contains(password)
    // Book solution:
    // db.passwords.get(username).contains(password)
  }

def checkLogin(userId: Int, password: String): DbReader[Boolean] =
  findUsername(userId).flatMap {
    case Some(user) => checkPassword(user, password)
    case None       => Reader(_ => false)
  }
// Book solution:
import cats.syntax.applicative._ // for pure
def checkLoginSolution(userId: Int, password: String): DbReader[Boolean] =
  for {
    username <- findUsername(userId)
    passwordOk <- username.map { username =>
      checkPassword(username, password)
    } .getOrElse {
      false.pure[DbReader]
    }
  } yield passwordOk

val users = Map(
  1 -> "dade",
  2 -> "kate",
  3 -> "margo"
)

val passwords = Map(
  "dade" -> "zerocool",
  "kate" -> "acidburn",
  "margo" -> "secret"
)

val db = Db(users, passwords)
checkLogin(1, "zerocool").run(db)
// val res5: cats.Id[Boolean] = true
checkLogin(4, "davinci").run(db)
// val res6: cats.Id[Boolean] = false