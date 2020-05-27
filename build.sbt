name := "velocypack-module-scala"
organization := "com.arangodb"
homepage := Some(new URL("https://github.com/arangodb/velocypack"))
licenses += "Apache 2" -> url(s"http://www.apache.org/licenses/LICENSE-2.0.txt")
scmInfo := Some(ScmInfo(url(s"https://github.com/arangodb/velocypack.git"), s"git@github.com:arangodb/velocypack.git"))


scalaVersion := "2.12.11"
scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature")

crossScalaVersions := Seq("2.12.11", "2.13.2")

libraryDependencies ++= Seq(
  "com.arangodb"            % "velocypack"              % "2.1.1",
  "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6",
  "org.scalatest"          %% "scalatest"               % "3.1.2"  % "test",
  "junit"                   % "junit"                   % "4.12"   % "test",
  "org.hamcrest"            % "hamcrest-all"            % "1.3"    % "test"
)

testOptions in Test += {
  val rel = scalaVersion.value.split("[.]").take(2).mkString(".")
  Tests.Argument(
    "-oDF", // -oW to remove colors
    "-u", s"target/junitresults/scala-$rel/"
  )
}

