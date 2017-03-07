name := "SimpleAkkaHttpServiceWithCassandra"
version := "1.0"
scalaVersion := "2.11.7"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
assemblyJarName in assembly := "SimpleAkkaHttpServiceWithCassandra.jar"

libraryDependencies ++= {
  val akkaStreamVersion = "1.0"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.3.12",
    "com.typesafe.akka" % "akka-stream-experimental_2.11" % akkaStreamVersion,
    "com.typesafe.akka" % "akka-http-core-experimental_2.11" % akkaStreamVersion,
    "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaStreamVersion,
    "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.4"
  )
}


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case _ => MergeStrategy.first
}

Revolver.settings