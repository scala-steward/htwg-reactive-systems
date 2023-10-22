val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "htwg-reactive-systems",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "com.olvind.tui" %% "tui" % "0.0.7",
    libraryDependencies += "org.scalaj" % "scalaj-http_2.13" % "2.4.2",
    libraryDependencies += "com.lihaoyi" % "ujson_3" % "3.1.3"
  )
