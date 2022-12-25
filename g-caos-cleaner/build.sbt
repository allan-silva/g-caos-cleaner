val scala3Version = "3.2.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "g-caos-cleaner",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "com.google.api-client" % "google-api-client" % "2.1.1",
    libraryDependencies += "com.google.oauth-client" % "google-oauth-client" % "1.34.1",
    libraryDependencies += "com.google.apis" % "google-api-services-gmail" % "v1-rev110-1.25.0",
  )