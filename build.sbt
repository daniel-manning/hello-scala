scalaVersion := "2.13.7"

libraryDependencies +=
  "org.typelevel" %% "cats-core" % "2.1.0"

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-deprecation",
  "-feature"
)