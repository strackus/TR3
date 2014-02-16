name := "TR3"

organization := "Stracke"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.3"

libraryDependencies += "org.scala-lang" % "scala-swing" % scalaVersion.value

scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.7", "-unchecked",
  "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint")

//scalacOptions in (Compile, doc) <++= baseDirectory.map {
//  (bd: File) => Seq[String](
//     "-sourcepath", bd.getAbsolutePath,
//     "-doc-source-url", "https://github.com/mslinn/changeMe/tree/master€{FILE_PATH}.scala"
//  )
//}

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.7", "-target", "1.7", "-g:vars")

resolvers ++= Seq(
  "Typesafe Releases"   at "http://repo.typesafe.com/typesafe/releases"
)

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")

libraryDependencies ++= Seq(
  //  "org.scalatest"           %% "scalatest"     % "2.0" % "test" withSources,
  //  "com.github.nscala-time"  %% "nscala-time"   % "0.6.0" withSources
)

logLevel := Level.Warn

// define the statements initially evaluated when entering 'console', 'console-quick', or 'console-7project'
//initialCommands := """
//                     |""".stripMargin

// Only show warnings and errors on the screen for compilations.
// This applies to both test:compile and compile and is Info by default
logLevel in compile := Level.Warn

//cancelable := true

//sublimeTransitive := true