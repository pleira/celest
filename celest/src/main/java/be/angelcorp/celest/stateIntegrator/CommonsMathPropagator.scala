/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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
package be.angelcorp.celest.stateIntegrator

import org.apache.commons.math3.ode._
import org.apache.commons.math3.linear.ArrayRealVector
import be.angelcorp.celest.state._
import be.angelcorp.celest.time.{Epoch, JulianDate}
import be.angelcorp.celest.universe.Universe

class CommonsMathPropagator[Y <: IState, DY <: IStateDerivative](
                                                                  val integrator: FirstOrderIntegrator,
                                                                  val equations: IStateEquation[Y, DY])(
                                                                  implicit val universe: Universe
                                                                  ) extends IStateIntegrator[Y] {

  val cm_equations = new FirstOrderDifferentialEquations {
    override def computeDerivatives(t: Double, y: Array[Double], yDot: Array[Double]) {
      val yState = equations.createState(new ArrayRealVector(y, false))
      val dyState = equations.calculateDerivatives(new JulianDate(t), yState)

      val dyVector = dyState.toVector
      System.arraycopy(
        dyVector match {
          case vector: ArrayRealVector => vector.getDataRef
          case _ => dyVector.toArray
        },
        0,
        yDot,
        0,
        dyVector.getDimension
      )
    }

    override def getDimension = equations.getDimension
  }

  override def integrate(t0: Epoch, t: Epoch, y0: Y) = {
    // Wrap the types to commons math compatible types
    val cm_y0 = y0.toVector.toArray
    val cm_t0 = t0.jd
    val cm_t = t.jd

    // Propagate the orbit
    val cm_y = Array.ofDim[Double](cm_equations.getDimension)
    val cm_t_end = try {
      integrator.integrate(cm_equations, cm_t0, cm_y0, cm_t, cm_y)
    } catch {
      case e: Throwable => throw e
    }

    // Unwrap to libs.celst types
    equations.createState(new ArrayRealVector(cm_y, false))
  }
}
