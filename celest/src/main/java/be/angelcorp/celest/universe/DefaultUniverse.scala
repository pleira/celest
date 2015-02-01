/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.angelcorp.celest.universe

import javax.inject.Singleton
import com.google.inject._
import org.eclipse.aether.repository.RemoteRepository
import be.angelcorp.celest.universe.modules.DefaultEarthOrientationData

class DefaultUniverse extends Universe {

  def configurationModules: Seq[Module] = Seq(
    new modules.DefaultAether,
    new modules.DefaultFrames,
    new modules.DefaultTime,
    new modules.DefaultJplEphemeris(430, "gov.nasa.jpl.ssd.pub.eph.planets.linux", "de430"),
    new modules.JplEphemerisBodies,
    DefaultEarthOrientationData.module,
    repositoriesModule
  )

  def repositoriesModule = new AbstractModule {
    def configure() {}

    @Provides
    @Singleton
    def provideRemoteRepositories = Seq(
      new RemoteRepository.Builder("resources", "default", "http://angelcorp.be/celest/resources").build()
    )
  }

  val injector = new DefaultUniverseBuilder(configurationModules).result.injector

}
