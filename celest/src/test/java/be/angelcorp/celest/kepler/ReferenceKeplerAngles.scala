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
package be.angelcorp.celest.kepler

import scala.math._
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.util.Precision
import be.angelcorp.celest.state.Keplerian
import be.angelcorp.celest.math.mod

object ReferenceKeplerAngles {

  val circularAngles = List[ArrayRealVector](
    // e, nu, M, E
    // Calculation using Fundamentals of Astrodynamics & applications matlab script: 'newtonnu', (with sincreased value for 'small')
    new ArrayRealVector(Array[Double](0.0, -3.1415926536, 3.1415926536, -3.1415926536)),
    new ArrayRealVector(Array[Double](0.0, -2.3561944902, 3.9269908170, -2.3561944902)),
    new ArrayRealVector(Array[Double](0.0, -1.5707963268, 4.7123889804, -1.5707963268)),
    new ArrayRealVector(Array[Double](0.0, -0.7853981634, 5.4977871438, -0.7853981634)),
    new ArrayRealVector(Array[Double](0.0, 0.0000000000, 0.0000000000, 0.0000000000)),
    new ArrayRealVector(Array[Double](0.0, 0.7853981634, 0.7853981634, 0.7853981634)),
    new ArrayRealVector(Array[Double](0.0, 1.5707963268, 1.5707963268, 1.5707963268)),
    new ArrayRealVector(Array[Double](0.0, 2.3561944902, 2.3561944902, 2.3561944902)),
    new ArrayRealVector(Array[Double](0.0, 3.1415926536, 3.1415926536, 3.1415926536))
  )

