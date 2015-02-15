enablePlugins(ScalaJSPlugin)

name := "Kipo"

version := "0.0.1"

scalaVersion := "2.11.5"

resolvers += Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "nxparser-repo" at "http://nxparser.googlecode.com/svn/repository/",
  "Bigdata releases" at "http://systap.com/maven/releases/",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases",
  "apache-repo-releases" at "http://repository.apache.org/content/repositories/releases/"
)

libraryDependencies ++= {

  val (akkaV, streamHttp, bigDataV) = ("2.3.9", "1.0-M3", "1.5.0")

  Seq(
    "org.parboiled"           %% "parboiled"                    % "2.0.1",
    //"org.scala-lang.modules"  % "scala-parser-combinators_2.11"     % "1.0.3",
    "com.typesafe.akka"       %% "akka-stream-experimental"     % streamHttp,
    "com.typesafe.akka"       %% "akka-http-experimental"       % streamHttp,
    "com.typesafe.akka"       %% "akka-http-core-experimental"  % streamHttp,
    "com.typesafe.akka"       %% "akka-actor"                   % akkaV,
    "com.bigdata"             %% "bigdata"                      % bigdataV,
    "com.lihaoyi"             %% "scalatags"                    % "0.4.5"
    "com.typesafe.akka"       %% "akka-testkit"                 % akkaV     % "test",  
    "org.specs2"              %% "specs2-core"                  % "2.4.16"  % "test"
  )

}

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

scalaJSStage in Global := FastOptStage
