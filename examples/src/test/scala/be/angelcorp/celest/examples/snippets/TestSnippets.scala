package be.angelcorp.celest.examples.snippets

import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import org.reflections.util.{ClasspathHelper, ConfigurationBuilder}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._
import scala.tools.nsc.GenericRunnerSettings
import scala.tools.nsc.interpreter.Results

class TestSnippets extends FlatSpec with Matchers {
  val flusher = new java.io.PrintWriter(System.out)

  val reflections = new Reflections(new ConfigurationBuilder()
    .setUrls(ClasspathHelper.forPackage("snippets"))
    .setScanners(new ResourcesScanner())
  )
  val snippets = reflections.getResources( """.*\.scala""".r.pattern)

  snippets.asScala.map(snippet => snippet should "run without exception" in {
    val interpreter = {
      val settings = new GenericRunnerSettings(println)
      settings.usejavacp.value = true
      new scala.tools.nsc.interpreter.IMain(settings, flusher)
    }

    val source = getClass.getClassLoader.getResource(snippet)
    interpreter.beQuietDuring {
      interpreter.interpret(s":load $source" )
    } match {
      case Results.Error => fail(s"Error while running script '$snippet'")
      case _ =>
    }
  })

}