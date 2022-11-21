val scala3Version = "3.1.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "biweekly-hackery",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.apache.commons" %  "commons-text" % "1.3",
      "org.scalatest"      %% "scalatest"    % "3.2.10"   % Test,
      "org.scalatestplus"  %% "mockito-3-4"  % "3.2.10.0" % Test
    )
  )