  val ellipticalAngles = List[ArrayRealVector](
    // e, nu, M, E
    // Calculation using Fundamentals of Astrodynamics & applications matlab script: 'newtonnu', (with sincreased value for 'small')
    new ArrayRealVector(Array[Double](0.1, -3.1415926536, 3.1415926536, -3.1415926536)),
    new ArrayRealVector(Array[Double](0.1, -2.3561944902, 4.0761607380, -2.2827342914)),
    new ArrayRealVector(Array[Double](0.1, -1.5707963268, 4.9120551453, -1.4706289056)),
    new ArrayRealVector(Array[Double](0.1, -0.7853981634, 5.6319320443, -0.7169631113)),
    new ArrayRealVector(Array[Double](0.1, 0.0000000000, 0.0000000000, 0.0000000000)),
    new ArrayRealVector(Array[Double](0.1, 0.7853981634, 0.6512532629, 0.7169631113)),
    new ArrayRealVector(Array[Double](0.1, 1.5707963268, 1.3711301619, 1.4706289056)),
    new ArrayRealVector(Array[Double](0.1, 2.3561944902, 2.2070245692, 2.2827342914)),
    new ArrayRealVector(Array[Double](0.1, 3.1415926536, 3.1415926536, 3.1415926536)),
    new ArrayRealVector(Array[Double](0.2, -3.1415926536, 3.1415926536, -3.1415926536)),
    new ArrayRealVector(Array[Double](0.2, -2.3561944902, 4.2419309133, -2.2026421312)),
    new ArrayRealVector(Array[Double](0.2, -1.5707963268, 5.1097060806, -1.3694384060)),
    new ArrayRealVector(Array[Double](0.2, -0.7853981634, 5.7523259906, -0.6522553846)),
    new ArrayRealVector(Array[Double](0.2, 0.0000000000, 0.0000000000, 0.0000000000)),
    new ArrayRealVector(Array[Double](0.2, 0.7853981634, 0.5308593166, 0.6522553846)),
    new ArrayRealVector(Array[Double](0.2, 1.5707963268, 1.1734792266, 1.3694384060)),
    new ArrayRealVector(Array[Double](0.2, 2.3561944902, 2.0412543939, 2.2026421312)),
    new ArrayRealVector(Array[Double](0.2, 3.1415926536, 3.1415926536, 3.1415926536)),
    new ArrayRealVector(Array[Double](0.3, -3.1415926536, 3.1415926536, -3.1415926536)),
    new ArrayRealVector(Array[Double](0.3, -2.3561944902, 4.4262205040, -2.1138112194)),
    new ArrayRealVector(Array[Double](0.3, -1.5707963268, 5.3032633948, -1.2661036728)),
    new ArrayRealVector(Array[Double](0.3, -0.7853981634, 5.8599789279, -0.5901527661)),
    new ArrayRealVector(Array[Double](0.3, 0.0000000000, 0.0000000000, 0.0000000000)),
    new ArrayRealVector(Array[Double](0.3, 0.7853981634, 0.4232063793, 0.5901527661)),
    new ArrayRealVector(Array[Double](0.3, 1.5707963268, 0.9799219124, 1.2661036728)),
    new ArrayRealVector(Array[Double](0.3, 2.3561944902, 1.8569648032, 2.1138112194)),
    new ArrayRealVector(Array[Double](0.3, 3.1415926536, 3.1415926536, 3.1415926536)),
    new ArrayRealVector(Array[Double](0.4, -3.1415926536, 3.1415926536, -3.1415926536)),
    new ArrayRealVector(Array[Double](0.4, -2.3561944902, 4.6313263879, -2.0133272226)),
    new ArrayRealVector(Array[Double](0.4, -1.5707963268, 5.4905118820, -1.1592794807)),
    new ArrayRealVector(Array[Double](0.4, -0.7853981634, 5.9556622951, -0.5295973782)),
    new ArrayRealVector(Array[Double](0.4, 0.0000000000, 0.0000000000, 0.0000000000)),
    new ArrayRealVector(Array[Double](0.4, 0.7853981634, 0.3275230120, 0.5295973782)),
    new ArrayRealVector(Array[Double](0.4, 1.5707963268, 0.7926734251, 1.1592794807)),
    new ArrayRealVector(Array[Double](0.4, 2.3561944902, 1.6518589193, 2.0133272226)),
    new ArrayRealVector(Array[Double](0.4, 3.1415926536, 3.1415926536, 3.1415926536)),
    new ArrayRealVector(Array[Double](0.5, -3.1415926536, 3.1415926536, -3.1415926536)),
    new ArrayRealVector(Array[Double](0.5, -2.3561944902, 4.8599063090, -1.8969240191)),
    new ArrayRealVector(Array[Double](0.5, -1.5707963268, 5.6690004579, -1.0471975512)),
    new ArrayRealVector(Array[Double](0.5, -0.7853981634, 6.0399192373, -0.4694752612)),
    new ArrayRealVector(Array[Double](0.5, 0.0000000000, 0.0000000000, 0.0000000000)),
    new ArrayRealVector(Array[Double](0.5, 0.7853981634, 0.2432660699, 0.4694752612)),
    new ArrayRealVector(Array[Double](0.5, 1.5707963268, 0.6141848493, 1.0471975512)),
    new ArrayRealVector(Array[Double](0.5, 2.3561944902, 1.4232789981, 1.8969240191)),
    new ArrayRealVector(Array[Double](0.5, 3.1415926536, 3.1415926536, 3.1415926536)),
    new ArrayRealVector(Array[Double](0.6, -3.1415926536, 3.1415926536, -3.1415926536)),
    new ArrayRealVector(Array[Double](0.6, -2.3561944902, 5.1147902016, -1.7579210263)),
    new ArrayRealVector(Array[Double](0.6, -1.5707963268, 5.8358900892, -0.9272952180)),
    new ArrayRealVector(Array[Double](0.6, -0.7853981634, 6.1130525738, -0.4084391419)),
    new ArrayRealVector(Array[Double](0.6, 0.0000000000, 0.0000000000, 0.0000000000)),
    new ArrayRealVector(Array[Double](0.6, 0.7853981634, 0.1701327334, 0.4084391419)),
    new ArrayRealVector(Array[Double](0.6, 1.5707963268, 0.4472952180, 0.9272952180)),
    new ArrayRealVector(Array[Double](0.6, 2.3561944902, 1.1683951056, 1.7579210263)),
    new ArrayRealVector(Array[Double](0.6, 3.1415926536, 3.1415926536, 3.1415926536)),
    new ArrayRealVector(Array[Double](0.7, -3.1415926536, 3.1415926536, -3.1415926536)),
    new ArrayRealVector(Array[Double](0.7, -2.3561944902, 5.3982470734, -1.5848689216)),
    new ArrayRealVector(Array[Double](0.7, -1.5707963268, 5.9876864670, -0.7953988302)),
    new ArrayRealVector(Array[Double](0.7, -0.7853981634, 6.1750733901, -0.3445591705)),
    new ArrayRealVector(Array[Double](0.7, 0.0000000000, 0.0000000000, 0.0000000000)),
    new ArrayRealVector(Array[Double](0.7, 0.7853981634, 0.1081119171, 0.3445591705)),
    new ArrayRealVector(Array[Double](0.7, 1.5707963268, 0.2954988402, 0.7953988302)),
    new ArrayRealVector(Array[Double](0.7, 2.3561944902, 0.8849382337, 1.5848689216)),
    new ArrayRealVector(Array[Double](0.7, 3.1415926536, 3.1415926536, 3.1415926536)),
    new ArrayRealVector(Array[Double](0.8, -3.1415926536, 3.1415926536, -3.1415926536)),
    new ArrayRealVector(Array[Double](0.8, -2.3561944902, 5.7094260302, -1.3552464166)),
    new ArrayRealVector(Array[Double](0.8, -1.5707963268, 6.1196841984, -0.6435011088)),
    new ArrayRealVector(Array[Double](0.8, -0.7853981634, 6.2255591484, -0.2744074161)),
    new ArrayRealVector(Array[Double](0.8, 0.0000000000, 0.0000000000, 0.0000000000)),
    new ArrayRealVector(Array[Double](0.8, 0.7853981634, 0.0576261588, 0.2744074161)),
    new ArrayRealVector(Array[Double](0.8, 1.5707963268, 0.1635011088, 0.6435011088)),
    new ArrayRealVector(Array[Double](0.8, 2.3561944902, 0.5737592770, 1.3552464166)),
    new ArrayRealVector(Array[Double](0.8, 3.1415926536, 3.1415926536, 3.1415926536)),
    new ArrayRealVector(Array[Double](0.9, -3.1415926536, 3.1415926536, -3.1415926536)),
    new ArrayRealVector(Array[Double](0.9, -2.3561944902, 6.0344980120, -1.0116016436)),
    new ArrayRealVector(Array[Double](0.9, -1.5707963268, 6.2244594003, -0.4510268118)),
    new ArrayRealVector(Array[Double](0.9, -0.7853981634, 6.2632181052, -0.1894852237)),
    new ArrayRealVector(Array[Double](0.9, 0.0000000000, 0.0000000000, 0.0000000000)),
    new ArrayRealVector(Array[Double](0.9, 0.7853981634, 0.0199672020, 0.1894852237)),
    new ArrayRealVector(Array[Double](0.9, 1.5707963268, 0.0587259069, 0.4510268118)),
    new ArrayRealVector(Array[Double](0.9, 2.3561944902, 0.2486872952, 1.0116016436)),
    new ArrayRealVector(Array[Double](0.9, 3.1415926536, 3.1415926536, 3.1415926536))
  )

