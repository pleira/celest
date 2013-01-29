/**
 * Copyright (C) 2012 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.libs.celest.frames

import implementations.HeliosphericCoordinateSystems._
import implementations.HeliosphericCoordinateSystems.GEI_D_MEAN
import implementations.HeliosphericCoordinateSystems.GEI_D_TRUE
import org.scalatest._
import junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import be.angelcorp.libs.celest.time.JulianDate
import be.angelcorp.libs.celest.time.timeStandard.TimeStandards
import be.angelcorp.libs.math.linear.Vector3D
import be.angelcorp.libs.celest.state.positionState.CartesianElements
import be.angelcorp.libs.celest.CelestMatchers._
import be.angelcorp.libs.celest.CelestMatchers
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class HeliosphericCoordinateSystems extends FlatSpec with ShouldMatchers {

	/**
	 * This is a numerical example for the transformations described in
	 * Fraenz/Harper, 'Heliospheric Coordinate Systems' and verifies based on Tables 8 and 9 of the paper.
	 */
	val referenceEpoch     = new JulianDate( 1996, 28, 8, 16, 46, 00, TimeStandards.TT )
	val Re                 = 6378.14E3
	val referenceDistances = Map(
		//(GEO_T(), 			Vector3D( 6.9027400, -1.6362400,  1.9166900)),
		(GEI_D_TRUE(), 		Vector3D(-5.7864335, -4.1039357,  1.9166900)),
		(GEI_D_MEAN(), 		Vector3D(-5.7864918, -4.1039136,  1.9165612)),
		(HAE_D(), 				Vector3D(-5.7864918, -3.0028771,  3.3908764)),
		(HAE_J2000(), 		Vector3D(-5.7840451, -3.0076174,  3.3908496)),
		(GEI_J2000(), 		Vector3D(-5.7840451, -4.1082375,  1.9146822))
		//(HGC_J2000(), 	Vector3D(-5.4328785,  4.1138243,  2.7493786)),
		//(HEE_D(), 			Vector3D(-4.0378470, -5.1182566,  3.3908764)),
		//(HEEQ_D(), 			Vector3D(-4.4132668, -5.1924440,  2.7496187)),
		//(HCD(), 				Vector3D(-4.3379628,  5.2555187,  2.7496187)),
		//(GSE_D(), 			Vector3D( 4.0378470,  5.1182566,  3.3908764)),
		//(GSM_D(), 			Vector3D( 4.0378470,  6.0071917,  1.2681645)),
		//(SM_D(), 				Vector3D( 3.3601371,  6.0071917,  2.5733108)),
		//(MAG_D(), 			Vector3D( 3.3344557,  6.0215108,  2.5732497)),
		//(HGRTN_E(), 		Vector3D( 4.0360303,  5.1931904, -3.2771992))
	)

	"GEI_J2000" should "transform to GEI_D_MEAN" in {
		val pos_calc = transform_GEI_J2000_To_GEI_D_MEAN.getTransform( referenceEpoch ).transform(
			new CartesianElements( referenceDistances.get( GEI_J2000() ).get * Re, Vector3D.ZERO )
		)
		val pos_true = referenceDistances.get( GEI_D_MEAN() ).get
		assert( pos_true.equals(pos_calc.getR / Re, 5E-3) )
	}

	"GEI_D_MEAN" should "transform to GEI_D_TRUE" in {
		val pos_calc = transform_GEI_D_MEAN_To_GEI_D_TRUE.getTransform( referenceEpoch ).transform(
			new CartesianElements( referenceDistances.get( GEI_D_MEAN() ).get * Re, Vector3D.ZERO )
		)
		val pos_true = referenceDistances.get( GEI_D_TRUE() ).get
		assert( pos_true.equals(pos_calc.getR / Re, 1E-3 * Re) )
	}

	"GEI_J2000" should "transform to HAE_J2000" in {
		val pos_calc = transform_GEI_J2000_To_HAE_J2000.getTransform( referenceEpoch ).transform(
			new CartesianElements( referenceDistances.get( GEI_J2000() ).get * Re, Vector3D.ZERO )
		)
		val pos_true = referenceDistances.get( HAE_J2000() ).get
		assert( pos_true.equals(pos_calc.getR / Re, 1E-6 * Re) )
	}

	"HAE_J2000" should "transform to HAE_D" in {
		val pos_calc = transform_HAE_J2000_To_HAE_D.getTransform( referenceEpoch ).transform(
			new CartesianElements( referenceDistances.get( HAE_J2000() ).get * Re, Vector3D.ZERO )
		)
		val pos_true = referenceDistances.get( HAE_D() ).get
		assert( pos_true.equals(pos_calc.getR / Re, 1E-3 * Re) )
	}

	"GEI_D_MEAN" should "transform to HAE_D" in {
		val pos_calc = transform_GEI_D_MEAN_To_HAE_D.getTransform( referenceEpoch ).transform(
			new CartesianElements( referenceDistances.get( GEI_D_MEAN() ).get * Re, Vector3D.ZERO )
		)
		val pos_true = referenceDistances.get( HAE_D() ).get
		assert( pos_true.equals(pos_calc.getR / Re, 1E-3 * Re) )
	}

}
