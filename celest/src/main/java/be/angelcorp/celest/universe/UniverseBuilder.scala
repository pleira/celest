package be.angelcorp.celest.universe

import scala.collection.mutable
import com.google.inject._
import net.codingwell.scalaguice.ScalaModule
import javax.inject.Singleton

trait UniverseBuilder {
  def modules: mutable.ListBuffer[Module]

  def result: Universe
}

class DefaultUniverseBuilder(val modules: mutable.ListBuffer[Module] = mutable.ListBuffer[Module]()) extends UniverseBuilder {

  def this(modules: Seq[Module]) = this(mutable.ListBuffer[Module](modules: _*))

  modules += new ScalaModule {
    override def configure() {}

    @Provides
    @Singleton
    def universe(injector: Injector): Universe = new BasicUniverse(injector)
  }

  override def result: Universe = {
    val injector = Guice.createInjector(modules.toArray: _*)
    injector.getInstance(classOf[Universe])
  }

}

class BasicUniverse(val injector: Injector) extends Universe
