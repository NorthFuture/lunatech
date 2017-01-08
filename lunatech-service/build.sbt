name := "lunatech-service"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation")

val akkaVersion = "2.4.12"
val delimitedCoreVersion = "0.7.0"
val junitVersion = "4.10"
val scalatestVersion = "3.0.1"
val logbackClassicVersion = "1.1.7"
val scalaLoggingVersion = "3.5.0"

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

