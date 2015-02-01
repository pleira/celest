package be.angelcorp.celest.universe.modules

import javax.inject.Singleton
import com.google.inject.Provides
import be.angelcorp.celest.body.Body
import be.angelcorp.celest.body.CelestialBodyAnnotations._
import be.angelcorp.celest.ephemeris.jplEphemeris.JplEphemeris
import be.angelcorp.celest.ephemeris.jplEphemeris
import be.angelcorp.celest.frameGraph.frames.ICRS
import net.codingwell.scalaguice.ScalaModule

/**
 * Modules used to bind JplEphemeris bodies to the [[be.angelcorp.celest.body.CelestialBodyAnnotations]].
 *
 * Note this Module requires that some [[be.angelcorp.celest.ephemeris.jplEphemeris.JplEphemeris]] is also configured.
 * For example use [[be.angelcorp.celest.universe.modules.DefaultJplEphemeris]].
 */
class JplEphemerisBodies extends ScalaModule {

  type F = ICRS

  @Provides
  @Mercury
  @Singleton
  def mercury(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.Mercury())

  @Provides
  @Venus
  @Singleton
  def venus(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.Venus())

  @Provides
  @Earth
  @Singleton
  def earth(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.Earth())

  @Provides
  @Moon
  @Singleton
  def moon(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.Moon())

  @Provides
  @EarthMoonBarycenter
  @Singleton
  def emb(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.EMB())

  @Provides
  @Mars
  @Singleton
  def mars(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.Mars())

  @Provides
  @Jupiter
  @Singleton
  def jupiter(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.Jupiter())

  @Provides
  @Saturn
  @Singleton
  def saturn(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.Saturn())

  @Provides
  @Uranus
  @Singleton
  def uranus(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.Uranus())

  @Provides
  @Neptune
  @Singleton
  def neptune(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.Neptune())

  @Provides
  @Pluto
  @Singleton
  def pluto(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.Pluto())

  @Provides
  @Sun
  @Singleton
  def sun(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.Sun())

  @Provides
  @SolarSystemBarycenter
  @Singleton
  def ssb(ephemeris: JplEphemeris[F]): Body[F] = ephemeris.body(jplEphemeris.SSB())

  def configure() {}

}
