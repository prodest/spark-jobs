logLevel := Level.Warn

//scalaVersion := "2.10.6"
//
//crossScalaVersions := Seq("2.10.6","2.11.8")


addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3" )

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

addSbtPlugin("org.scala-sbt.plugins" % "sbt-onejar" % "0.8")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.0")
