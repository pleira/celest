package be.angelcorp.celest.frameGraph

import javax.inject.Singleton
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, FlatSpec}
import com.google.inject.Module
import be.angelcorp.celest.data._
import be.angelcorp.celest.time.{JulianDate, Epoch}
import be.angelcorp.celest.time.timeStandard.TimeStandards._
import be.angelcorp.celest.universe.{modules, DefaultUniverse}
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.frameGraph.frames._
import be.angelcorp.libs.math.linear.Vector3D
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.physics.Units._

/**
 * Test IAU reference systems, based on:
 * D. Vallado et al. ,<b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006<br/>
 */
@RunWith(classOf[JUnitRunner])
class TestIAUReferenceSystems extends FlatSpec with Matchers {

  def makeUniverse(eop: EarthOrientationDataEntry) = new DefaultUniverse {
    override def configurationModules: Iterable[Module] = Seq(
      new modules.DefaultAether,
      new modules.DefaultFrames {
        override def configureData() {
          bind[EarthOrientationData].toInstance(new EarthOrientationData(null, new TimeStandard {
            def offsetToTT(JD_this: Epoch): Double = 32 + 32.184
            def offsetFromTT(JD_tt: Epoch): Double = -offsetToTT(JD_tt)
          }){
            override def getEntry(epoch: Epoch, Δt_max: Double) = eop
          })
          bind[ExcessLengthOfDay].to[EarthOrientationData].in(classOf[Singleton])
          bind[UT1Provider].to[EarthOrientationData].in(classOf[Singleton])
          bind[PolarOrientationData].to[EarthOrientationData].in(classOf[Singleton])
        }
      },
      new modules.DefaultTime,
      new modules.DefaultJplEphemeris(430, "gov.nasa.jpl.ssd.pub.eph.planets.linux", "de430"),
      new modules.JplEphemerisBodies,
      thisUniverseModule,
      repositoriesModule
    )
  }

  "The framegraph" should "operate correctly on the default IAU frames" in {
    implicit val universe = makeUniverse(new EarthOrientationDataEntry(2004, 4, 6, 53101, arcSeconds(-0.140682), arcSeconds(0.333309), -0.439962, 0.001556, arcSeconds(-0.000199), arcSeconds(-0.000252)))
    val epoch = new JulianDate(2453101.82815474550, TT)
    val framegraph = universe.instance[ReferenceFrameGraph]

    val pv = new PosVel(
      Vector3D(-1033.4793830E3, 7901.2952754E3, 6380.3565958E3),
      Vector3D(-3.225636520E3, -2.872451450E3, 5.531924446E3),
      ITRF()
    )

    val pvTIRS = framegraph.getTransform(ITRF(), TIRF(), epoch).get.transform(pv).toPosVel
    pvTIRS.position.x should be( -1033.4750312E3   +- 1E-3)
    pvTIRS.position.y should be(  7901.3055856E3   +- 1E-3)
    pvTIRS.position.z should be(  6380.3445328E3   +- 1E-3)
    pvTIRS.velocity.x should be(    -3.225632747E3 +- 1E-3)
    pvTIRS.velocity.y should be(    -2.872442511E3 +- 1E-3)
    pvTIRS.velocity.z should be(     5.531931288E3 +- 1E-3)

    val pvERF = framegraph.getTransform(ITRF(), ERF(), epoch).get.transform(pv).toPosVel
    pvERF.position.x should be( 5094.5146278E3   +- 1E-2)
    pvERF.position.y should be( 6127.3665880E3   +- 1E-2)
    pvERF.position.z should be( 6380.3445328E3   +- 1E-2)
    pvERF.velocity.x should be(   -4.746088587E3 +- 1E-3)
    pvERF.velocity.y should be(    0.786077104E3 +- 1E-3)
    pvERF.velocity.z should be(    5.531931288E3 +- 1E-3)

    val pvMOD_corr = framegraph.getTransform(ITRF(), MOD(), epoch).get.transform(pv).toPosVel
    pvMOD_corr.position.x should be( 5094.0283745E3 +- 1E0)
    pvMOD_corr.position.y should be( 6127.8708164E3 +- 1E0)
    pvMOD_corr.position.z should be( 6380.2485164E3 +- 1E0)
    pvMOD_corr.velocity.x should be( -4.746263052E3 +- 1E-3)
    pvMOD_corr.velocity.y should be(  0.786014045E3 +- 1E-3)
    pvMOD_corr.velocity.z should be(  5.531790562E3 +- 1E-3)
  }

}
