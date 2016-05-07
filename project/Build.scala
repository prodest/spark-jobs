import com.github.retronym.SbtOneJar
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import sbt.Keys._
import sbt._
import sbtassembly.{Assembly, MergeStrategy, PathList}


object xBuild extends Build {

//  enablePlugins(JavaAppPackaging)


  lazy val buildSettings = Seq(
    version := "0.1-SNAPSHOT",
    organization := "es.gov.prodest",
    scalaVersion := "2.11.8"
  )

  val defaultMergeStrategy: String => MergeStrategy = {
    case x if Assembly.isConfigFile(x) =>
      MergeStrategy.concat
    case PathList(ps@_*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>
      MergeStrategy.rename
    case PathList("META-INF", xs@_*) =>
      xs map {
        _.toLowerCase
      } match {
        case ("application.conf" :: Nil) =>
          MergeStrategy.first
        case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
          MergeStrategy.discard
        case ps@(x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
          MergeStrategy.discard
        case "plexus" :: xs =>
          MergeStrategy.discard
        case "services" :: xs =>
          MergeStrategy.filterDistinctLines
        case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
          MergeStrategy.filterDistinctLines
        case _ => MergeStrategy.last
      }
    case _ => MergeStrategy.last
  }

  // Resolvers
  lazy val local = "Local Maven Repository" at "file:///" + Path.userHome + "/.m2/repository"
  lazy val typesafe = "Typesafe" at "http://repo.typesafe.com/typesafe/releases"
  lazy val maven2 = "maven2" at "https://repo1.maven.org/maven2"
  lazy val fwbrasil = "fwbrasil.net" at "http://repo1.maven.org/maven2"
  lazy val bintray = "bintray.com" at "http://dl.bintray.com/sbt/sbt-plugin-releases"

  // lib deps dev
  lazy val sparkSQL  = "org.apache.spark" % "spark-sql_2.11" % "1.6.0" % "provided"   // deve ser rodado dentro do spark-submit
  lazy val jtds  = "net.sourceforge.jtds" % "jtds" % "1.3.1" exclude("org.slf4j","slf4j-api") exclude ("commons-net","commons-net")  exclude("jline","jline")
  lazy val jacksonDataBind  = "com.fasterxml.jackson.core_2.11" % "jackson-databind" % "2.4.4" exclude ("commons-net","commons-net")  exclude("jline","jline")
  lazy val jline =   "jline" % "jline" % "2.12.1" exclude ("commons-logging","commons-logging")
  lazy val commonsNet =   "commons-net" % "commons-net" % "3.1"





  def commonSettings =
    buildSettings ++
      Seq(
        ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
        exportJars := true,
        resolvers ++= Seq(
          typesafe,
          maven2,
          fwbrasil,
          bintray
        )
      )



  lazy val libDep = Seq ( sparkSQL, jtds  )
  classpathTypes ~= (_ + "orbit")
  dependencyOverrides ++= Set(
    jacksonDataBind
  )




  lazy val TransparenciaTaskRunner = Project(
    id = "spark-table-transfer",
    base = file("."),
    settings = commonSettings ++ Seq(
      libraryDependencies ++= libDep)  )





}

