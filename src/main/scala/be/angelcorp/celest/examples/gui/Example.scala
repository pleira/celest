package be.angelcorp.celest.examples.gui

import org.slf4j.LoggerFactory

class Example(val clazz: Class[_]) {
  val logger = LoggerFactory.getLogger(classOf[Example])

  val annotation = clazz.getAnnotation(classOf[CelestExample])

  override def toString = annotation.name()

  def invoke() {
    logger.info("Invoking example: {}", clazz.getSimpleName)
    try {
      new Thread {
        override def run() {
          clazz.newInstance()
        }
      }.start()
    } catch {
      case e: InstantiationException =>
        logger.error("Could not construct example ({}), make sure a default constructor is available", clazz, e)
      case e: IllegalAccessException =>
        logger.error("Could not access the default constructor of example: {}", clazz, e)
    }
  }

}