  val parabolicAngles = List[ArrayRealVector](
    // e, nu, M, B
    // Calculation using Fundamentals of Astrodynamics & applications matlab script: 'newtonnu', (with sincreased value for 'small')
    new ArrayRealVector(Array[Double](1.0, -3.1415926536, Double.PositiveInfinity, Double.PositiveInfinity)),
    new ArrayRealVector(Array[Double](1.0, -2.3561944902, -7.1045694997, -2.4142135624)),
    new ArrayRealVector(Array[Double](1.0, -1.5707963268, -1.3333333333, -1.0000000000)),
    new ArrayRealVector(Array[Double](1.0, -0.7853981634, -0.4379028330, -0.4142135624)),
    new ArrayRealVector(Array[Double](1.0, 0.0000000000, 0.0000000000, 0.0000000000)),
    new ArrayRealVector(Array[Double](1.0, 0.7853981634, 0.4379028330, 0.4142135624)),
    new ArrayRealVector(Array[Double](1.0, 1.5707963268, 1.3333333333, 1.0000000000)),
    new ArrayRealVector(Array[Double](1.0, 2.3561944902, 7.1045694997, 2.4142135624)),
    new ArrayRealVector(Array[Double](1.0, 3.1415926536, Double.PositiveInfinity, Double.PositiveInfinity))
  )

