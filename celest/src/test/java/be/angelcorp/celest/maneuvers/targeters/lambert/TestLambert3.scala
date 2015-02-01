package be.angelcorp.celest.maneuvers.targeters.lambert

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * All reference values computed with:
 * Robust solver for Lambert's orbital-boundary value problem [matlab package] by Rody Oldenhuis
 */
class TestLambert3 extends FlatSpec with ShouldMatchers {

  /**
   * Mathematica:
   * <pre>
   * f[u_] := (2 (ArcSin[Sqrt[u]] - Sqrt[u] Sqrt[1 - u]))/u^(3/2)
   * y = {-0.50, -0.40, -0.35, -0.30, -0.25, -0.20, -0.15, -0.10, -0.05, -0.01, 0.01, 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40};
   * Re[f /@ y] // N // InputForm
   * </pre>
   *
   * Note the Re[] is used to generate a clean output, meaning it gets rid of the " + 0.*i " part of the solution
   */
  "Sigma" should "be computed corectly" in {
    // The sequence approximation is most accurate around y=0
    val y = List(-0.50, -0.40, -0.35, -0.30, -0.25, -0.20, -0.15, -0.10, -0.05, -0.01, 0.01, 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40)
    val sigma = List(1.1740600477555074, 1.2006860891065543, 1.214808913632454, 1.2295256780898363, 1.2448827090455037, 1.260931820471868, 1.277731209586127, 1.2953465431659816, 1.3138522851969212, 1.32935462400266, 1.3373549017955955, 1.3538870548220183, 1.3756258452955747, 1.3986803809252573, 1.4232038033112997, 1.449377179297274, 1.4774167347425755, 1.5075836065550865, 1.5401972516486249)
    y.zip(sigma).foreach(entry => {
      Lambert3.sigmax(entry._1) should be(entry._2 plusOrMinus (entry._2 * 1E-10))
    })
  }

  /**
   * Mathematica:
   * <pre>
   * f[u_] := (2 (ArcSin[Sqrt[u]] - Sqrt[u] Sqrt[1 - u]))/u^(3/2)
   * y = {-0.50, -0.40, -0.35, -0.30, -0.25, -0.20, -0.15, -0.10, -0.05, -0.01, 0.01, 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40, 0.50};
   * Re[D[f[u], u] //. u -> # & /@ y] // N // InputForm
   * </pre>
   * Note the Re[] is used to generate a clean output, meaning it gets rid of the " + 0.*i " part of the solution
   */
  "d(Sigma) / dx" should "be computed corectly" in {
    // The sequence approximation is most accurate around y=0
    val y = List(-0.50, -0.40, -0.35, -0.30, -0.25, -0.20, -0.15, -0.10, -0.05, -0.01, 0.01, 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40, 0.50)
    val dsigma = List(0.25619381955561993, 0.2768015605069962, 0.2882498251455443, 0.3005749284023169, 0.3138787262736944, 0.32827936178624384, 0.34391465265707666, 0.36094636257785595, 0.379565637966202, 0.39575555840232823, 0.40432778250300316, 0.42252243874573026, 0.4474633883555974, 0.4752267119911693, 0.5063113626642082, 0.5413412312503709, 0.5811070551830788, 0.6266265198615377, 0.6792325499966845, 0.8134868092558509)
    y.zip(dsigma).foreach(entry => {
      Lambert3.dsigdx(entry._1) should be(entry._2 plusOrMinus (entry._2 * 1E-8))
    })
  }

  /**
   * Mathematica:
   * <pre>
   * f[u_] := (2 (ArcSin[Sqrt[u]] - Sqrt[u] Sqrt[1 - u]))/u^(3/2)
   * y = {-0.40, -0.35, -0.30, -0.25, -0.20, -0.15, -0.10, -0.05, -0.01, 0.01, 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40}
   * Re[D[D[f[u], u], u] //. u -> # & /@ y] // N // InputForm
   * </pre>
   * Note the Re[] is used to generate a clean output, meaning it gets rid of the " + 0.*i " part of the solution
   */
  "d2(Sigma) / dx2" should "be computed corectly" in {
    // The sequence approximation is most accurate around y=0
    val y = List(-0.40, -0.35, -0.30, -0.25, -0.20, -0.15, -0.10, -0.05, -0.01, 0.01, 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40)
    val d2sigma = List(0.22080572686780542, 0.23741840008960224, 0.2559243538474334, 0.2766202515371958, 0.2998631507644234, 0.32608590231183143, 0.355817344031891, 0.3897090802418006, 0.4203559165543993, 0.4370256125184824, 0.4734223171365102, 0.5255547732151626, 0.5866315773494932, 0.6588203963842716, 0.7449905588522938, 0.8490060131619401, 0.9761725323827974, 1.1339400989199078)
    y.zip(d2sigma).foreach(entry => {
      Lambert3.d2sigdx2(entry._1) should be(entry._2 plusOrMinus (entry._2 * 1E-8))
    })
  }

  /**
   * Mathematica:
   * <pre>
   * f[u_] := (2 (ArcSin[Sqrt[u]] - Sqrt[u] Sqrt[1 - u]))/u^(3/2)
   * y = {-0.40, -0.35, -0.30, -0.25, -0.20, -0.15, -0.10, -0.05, -0.01, 0.01, 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40}
   * Re[D[D[D[f[u], u], u], u] //. u -> # & /@ y] // N // InputForm
   * </pre>
   * Note the Re[] is used to generate a clean output, meaning it gets rid of the " + 0.*i " part of the solution
   */
  "d3(Sigma) / dx3" should "be computed corectly" in {
    // The sequence approximation is most accurate around y=0
    val y = List(-0.40, -0.35, -0.30, -0.25, -0.20, -0.15, -0.10, -0.05, -0.01, 0.01, 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40)
    val d3sigma = List(0.31504579619942774, 0.3502851982531183, 0.390937917252586, 0.4380831080811731, 0.49306904892318926, 0.5575949991953166, 0.6338228769163834, 0.7245315910549834, 0.809916865080595, 0.8576586991548538, 0.9649813601281494, 1.1258154076422215, 1.324418292201699, 1.5726038689390407, 1.8869379187799495, 2.291140145774534, 2.819989089278579, 3.5258829754487238)
    y.zip(d3sigma).foreach(entry => {
      Lambert3.d3sigdx3(entry._1) should be(entry._2 plusOrMinus (entry._2 * 1E-7))
    })
  }


}
