name := "Kipo"

version := "0.0.1"

scalaVersion := "2.11.5"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {

  val (akkaV, streamHttp) = ("2.3.9", "1.0-M3")

  Seq(
    "org.scala-lang.modules"  % "scala-parser-combinators_2.11"     % "1.0.3",
    "com.typesafe.akka"       % "akka-stream-experimental_2.11"     % streamHttp,
    "com.typesafe.akka"       % "akka-http-experimental_2.11"       % streamHttp,
    "com.typesafe.akka"       % "akka-http-core-experimental_2.11"  % streamHttp,
    "com.typesafe.akka"       % "akka-actor_2.11"                   % akkaV,
    "com.typesafe.akka"       % "akka-testkit_2.11"                 % akkaV     % "test",  
    "org.specs2"              % "specs2-core_2.11"                  % "2.4.16"  % "test"
  )

}

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
