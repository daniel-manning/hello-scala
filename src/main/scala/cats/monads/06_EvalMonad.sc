// Evaluation in Scala

// Call-by-value evaluation
val x = {
  println("Computing x")
  math.random()
}
// Computing x
// val x: Double = 0.2623930428031148

// first access
x
// val res0: Double = 0.2623930428031148

// second access
x
// val res1: Double = 0.2623930428031148

// The computation is evaluated where it is defined (eager)
// The computation is evaluated once (memoized)

// Call-by-name evaluation
def y = {
  println("Computing y")
  math.random()
}
// def y: Double

// first access
y
// Computing y
// val res2: Double = 0.10811554030735937

// second access
y
// Computing y
// val res3: Double = 0.6377158880306714

// The computation is evaluated where it is used (lazy)
// The computation is evaluated each time (not memoized)

// Call-by-need evaluation
lazy val z = {
  println("Computing z")
  math.random()
}
// lazy val z: Double // unevaluated

// first access
z
// Computing z
// val res4: Double = 0.5830740397954601

// second access
z
// val res5: Double = 0.5830740397954601

// The computation is evaluated where it is used (lazy)
// The computation is evaluated once (memoized)

// cats Eval
import cats.Eval
val now = Eval.now(math.random() + 1000)
// val now: cats.Eval[Double] = Now(1000.0560528009801)
val always = Eval.always(math.random() + 2000)
// val always: cats.Eval[Double] = cats.Always@62cbfdbf
val later = Eval.later(math.random() + 2000)
// val later: cats.Eval[Double] = cats.Later@7d0ce9e1

now.value
// val res6: Double = 1000.0560528009801
always.value
// val res7: Double = 2000.4589666428435
later.value
// val res8: Double = 2000.7227542215044

// Scala - Cats
// val - Now
// def - Always
// lazy val - Later

// Eval as a Monad
val greeting = Eval
  .always { println("Step 1"); "Hello" }
  .map { s => println("Step 2"); s"$s world" }
// val greeting: cats.Eval[String] = cats.Eval$$anon$4@24de596
greeting.value
// Step 1
// Step 2
// val res9: String = Hello world

// Maps are always called lazily on demand
val ans = for {
  a <- Eval.now { println("Calculating a"); 40 }
  b <- Eval.always { println("Calculating b"); 2 }
} yield {
  println("Adding a and b")
  a + b
}
// val ans: cats.Eval[Int] = cats.Eval$$anon$4@4fa991fc

ans.value
// Calculating b
// Adding a and b
// val res10: Int = 42
ans.value
// Calculating b
// Adding a and b
// val res11: Int = 42

// memoize method
val saying =
  Eval
    .always { println("Step 1"); "The cat" }
    .map { s => println("Step 2"); s"$s sat on" }
    .memoize
    .map { s => println("Step 3"); s"$s the mat" }

saying.value // first access
// Step 1
// Step 2
// Step 3
// val res12: String = The cat sat on the mat

saying.value // second access
// Step 3
// val res13: String = The cat sat on the mat

// Trampolining and defer
def factorial(n: BigInt): Eval[BigInt] =
  if (n == 1) Eval.now(n)
  else Eval.defer(factorial(n - 1).map(_ * n))

factorial(50000).value
// val res14: BigInt = 334732050959714483691547609407148647791277322381045480773010032199016802214436564169738123107191693087984804381902082998936163847430666937426305728453637840383257562821233599872682440782359723560408538544413733837535685655363711683274051660761551659214061560754612942017905674796654986292422200225415535107181598016154764518106166749702179965374749725411393381916388235006303076442568748572713946510819098749096434862685892298078700310310089628611545539799116129406523273969714972110312611428607337935096878373558118306095517289066038335925328516359617308852798119573994952994503063544424784926410289900695596348835299005576765509291754759207880448076225624151651304590463180685174067663600123295564540657242251754734281831210291957155937874236411171945138385930380064131329763...

// We are avoiding creating a stack overflow :O
// ... by creating a chain of functions on the heap, which has other problems.

// Exercise: safer folding using Eval
def foldRightEval[A, B](as: List[A], acc: Eval[B])
  (fn: (A, Eval[B]) => Eval[B]): Eval[B] =
  as match {
    case head :: tail => Eval.defer(fn(head, foldRightEval(tail, acc)(fn)))
    case Nil          => acc
  }

def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
  foldRightEval(as, Eval.now(acc)) { (a, eb) => eb.map(fn(a, _))
  }.value

foldRight((1 to 100000).toList, 0L)(_ + _)
// val res15: Long = 5000050000
