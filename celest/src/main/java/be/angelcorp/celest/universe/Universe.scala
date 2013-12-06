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

import be.angelcorp.celest.frameGraph.ReferenceFrameGraph
import com.google.inject.spi.TypeConverterBinding
import com.google.inject._
import java.lang.annotation.Annotation

/**
 * Contains all the context information regarding a simulation, such as the reference frame/time data.
 */
trait Universe extends Injector {

  /** Reference frame graph */
  def frames: ReferenceFrameGraph

  /** Dependency injector */
  def injector: Injector

  override def injectMembers(instance: AnyRef) =
    injector.injectMembers(instance)

  override def getMembersInjector[T](typeLiteral: TypeLiteral[T]): MembersInjector[T] =
    injector.getMembersInjector(typeLiteral)

  override def getMembersInjector[T](`type`: Class[T]): MembersInjector[T] =
    injector.getMembersInjector(`type`)

  override def getBindings: java.util.Map[Key[_], Binding[_]] =
    injector.getBindings

  override def getAllBindings: java.util.Map[Key[_], Binding[_]] =
    injector.getAllBindings

  override def getBinding[T](key: Key[T]): Binding[T] =
    injector.getBinding(key)

  override def getBinding[T](`type`: Class[T]): Binding[T] =
    injector.getBinding(`type`)

  override def getExistingBinding[T](key: Key[T]): Binding[T] =
    injector.getExistingBinding(key)

  override def findBindingsByType[T](`type`: TypeLiteral[T]): java.util.List[Binding[T]] =
    injector.findBindingsByType(`type`)

  override def getProvider[T](key: Key[T]): Provider[T] =
    injector.getProvider(key)

  override def getProvider[T](`type`: Class[T]): Provider[T] =
    injector.getProvider(`type`)

  override def getInstance[T](key: Key[T]): T =
    injector.getInstance(key)

  override def getInstance[T](`type`: Class[T]): T =
    injector.getInstance(`type`)

  /**
   * Returns the appropriate instance for the given injection type annotated with the given annotation.
   * When feasible, avoid using this method, in favor of having Guice inject your dependencies ahead of time.
   */
  def getInstance[T](`type`: Class[T], annotation: Annotation): T =
    injector.getInstance(Key.get(`type`, annotation))

  /**
   * Returns the appropriate instance for the given injection type annotated with the given annotation class.
   * When feasible, avoid using this method, in favor of having Guice inject your dependencies ahead of time.
   */
  def getInstance[T](`type`: Class[T], annotation: Class[_ <: Annotation]): T =
    injector.getInstance(Key.get(`type`, annotation))

  override def getParent: Injector =
    injector.getParent

  override def createChildInjector(modules: java.lang.Iterable[_ <: Module]): Injector =
    injector.createChildInjector(modules)

  override def createChildInjector(modules: Module*): Injector =
    injector.createChildInjector(modules: _*)

  override def getScopeBindings: java.util.Map[Class[_ <: Annotation], Scope] =
    injector.getScopeBindings

  override def getTypeConverterBindings: java.util.Set[TypeConverterBinding] =
    injector.getTypeConverterBindings

}
