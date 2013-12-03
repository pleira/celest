package be.angelcorp.celest.console

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop


/**
 * Run the celest library in a scala console
 */
object Console extends App {
  val settings = new Settings
  settings.usejavacp.value = true
  settings.deprecation.value = true

  new ConsoleLoop().process(settings)
}

class ConsoleLoop extends ILoop {

  addThunk {
    val stream = this.getClass.getClassLoader.getResourceAsStream("init.scala")
    intp.beQuietDuring {
      for (line <- scala.io.Source.fromInputStream(stream).getLines())
        intp.interpret(line)
    }
  }

  override def printWelcome() {
    echo(
      """Celest console
        |------------------------------
      """.stripMargin
    )
  }

}