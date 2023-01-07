ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.1"

val MunitVersion = "0.7.29"
val GoogleApiClientVersion = "1.25.1"
val GoogleOAuthClientVersion = "1.34.1"
val GoogleApiGMailVersion = "v1-rev110-1.25.0"
val ApacheHttpClientVersion = "5.2.1"
val SttpVersion = "3.8.5"
val scalaTestVersion = "3.2.14"

val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalameta" %% "munit" % MunitVersion % Test
  ),
)

lazy val core = (project in file("g-caos-cleaner-core"))
  .settings(commonSettings)
  .settings(
    name := "g-caos-cleaner-core"
  )

lazy val cli = (project in file("g-caos-cleaner-cli"))
  .settings(commonSettings)
  .settings(
    name := "g-caos-cleaner-cli",

    libraryDependencies ++= Seq(
      "com.google.api-client" % "google-api-client" % GoogleApiClientVersion,
      "com.google.oauth-client" % "google-oauth-client" % GoogleOAuthClientVersion,
      "com.google.apis" % "google-api-services-gmail" % GoogleApiGMailVersion,
      "org.apache.httpcomponents.client5" % "httpclient5" % ApacheHttpClientVersion,
      "com.softwaremill.sttp.client3" %% "core" % SttpVersion,
      "com.softwaremill.sttp.client3" %% "upickle" % SttpVersion,
    )
  )
  .dependsOn(core)