  val hyperbolicAngles = List[ArrayRealVector](
    // e, nu, M, H
    // Calculation using Fundamentals of Astrodynamics & applications matlab script: 'newtonm', (with sincreased value for 'small')
    new ArrayRealVector(Array[Double](1.1, -2.620287384013, -3.141592653590, -2.302404556848)),
    new ArrayRealVector(Array[Double](1.1, -2.598357906819, -2.356194490192, -2.108984340120)),
    new ArrayRealVector(Array[Double](1.1, -2.559970342379, -1.570796326795, -1.853619303090)),
    new ArrayRealVector(Array[Double](1.1, -2.468816705341, -0.785398163397, -1.463234368718)),
    new ArrayRealVector(Array[Double](1.1, +0.000000000000, +0.000000000000, +0.000000000000)),
    new ArrayRealVector(Array[Double](1.1, +2.468816705341, +0.785398163397, +1.463234368718)),
    new ArrayRealVector(Array[Double](1.1, +2.559970342379, +1.570796326795, +1.853619303090)),
    new ArrayRealVector(Array[Double](1.1, +2.598357906819, +2.356194490192, +2.108984340120)),
    new ArrayRealVector(Array[Double](1.1, +2.620287384013, +3.141592653590, +2.302404556848)),
    new ArrayRealVector(Array[Double](1.2, -2.420922895272, -3.141592653590, -2.198451416639)),
    new ArrayRealVector(Array[Double](1.2, -2.387879887842, -2.356194490192, -2.001126500588)),
    new ArrayRealVector(Array[Double](1.2, -2.329348382077, -1.570796326795, -1.739089097817)),
    new ArrayRealVector(Array[Double](1.2, -2.186929257038, -0.785398163397, -1.333813546809)),
    new ArrayRealVector(Array[Double](1.2, +0.000000000000, +0.000000000000, +0.000000000001)),
    new ArrayRealVector(Array[Double](1.2, +2.186929257038, +0.785398163397, +1.333813546809)),
    new ArrayRealVector(Array[Double](1.2, +2.329348382077, +1.570796326795, +1.739089097817)),
    new ArrayRealVector(Array[Double](1.2, +2.387879887842, +2.356194490192, +2.001126500588)),
    new ArrayRealVector(Array[Double](1.2, +2.420922895272, +3.141592653590, +2.198451416639)),
    new ArrayRealVector(Array[Double](1.3, -2.276660844617, -3.141592653590, -2.102998300211)),
    new ArrayRealVector(Array[Double](1.3, -2.233806171912, -2.356194490192, -1.902199334811)),
    new ArrayRealVector(Array[Double](1.3, -2.157189399550, -1.570796326795, -1.634336398043)),
    new ArrayRealVector(Array[Double](1.3, -1.967769209033, -0.785398163397, -1.216853991455)),
    new ArrayRealVector(Array[Double](1.3, -0.000000000000, +0.000000000000, +0.000000000000)),
    new ArrayRealVector(Array[Double](1.3, +1.967769209033, +0.785398163397, +1.216853991455)),
    new ArrayRealVector(Array[Double](1.3, +2.157189399550, +1.570796326795, +1.634336398043)),
    new ArrayRealVector(Array[Double](1.3, +2.233806171912, +2.356194490192, +1.902199334811)),
    new ArrayRealVector(Array[Double](1.3, +2.276660844617, +3.141592653590, +2.102998300211)),
    new ArrayRealVector(Array[Double](1.4, -2.160844328471, -3.141592653590, -2.014863650658)),
    new ArrayRealVector(Array[Double](1.4, -2.108742046955, -2.356194490192, -1.811013318414)),
    new ArrayRealVector(Array[Double](1.4, -2.014976562916, -1.570796326795, -1.538196833103)),
    new ArrayRealVector(Array[Double](1.4, -1.781542165902, -0.785398163397, -1.111503178776)),
    new ArrayRealVector(Array[Double](1.4, +0.000000000000, +0.000000000000, +0.000000000000)),
    new ArrayRealVector(Array[Double](1.4, +1.781542165902, +0.785398163397, +1.111503178776)),
    new ArrayRealVector(Array[Double](1.4, +2.014976562916, +1.570796326795, +1.538196833103)),
    new ArrayRealVector(Array[Double](1.4, +2.108742046955, +2.356194490192, +1.811013318414)),
    new ArrayRealVector(Array[Double](1.4, +2.160844328471, +3.141592653590, +2.014863650659)),
    new ArrayRealVector(Array[Double](1.5, -2.062916599555, -3.141592653590, -1.933109602011)),
    new ArrayRealVector(Array[Double](1.5, -2.001911309016, -2.356194490192, -1.726623720449)),
    new ArrayRealVector(Array[Double](1.5, -1.891692872568, -1.570796326795, -1.449742150875)),
    new ArrayRealVector(Array[Double](1.5, -1.617861458998, -0.785398163397, -1.016993449741)),
    new ArrayRealVector(Array[Double](1.5, +0.000000000000, +0.000000000000, +0.000000000000)),
    new ArrayRealVector(Array[Double](1.5, +1.617861458998, +0.785398163397, +1.016993449741)),
    new ArrayRealVector(Array[Double](1.5, +1.891692872568, +1.570796326795, +1.449742150875)),
    new ArrayRealVector(Array[Double](1.5, +2.001911309016, +2.356194490192, +1.726623720449)),
    new ArrayRealVector(Array[Double](1.5, +2.062916599555, +3.141592653590, +1.933109602011)),
    new ArrayRealVector(Array[Double](1.6, -1.977410570312, -3.141592653590, -1.856978670348)),
    new ArrayRealVector(Array[Double](1.6, -1.907778240270, -2.356194490192, -1.648266018621)),
    new ArrayRealVector(Array[Double](1.6, -1.781812276135, -1.570796326795, -1.368212525929)),
    new ArrayRealVector(Array[Double](1.6, -1.472142509564, -0.785398163397, -0.932552173309)),
    new ArrayRealVector(Array[Double](1.6, +0.000000000000, +0.000000000000, +0.000000000000)),
    new ArrayRealVector(Array[Double](1.6, +1.472142509564, +0.785398163397, +0.932552173309)),
    new ArrayRealVector(Array[Double](1.6, +1.781812276135, +1.570796326795, +1.368212525929)),
    new ArrayRealVector(Array[Double](1.6, +1.907778240270, +2.356194490192, +1.648266018621)),
    new ArrayRealVector(Array[Double](1.6, +1.977410570312, +3.141592653590, +1.856978670348)),
    new ArrayRealVector(Array[Double](1.7, -1.901080728229, -3.141592653590, -1.785849618069)),
    new ArrayRealVector(Array[Double](1.7, -1.823088504481, -2.356194490192, -1.575310882892)),
    new ArrayRealVector(Array[Double](1.7, -1.682178907972, -1.570796326795, -1.292969754471)),
    new ArrayRealVector(Array[Double](1.7, -1.341985410542, -0.785398163397, -0.857364481458)),
    new ArrayRealVector(Array[Double](1.7, +0.000000000000, +0.000000000000, +0.000000000000)),
    new ArrayRealVector(Array[Double](1.7, +1.341985410542, +0.785398163397, +0.857364481458)),
    new ArrayRealVector(Array[Double](1.7, +1.682178907972, +1.570796326795, +1.292969754471)),
    new ArrayRealVector(Array[Double](1.7, +1.823088504481, +2.356194490192, +1.575310882892)),
    new ArrayRealVector(Array[Double](1.7, +1.901080728229, +3.141592653590, +1.785849618069)),
    new ArrayRealVector(Array[Double](1.8, -1.831832401525, -3.141592653590, -1.719205928406)),
    new ArrayRealVector(Array[Double](1.8, -1.745765387974, -2.356194490192, -1.507232075888)),
    new ArrayRealVector(Array[Double](1.8, -1.590840498544, -1.570796326795, -1.223464642108)),
    new ArrayRealVector(Array[Double](1.8, -1.225825286634, -0.785398163397, -0.790575942826)),
    new ArrayRealVector(Array[Double](1.8, +0.000000000000, +0.000000000000, +0.000000000000)),
    new ArrayRealVector(Array[Double](1.8, +1.225825286634, +0.785398163397, +0.790575942826)),
    new ArrayRealVector(Array[Double](1.8, +1.590840498544, +1.570796326795, +1.223464642108)),
    new ArrayRealVector(Array[Double](1.8, +1.745765387974, +2.356194490192, +1.507232075888)),
    new ArrayRealVector(Array[Double](1.8, +1.831832401525, +3.141592653590, +1.719205928406)),
    new ArrayRealVector(Array[Double](1.9, -1.768235007995, -3.141592653590, -1.656612811525)),
    new ArrayRealVector(Array[Double](1.9, -1.674408070898, -2.356194490192, -1.443583131541)),
    new ArrayRealVector(Array[Double](1.9, -1.506515986718, -1.570796326795, -1.159214320622)),
    new ArrayRealVector(Array[Double](1.9, -1.122379769679, -0.785398163397, -0.731318142901)),
    new ArrayRealVector(Array[Double](1.9, +0.000000000000, +0.000000000000, +0.000000000000)),
    new ArrayRealVector(Array[Double](1.9, +1.122379769679, +0.785398163397, +0.731318142901)),
    new ArrayRealVector(Array[Double](1.9, +1.506515986718, +1.570796326795, +1.159214320622)),
    new ArrayRealVector(Array[Double](1.9, +1.674408070898, +2.356194490192, +1.443583131541)),
    new ArrayRealVector(Array[Double](1.9, +1.768235007995, +3.141592653590, +1.656612811525)),
    new ArrayRealVector(Array[Double](2.0, -1.709270694164, -3.141592653590, -1.597700127280)),
    new ArrayRealVector(Array[Double](2.0, -1.608032374118, -2.356194490192, -1.383980151397)),
    new ArrayRealVector(Array[Double](2.0, -1.428319888970, -1.570796326795, -1.099786621163)),
    new ArrayRealVector(Array[Double](2.0, -1.030442880989, -0.785398163397, -0.678741138202)),
    new ArrayRealVector(Array[Double](2.0, +0.000000000000, +0.000000000000, +0.000000000000)),
    new ArrayRealVector(Array[Double](2.0, +1.030442880989, +0.785398163397, +0.678741138202)),
    new ArrayRealVector(Array[Double](2.0, +1.428319888970, +1.570796326795, +1.099786621163)),
    new ArrayRealVector(Array[Double](2.0, +1.608032374118, +2.356194490192, +1.383980151397)),
    new ArrayRealVector(Array[Double](2.0, +1.709270694164, +3.141592653590, +1.597700127280))
  )

