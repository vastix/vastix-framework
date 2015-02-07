name := "Kipo"

version := "0.0.1"

scalaVersion := "2.11.5"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {

  val (akkaV, streamHttp) = ("2.3.9", "1.0-M3")

  Seq(
    "org.parboiled"           %% "parboiled"                         % "2.0.1",
    //"org.scala-lang.modules"  % "scala-parser-combinators_2.11"     % "1.0.3",
    "com.typesafe.akka"       %% "akka-stream-experimental"     % streamHttp,
    "com.typesafe.akka"       %% "akka-http-experimental"       % streamHttp,
    "com.typesafe.akka"       %% "akka-http-core-experimental"  % streamHttp,
    "com.typesafe.akka"       %% "akka-actor"                   % akkaV,
    "com.typesafe.akka"       %% "akka-testkit"                 % akkaV     % "test",  
    "org.specs2"              %% "specs2-core"                  % "2.4.16"  % "test"
  )

}

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
