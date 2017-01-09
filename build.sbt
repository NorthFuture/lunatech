organization := "com.lunatech"
name := "lunatech"

scalacOptions ++= Seq("-deprecation")

version := "1.0.0"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
)

lazy val lunatechModel = project
  .in(file("lunatech-model"))

lazy val lunatechService = project
  .in(file("lunatech-service")).dependsOn(lunatechModel)

lazy val lunatechWeb = project
  .in(file("lunatech-web")).dependsOn(lunatechService)


lazy val lunatech = project
  .in(file("."))
  .aggregate(lunatechModel)
  .aggregate(lunatechService)
  .aggregate(lunatechWeb)
