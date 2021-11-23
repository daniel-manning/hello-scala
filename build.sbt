scalaVersion := "2.13.7"

Test / fork := true

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.1.0",
  "org.scalatest" %% "scalatest" % "3.2.10" % "test",
  "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.39.12" % "test",
  "com.dimafeng" %% "testcontainers-scala-kafka" % "0.39.12" % "test",
  "com.github.fd4s" %% "fs2-kafka-vulcan" % "2.2.0"
)

resolvers += "confluent" at "https://packages.confluent.io/maven/"

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-deprecation",
  "-feature"
)