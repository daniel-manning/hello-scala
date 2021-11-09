import cats.Id
import cats.data.EitherT

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

type Response[A] = EitherT[Future, String, A]

val powerLevels = Map(
  "Jazz" -> 6,
  "Bumblebee" -> 8,
  "Hot Rod" -> 10
)

import cats.instances.future._ // for Monad

def getPowerLevel(autobot: String): Response[Int] =
  powerLevels.get(autobot) match {
    case Some(value) => EitherT.right(Future(value))
    case None => EitherT.left(Future(s"$autobot unreachable"))
  }

def canSpecialMove(ally1: String, ally2: String): Response[Boolean] =
  for {
    a <- getPowerLevel(ally1)
    b <- getPowerLevel(ally2)
  } yield (a + b) > 15

def tacticalReport(ally1: String, ally2: String): String = {
  val stack = canSpecialMove(ally1, ally2).value

  Await.result(stack, 1.second) match {
    case Left(value) => s"Comms error: $value"
    case Right(true) => s"$ally1 and $ally2 are ready to roll out!"
    case Right(false) => s"$ally1 and $ally2 need a recharge"
  }
}

tacticalReport("Jazz", "Bumblebee")
// val res0: String = Jazz and Bumblebee need a recharge
tacticalReport("Bumblebee", "Hot Rod")
// val res1: String = Bumblebee and Hot Rod are ready to roll out!
tacticalReport("Jazz", "Ironhide")
// val res2: String = Comms error: Ironhide unreachable





