val scala3Version = "3.2.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "g-caos-cleaner-cli",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "com.google.api-client" % "google-api-client" % "1.25.1",
    libraryDependencies += "com.google.oauth-client" % "google-oauth-client" % "1.34.1",
    libraryDependencies += "com.google.apis" % "google-api-services-gmail" % "v1-rev110-1.25.0",
    libraryDependencies += "org.apache.httpcomponents.client5" % "httpclient5" % "5.2.1",
    libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.8.5",
    libraryDependencies += "com.softwaremill.sttp.client3" %% "upickle" % "3.8.5"
  )
