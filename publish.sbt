pomIncludeRepository := { _ => false }

releaseCrossBuild := true
releasePublishArtifactsAction := PgpKeys.publishSigned.value
publishMavenStyle := true
publishArtifact in Test := false
publishTo := Some(if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging)

PgpKeys.useGpg in Global := true      // workaround with pgp and sbt 1.2.x
pgpSecretRing := pgpPublicRing.value  // workaround with pgp and sbt 1.2.x

pomExtra in Global := {
  <developers>
    <developer>
      <id>mpv1989</id>
      <name>Mark Vollmary</name>
      <url>https://github.com/mpv1989</url>
    </developer>
  </developers>
}


import ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    //runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    releaseStepCommand("sonatypeReleaseAll"),
    pushChanges
  )
