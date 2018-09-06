name := "EmansDashboard"

version := "1.0"

lazy val `emansdashboard` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.0"
libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0"
libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.0"
libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0"
libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.1.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "1.0.0-RC3"