  val circularEllipticalAngels = circularAngles ::: ellipticalAngles
  val all = circularAngles ::: ellipticalAngles ::: parabolicAngles ::: hyperbolicAngles

  def checkEqual(v: ArrayRealVector, true_value: Double, computed_value: Double): Boolean = {
    if (computed_value.isNaN && true_value.isNaN) {
      true; // Difference in definition for angles that are not reachable
    } else if (true_value.isInfinite) {
      // Accuracy breaks down as slight angles generate huge differences in anomaly
      abs(computed_value) > 1E10
    } else if (abs(true_value) < 10) {
      abs(mod(computed_value - true_value + Pi, 2 * Pi) - Pi) < 1E-9
    } else {
      Precision.equals(true_value, computed_value, abs(true_value * 1E-9))
    }
  }

  def checkEqualAnomaly(v: ArrayRealVector, computedValue: Double) =
    checkEqual(v, v.getEntry(3), computedValue)

  def checkEqualTrueAnomaly(v: ArrayRealVector, computedValue: Double) =
    checkEqual(v, v.getEntry(1), computedValue)

  def createKeplerElements(v: ArrayRealVector) =
    new Keplerian(Double.NaN, v.getEntry(0), Double.NaN, Double.NaN, Double.NaN, v.getEntry(2), null)

}
