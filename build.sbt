lazy val root = project
  .in(file("."))
  .settings(
    name := "htwg-reactive-systems",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.3.1",

    // UI dependencies
    libraryDependencies += "com.olvind.tui" %% "tui" % "0.0.7",

    // API client dependencies
    libraryDependencies += "com.lihaoyi" % "ujson_3" % "3.1.3",
    libraryDependencies += "org.scalaj" % "scalaj-http_2.13" % "2.4.2",

    // DSL Parser dependencies
    libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.3.0",

    // Actor
    libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0",
    libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.8.0" % Test,

    // Test dependencies
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.17" % Test,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test,
    libraryDependencies += "com.squareup.okhttp3" % "mockwebserver" % "4.12.0" % Test
  )
