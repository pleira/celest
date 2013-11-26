/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.angelcorp.libs.celest.universe

import javax.inject.{Singleton, Named}
import com.google.inject._
import com.google.inject.name.Names
import org.eclipse.aether.repository.RemoteRepository
import be.angelcorp.libs.celest.time.timeStandard._

class DefaultUniverse extends Universe {

  def configurationModules: Iterable[Module] = Seq(
    new modules.DefaultAether,
    new modules.DefaultTime,
    thisUniverseModule,
    repositoriesModule
  )

  val injector = Guice.createInjector( configurationModules.toArray : _* )

  val TAI: ITimeStandard = injector.getInstance( Key.get( classOf[ITimeStandard], Names.named("TAI") ) )
  val TT:  ITimeStandard = injector.getInstance( Key.get( classOf[ITimeStandard], Names.named("TT" ) ) )
  val TDT: ITimeStandard = injector.getInstance( Key.get( classOf[ITimeStandard], Names.named("TDT") ) )
  val TCB: ITimeStandard = injector.getInstance( Key.get( classOf[ITimeStandard], Names.named("TCB") ) )
  val TCG: ITimeStandard = injector.getInstance( Key.get( classOf[ITimeStandard], Names.named("TCG") ) )
  val TDB: ITimeStandard = injector.getInstance( Key.get( classOf[ITimeStandard], Names.named("TDB") ) )
  val UTC: ITimeStandard = injector.getInstance( Key.get( classOf[ITimeStandard], Names.named("UTC") ) )
  val UT1: ITimeStandard = injector.getInstance( Key.get( classOf[ITimeStandard], Names.named("UT1") ) )

  lazy val frames = throw new UnsupportedOperationException("Operation not yet implemented")

  def thisUniverseModule = {
    val universe = this
    new AbstractModule {
      def configure() = bind( classOf[Universe] ).toInstance( universe )
    }
  }

  def repositoriesModule = new AbstractModule {
    def configure() {}
    @Provides
    @Singleton
    def provideRemoteRepositories = Seq(
      new RemoteRepository.Builder("resources", "default", "http://angelcorp.be/celest/resources").build()
    )
  }

}
