import common._

name := "lunatech-service"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation")
libraryDependencies ++= Seq(

  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,

  "net.tixxit" %% "delimited-core" % delimitedCoreVersion,

  "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,

  "junit" % "junit" % junitVersion % "test",
  "org.scalatest" %% "scalatest" % scalatestVersion % "test"
)

