name := "vrbo-listings-api"
version := "1.0"
scalaVersion := "2.12.8"
resolvers += Resolver.jcenterRepo

val twitterVersion = "19.1.0"

libraryDependencies += "com.twitter" %% "finagle-http" % twitterVersion
libraryDependencies += "com.twitter" %% "finatra-http" % twitterVersion
libraryDependencies += "com.twitter" %% "finatra-jackson" % twitterVersion
libraryDependencies += "com.github.cb372" %% "scalacache-caffeine" % "0.28.0"
libraryDependencies += "com.github.cb372" %% "scalacache-core" % "0.28.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "com.twitter" %% "inject-server" % twitterVersion % Test
libraryDependencies += "com.twitter" %% "inject-app" % twitterVersion % Test
libraryDependencies += "com.twitter" %% "inject-core" % twitterVersion % Test
libraryDependencies += "com.twitter" %% "inject-modules" % twitterVersion % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test
libraryDependencies += "io.circe" %% "circe-parser" % "0.12.3" % Test

libraryDependencies += "com.twitter" %% "finatra-http" % twitterVersion % "test" classifier "tests"
libraryDependencies += "com.twitter" %% "finatra-jackson" % twitterVersion % "test" classifier "tests"
libraryDependencies += "com.twitter" %% "inject-server" % twitterVersion % "test" classifier "tests"
libraryDependencies += "com.twitter" %% "inject-app" % twitterVersion % "test" classifier "tests"
libraryDependencies += "com.twitter" %% "inject-core" % twitterVersion % "test" classifier "tests"
libraryDependencies += "com.twitter" %% "inject-modules" % twitterVersion % "test" classifier "tests"

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
enablePlugins(AshScriptPlugin)

dockerBaseImage := "openjdk:8-jre-alpine"
mainClass in Compile := Some("com.vrbo.listings.ListingsApi")