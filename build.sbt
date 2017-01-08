name := "lunatech"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation")

val akkaV   = "2.4.12"
val akkaHttpV  = "10.0.0"

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
