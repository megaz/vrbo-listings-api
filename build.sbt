name := "vrbo-listings-api"
version := "1.0"
scalaVersion := "2.12.1"

resolvers += Resolver.jcenterRepo

libraryDependencies += "com.twitter" %% "finagle-http" % "19.7.0"
libraryDependencies += "com.twitter" %% "finatra-http" % "19.7.0"
libraryDependencies += "com.github.cb372" %% "scalacache-caffeine" % "0.28.0"
libraryDependencies += "com.github.cb372" %% "scalacache-core" % "0.28.0"
