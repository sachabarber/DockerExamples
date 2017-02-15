name := "SimpleAkkaHttpServiceWithMySql"
version := "1.0"
scalaVersion := "2.11.7"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
assemblyJarName in assembly := "SimpleAkkaHttpServiceWithMySql.jar"

libraryDependencies ++= {
  val akkaStreamVersion = "1.0"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.3.12",
    "com.typesafe.akka" % "akka-stream-experimental_2.11" % akkaStreamVersion,
    "com.typesafe.akka" % "akka-http-core-experimental_2.11" % akkaStreamVersion,
    "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaStreamVersion,
    "mysql"             % "mysql-connector-java" % "5.1.24"
  )
}

Revolver.settings