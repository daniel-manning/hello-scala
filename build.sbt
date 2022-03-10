scalaVersion := "2.13.8"

Test / fork := true

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.7.0",
  "org.scalatest" %% "scalatest" % "3.2.11" % "test",
  "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.40.2" % "test",
  "com.dimafeng" %% "testcontainers-scala-kafka" % "0.40.2" % "test",
  "com.dimafeng" %% "testcontainers-scala-mysql" % "0.40.2" % "test",
  "mysql" % "mysql-connector-java" % "8.0.28",
  "com.github.fd4s" %% "fs2-kafka-vulcan" % "2.3.0"
)

resolvers += "confluent" at "https://packages.confluent.io/maven/"

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-deprecation",
  "-feature"
)