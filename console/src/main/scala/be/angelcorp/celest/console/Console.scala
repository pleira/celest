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

  val filename = this.getClass.getClassLoader.getResource("init.scala")
  val cmd = ":load " + filename
  command(cmd)

  override def printWelcome() {
    echo(
      """Celest console
        |------------------------------
      """.stripMargin
    )
  }

}