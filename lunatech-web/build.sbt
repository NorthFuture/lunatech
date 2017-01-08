name := "lunatech-web"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation")

val akkaV   = "2.4.12"
val akkaHttpV  = "10.0.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-xml" % akkaHttpV
)
