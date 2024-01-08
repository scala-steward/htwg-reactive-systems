val AkkaVersion = "2.9.0"
val ScalatestVersion = "3.2.17"

lazy val commonSettings = Seq(
  organization := "de.htwg.se",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "3.3.1",
  resolvers += "Akka library repository" at "https://repo.akka.io/maven",
  resolvers += "confluent" at "https://packages.confluent.io/maven/",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % ScalatestVersion % Test,
    "org.scalactic" %% "scalactic" % ScalatestVersion % Test
  )
)

lazy val actors = project
  .in(file("actors"))
  .settings(
    commonSettings,
    name := "actors",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test
    )
  )

lazy val apiClient = project
  .in(file("apiclient"))
  .settings(
    commonSettings,
    name := "apiclient",
    libraryDependencies ++= Seq(
      "com.lihaoyi" % "ujson_3" % "3.1.3",
      "org.scalaj" % "scalaj-http_2.13" % "2.4.2",
      "com.squareup.okhttp3" % "mockwebserver" % "4.12.0" % Test
    )
  )

lazy val dsl = project
  .in(file("dsl"))
  .settings(
    commonSettings,
    name := "dsl",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % "2.2.0"
    )
  )

lazy val streams = project
  .in(file("streams"))
  .settings(
    commonSettings,
    name := "streams",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "org.apache.kafka" % "kafka-clients" % "7.5.2-ce",
     /*  "org.slf4j" % "slf4j-api" % "2.0.10",
      "ch.qos.logback" % "logback-classic" % "1.4.14" */
    )
  )
  .dependsOn(dsl)

lazy val spark = project
  .in(file("spark"))
  .settings(
    commonSettings,
    name := "spark",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "3.5.0" cross CrossVersion.for3Use2_13,
      "org.apache.spark" %% "spark-sql" % "3.5.0" cross CrossVersion.for3Use2_13,
      "org.apache.spark" %% "spark-streaming" % "3.5.0" cross CrossVersion.for3Use2_13,
      "org.scala-lang.modules" %% "scala-xml" % "2.2.0" cross CrossVersion.for3Use2_13,
      "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.5.0" cross CrossVersion.for3Use2_13,

    ),
    excludeDependencies ++= Seq(
      ExclusionRule("org.scala-lang.modules", "scala-xml_3"),
      ExclusionRule("org.scala-lang.modules", "scala-parser-combinators_2.13")
    )
  )
  .dependsOn(dsl)
lazy val root = project
  .in(file("."))
  .settings(
    commonSettings,
    name := "tui",
    libraryDependencies ++= Seq(
      "com.olvind.tui" %% "tui" % "0.0.7" ,
    )
  )
  .dependsOn(apiClient)
  .aggregate(actors, apiClient, dsl, streams)
