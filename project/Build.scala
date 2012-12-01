import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "analyze_text"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
	"mysql" % "mysql-connector-java" % "5.1.18",
	"edu.stanford.nlp" % "stanford-corenlp" % "1.3.3",
	"edu.stanford.nlp" % "stanford-parser" % "2.0.3"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    )

}
