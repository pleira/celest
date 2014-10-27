package be.angelcorp.celest.frameGraph

import javax.inject.{Inject, Provider, Singleton}

import be.angelcorp.celest.data.eop._
import be.angelcorp.celest.frameGraph.frames._
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.UTC
import be.angelcorp.celest.time.{Epoch, JulianDate}
import be.angelcorp.celest.time.timeStandard.TimeStandards._
import be.angelcorp.celest.universe.DefaultUniverseBuilder
import be.angelcorp.celest.universe.modules._
import com.google.inject.Provides
import com.google.inject.util.Providers
import net.codingwell.scalaguice.ScalaModule
import org.eclipse.aether.repository.RemoteRepository
import org.scalatest.{FlatSpec, Matchers}

/**
 * Test IAU reference systems, based on:
 * D. Vallado et al. ,<b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006<br/>
 */
class TestIAUReferenceSystems extends FlatSpec with Matchers {

  def makeUniverse(eop: EarthOrientationDataEntry) = new DefaultUniverseBuilder {
    modules += new DefaultAether
    modules += new ScalaModule {
      override def configure() {
        bind[ExcessLengthOfDay].toInstance(new ExcessLengthOfDay {
          override def lod(epoch: Epoch): Double = eop.lod
        })
        bind[UT1Provider].toInstance(new UT1Provider {
          override def UT1_UTC(jd_utc: Epoch): Double = eop.ut1_utc
        })
        bind[PoleProvider].toInstance(new PoleProvider {
          override def polarCoordinatesOn(epoch: Epoch): (Double, Double) = (eop.x, eop.y)
        })
      }
      @Provides
      def provideEarthOrientationDataProvider( @UTC utc: TimeStandard ): EarthOrientationData =
        new EarthOrientationData(utc) {
          override def getEntry(epoch: Epoch, Δt_max: Double) = eop
        }
    }
    modules += new DefaultFrames
    modules += new DefaultTime
    modules += new DefaultJplEphemeris(430, "gov.nasa.jpl.ssd.pub.eph.planets.linux", "de430")
    modules += new JplEphemerisBodies()
    modules += new ScalaModule {
      override def configure() {}
      @Provides
      @Singleton
      def provideRemoteRepositories = Seq(
        new RemoteRepository.Builder("resources", "default", "http://angelcorp.be/celest/resources").build()
      )
    }
  }.result

  "The framegraph" should "operate correctly on the default IAU frames" in {
    implicit val universe = makeUniverse(new EarthOrientationDataEntry(2004, 4, 6, 53101, arcSeconds(-0.140682), arcSeconds(0.333309), -0.439962, 0.001556, arcSeconds(-0.000199), arcSeconds(-0.000252)))
    val epoch = new JulianDate(2453101.82815474550, TT)
    val framegraph = universe.instance[ReferenceFrameGraph]

    val itrf = universe.instance[ITRS]
    val pv = new PosVel(
      Vec3(-1033.4793830E3, 7901.2952754E3, 6380.3565958E3),
      Vec3(-3.225636520E3, -2.872451450E3, 5.531924446E3),
      itrf
    )

    val pvTIRS = framegraph.getTransform(itrf, universe.instance[TIRS], epoch).get.transform(pv).toPosVel
    pvTIRS.position.x should be(-1033.4750312E3 +- 1E-3)
    pvTIRS.position.y should be(7901.3055856E3 +- 1E-3)
    pvTIRS.position.z should be(6380.3445328E3 +- 1E-3)
    pvTIRS.velocity.x should be(-3.225632747E3 +- 1E-3)
    pvTIRS.velocity.y should be(-2.872442511E3 +- 1E-3)
    pvTIRS.velocity.z should be(5.531931288E3 +- 1E-3)

    val pvERF = framegraph.getTransform(itrf, universe.instance[ERS], epoch).get.transform(pv).toPosVel
    pvERF.position.x should be(5094.5146278E3 +- 1E-2)
    pvERF.position.y should be(6127.3665880E3 +- 1E-2)
    pvERF.position.z should be(6380.3445328E3 +- 1E-2)
    pvERF.velocity.x should be(-4.746088587E3 +- 1E-3)
    pvERF.velocity.y should be(0.786077104E3 +- 1E-3)
    pvERF.velocity.z should be(5.531931288E3 +- 1E-3)

    val pvMOD_corr = framegraph.getTransform(itrf, universe.instance[MOD], epoch).get.transform(pv).toPosVel
    pvMOD_corr.position.x should be(5094.0283745E3 +- 1E0)
    pvMOD_corr.position.y should be(6127.8708164E3 +- 1E0)
    pvMOD_corr.position.z should be(6380.2485164E3 +- 1E0)
    pvMOD_corr.velocity.x should be(-4.746263052E3 +- 1E-3)
    pvMOD_corr.velocity.y should be(0.786014045E3 +- 1E-3)
    pvMOD_corr.velocity.z should be(5.531790562E3 +- 1E-3)
  }

}
