package be.angelcorp.libs.celest_examples.snippets

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, FlatSpec}
import scala.collection.JavaConverters._
import scala.tools.nsc.GenericRunnerSettings
import org.reflections.{ Reflections}
import java.nio.file.Paths
import scala.io.Source
import scala.tools.nsc.interpreter.Results
import com.google.common.base.Predicate
import org.reflections.util.{ClasspathHelper, ConfigurationBuilder}
import org.reflections.scanners.ResourcesScanner

@RunWith(classOf[JUnitRunner])
class TestSnippets  extends FlatSpec with Matchers {
  val flusher = new java.io.PrintWriter(System.out)

  val reflections = new Reflections(new ConfigurationBuilder()
      .setUrls(ClasspathHelper.forPackage("snippets"))
      .setScanners(new ResourcesScanner())
  )
  val snippets = reflections.getResources( """.*\.scala""".r.pattern )

  snippets.asScala.map( snippet => snippet should "run without exception" in {
    val interpreter = {
      val settings = new GenericRunnerSettings( println )
      settings.usejavacp.value = true
      new scala.tools.nsc.interpreter.IMain(settings, flusher)
    }

    val source = Source.fromInputStream( getClass.getClassLoader.getResourceAsStream(snippet) ).getLines().mkString("\n")
    interpreter.beQuietDuring {
      interpreter.interpret( source )
    } match {
      case Results.Error => fail( s"Error while running script '$snippet'" )
      case _ =>
    }
  } )

}