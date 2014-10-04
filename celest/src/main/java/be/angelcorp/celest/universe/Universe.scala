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

import java.lang.annotation.Annotation

import com.google.inject._
import net.codingwell.scalaguice.KeyExtensions.ScalaTypeLiteral
import net.codingwell.scalaguice._

/**
 * Contains all the context information regarding a simulation, such as the reference frame/time data.
 *
 * This trait is based on the dependency injection technique, as implemented by Guide. For more information see
 * http://code.google.com/p/google-guice.
 */
trait Universe {

  /** Dependency injector */
  def injector: Injector

  /**
   * Injects dependencies into the fields and methods of `instance`. Ignores the presence or absence of an injectable constructor.
   * <p>
   * Whenever Guice creates an instance, it performs this injection automatically (after first performing constructor
   * injection), so if you're able to let Guice create all your objects for you, you'll never need to use this method.
   * </p>
   * @param instance to inject members on
   * @see com.google.inject.Injector#injectMembers
   */
  def injectMembers(instance: AnyRef) =
    injector.injectMembers(instance)

  /**
   * Returns the provider used to obtain instances for the given injection type.
   * When feasible, avoid using this method, in favor of having Guice inject your dependencies ahead of time.
   * @see com.google.inject.Injector#getProvider
   */
  def provider[T: Manifest]: Provider[T] =
    injector.getProvider(typeLiteral[T].toKey)

  /**
   * Returns the provider used to obtain instances for the given injection key.
   * When feasible, avoid using this method, in favor of having Guice inject your dependencies ahead of time.
   * @see com.google.inject.Injector#getProvider
   */
  def provider[T](key: Key[T]): Provider[T] =
    injector.getProvider(key)

  /**
   * Returns the appropriate instance for the given injection type.
   * When feasible, avoid using this method, in favor of having Guice inject your dependencies ahead of time.
   * @see com.google.inject.Injector#getInstance
   */
  def instance[T: Manifest]: T =
    injector.getInstance(typeLiteral[T].toKey)

  /**
   * Returns the appropriate instance for the given injection key.
   * When feasible, avoid using this method, in favor of having Guice inject your dependencies ahead of time.
   * @see com.google.inject.Injector#getInstance
   */
  def instance[T](key: Key[T]): T =
    injector.getInstance(key)

  /**
   * Returns the appropriate instance for the given injection type annotated with the given annotation.
   * When feasible, avoid using this method, in favor of having Guice inject your dependencies ahead of time.
   * @see com.google.inject.Injector#getInstance
   */
  def instance[T: Manifest](annotation: Annotation): T =
    injector.getInstance(typeLiteral[T].annotatedWith(annotation))

  /**
   * Returns the appropriate instance for the given injection type annotated with the given annotation class.
   * When feasible, avoid using this method, in favor of having Guice inject your dependencies ahead of time.
   * @see com.google.inject.Injector#getInstance
   */
  def instance[T: Manifest, A <: java.lang.annotation.Annotation : Manifest]: T =
    injector.getInstance( new ScalaTypeLiteral(typeLiteral[T]).annotatedWith[A])

}
