val AkkaVersion = "2.9.0"
val SttpVersion = "3.9.1"
val ScalatestVersion = "3.2.17"

lazy val commonSettings = Seq(
  organization := "de.htwg.se",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "3.3.1",
  resolvers += "Akka library repository" at "https://repo.akka.io/maven",
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
      "org.scala-lang.modules" %% "scala-parser-combinators" % "2.3.0"
    )
  )

lazy val root = project
  .in(file("."))
  .settings(
    commonSettings,
    name := "tui",
    libraryDependencies ++= Seq(
      "com.olvind.tui" %% "tui" % "0.0.7"
    )
  )
  .dependsOn(apiClient)
  .aggregate(actors, apiClient, dsl)
