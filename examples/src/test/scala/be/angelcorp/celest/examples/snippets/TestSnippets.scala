package be.angelcorp.celest.examples.snippets

import java.io.{BufferedReader, InputStreamReader}

import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import org.reflections.util.{ClasspathHelper, ConfigurationBuilder}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._
import scala.io.Source
import scala.tools.nsc.GenericRunnerSettings
import scala.tools.nsc.interpreter._

class TestSnippets extends FlatSpec with Matchers {
  val flusher = new java.io.PrintWriter(System.out)

  val reflections = new Reflections(new ConfigurationBuilder()
    .setUrls(ClasspathHelper.forPackage("snippets"))
    .setScanners(new ResourcesScanner())
  )
  val snippets = reflections.getResources( """.*\.sc""".r.pattern)

  snippets.asScala.map(snippet => snippet should "compile and run without exception" in {
    val interpreter = {
      val settings = new GenericRunnerSettings( err => System.err.println(err) )
      settings.usejavacp.value = true
      new scala.tools.nsc.interpreter.IMain(settings, flusher)
    }

    val source = Source.fromInputStream( getClass.getClassLoader.getResourceAsStream(snippet) )
    val lines  = source.getLines().mkString("\n")
    val script = interpreter.interpret( lines ) match {
      case Results.Success    =>
      case Results.Error      => fail( "Failed to execute script successfully: The input script was erroneous in some way." )
      case Results.Incomplete => fail( "Failed to execute script successfully: The input script was incomplete." )
    }
    interpreter.reset()
  })

}