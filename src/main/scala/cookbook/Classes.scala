package cookbook

case class Person(var name: String, var age: Int)
object Person{
  def apply() = new Person("<no name>", 0)
  def apply(name: String) = new Person(name, 0)
}

class Brain private{
  override def toString ="This is the brain."
}

object Brain{
  val brain = new Brain
  def getInstance: Brain = brain
}

object Classes extends App {

  println("Multi Constructor Example")
  val a = Person()
  val b = Person("Pam")
  val c = Person("Pedro", 76)

  println(a)
  println(b)
  println(c)

  a.name = "Terry"
  a.age = 53
  println(a)

  println("Enforcing Singleton Example")
  // This won't compile
  // val brain = new Brain

  val brain = Brain.getInstance
  println(brain)

  println("Default values example")
  case class Timer(start: Int = 0, stop: Int = 60)
  println(new Timer)
  println(Timer(10))
  println(Timer(30, 120))
  println(Timer(stop = 15))
  println(Timer(start = 15))
  println(Timer(stop = 120, start = 15))
}

