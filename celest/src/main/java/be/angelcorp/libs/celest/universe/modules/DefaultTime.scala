package be.angelcorp.libs.celest.universe.modules

import com.google.inject._
import com.google.inject.name.{Named, Names}
import be.angelcorp.libs.util.physics.Time
import be.angelcorp.libs.celest.time.{JulianDate, Epoch}
import be.angelcorp.libs.celest.time.Epochs._
import be.angelcorp.libs.celest.time.timeStandard._
import be.angelcorp.libs.celest.universe.Universe

/**
 * This module configures the default behaviour of the different time standards available in the universe.
 *
 * It defines the following time standards:
 *
 * <ul>
 * <li>TAI: [[be.angelcorp.libs.celest.time.timeStandard.TAI]]</li>
 * <li>TT: [[be.angelcorp.libs.celest.time.timeStandard.TT]]</li>
 * <li>TDT: [[be.angelcorp.libs.celest.time.timeStandard.TT]]</li>
 * <li>TCB: [[be.angelcorp.libs.celest.time.timeStandard.TCB]]</li>
 * <li>TCG: [[be.angelcorp.libs.celest.time.timeStandard.TCG]]</li>
 * <li>TDB: [[be.angelcorp.libs.celest.time.timeStandard.TDB]]</li>
 * <li>UTC: [[be.angelcorp.libs.celest.time.timeStandard.DefaultUTC]]</li>
 * <li>UT1: [[be.angelcorp.libs.celest.time.timeStandard.DefaultUT1]]</li>
 * </ul>
 *
 * Furthermore it defines the following epochs:
 *
 * <ul>
 * <li>J2000.0: 2451545.0jd TT</li>
 * <li>J1950.0: 2433282.0jd TT</li>
 * <li>J1900.0: 2415020.0jd TT</li>
 * <li>B1950.0: 2433282.42345905jd TT</li>
 * <li>TT  epoch: 2433282.42345905jd TT</li>
 * <li>TAI epoch: 2433282.42345905jd TT</li>
 * <li>TCG epoch: 2433282.42345905jd TT</li>
 * <li>TCB epoch: 2433282.42345905jd TT</li>
 * <li>TDB epoch: 2433282.42345905jd TT - 65.5 µs </li>
 * </ul>
 *
 */
class DefaultTime extends AbstractModule {

  def configure() {
    bind( classOf[ITimeStandard] ).annotatedWith( Names.named( "TAI" ) ).to( classOf[TAI] ).asEagerSingleton()
    bind( classOf[ITimeStandard] ).annotatedWith( Names.named( "TT"  ) ).to( classOf[TT]  ).asEagerSingleton()
    bind( classOf[ITimeStandard] ).annotatedWith( Names.named( "TDT" ) ).to( classOf[TT]  ).asEagerSingleton()
    bind( classOf[ITimeStandard] ).annotatedWith( Names.named( "TCB" ) ).to( classOf[TCB] ).asEagerSingleton()
    bind( classOf[ITimeStandard] ).annotatedWith( Names.named( "TCG" ) ).to( classOf[TCG] ).asEagerSingleton()
    bind( classOf[ITimeStandard] ).annotatedWith( Names.named( "TDB" ) ).to( classOf[TDB] ).asEagerSingleton()
    bind( classOf[ITimeStandard] ).annotatedWith( Names.named( "UTC" ) ).to( classOf[DefaultUTC] ).asEagerSingleton()
    bind( classOf[ITimeStandard] ).annotatedWith( Names.named( "UT1" ) ).to( classOf[DefaultUT1] ).asEagerSingleton()
  }

  @Provides
  @J2000
  @Singleton
  /** The J2000 epoch in JulianDate form. */
  def j2000Provider(@Named("TT") tt: ITimeStandard, universe: Universe): Epoch = new JulianDate(2451545.0, tt)(universe)

  @Provides
  @J1950
  @Singleton
  /** The J1950 epoch in JulianDate form. */
  def j1950Provider(@Named("TT") tt: ITimeStandard, universe: Universe): Epoch = new JulianDate(2433282.5, tt)(universe)

  @Provides
  @J1900
  @Singleton
  /** The J1900 epoch in JulianDate form. */
  def j1900Provider(@Named("TT") tt: ITimeStandard, universe: Universe): Epoch = new JulianDate(2415020.0, tt)(universe)

  @Provides
  @B1950
  @Singleton
  /** The B1950 epoch in JulianDate form. */
  def b950Provider(@Named("TT") tt: ITimeStandard, universe: Universe): Epoch = new JulianDate(2433282.42345905, tt)(universe)


  @Provides
  @TAI_EPOCH
  @Singleton
  /** The starting epoch of the TAI timeline: 1 January 1977 00:00:00 TAI (same as TAI/TT/TCG/TCB). */
  def taiEpochProvider(@Named("TT") tt: ITimeStandard, universe: Universe): Epoch = new JulianDate( 2443144.5003725, tt)(universe)

  @Provides
  @TT_EPOCH
  @Singleton
  /** The starting epoch of the TT timeline: 1 January 1977 00:00:00 TAI (same as TAI/TT/TCG/TCB). */
  def ttEpochProvider(@TAI_EPOCH epoch: Epoch): Epoch = epoch

  @Provides
  @TCG_EPOCH
  @Singleton
  /** The starting epoch of the TCG timeline: 1 January 1977 00:00:00 TAI (same as TAI/TT/TCG/TCB). */
  def tcgEpochProvider(@TAI_EPOCH epoch: Epoch): Epoch = epoch

  @Provides
  @TCB_EPOCH
  @Singleton
  /** The starting epoch of the TCB timeline: 1 January 1977 00:00:00 TAI (same as TAI/TT/TCG/TCB). */
  def tcbEpochProvider(@TAI_EPOCH epoch: Epoch): Epoch = epoch

  @Provides
  @TDB_EPOCH
  @Singleton
  /** The starting epoch of the TDB timeline. */
  def tdbEpochProvider(@TAI_EPOCH epoch: Epoch): Epoch = epoch.add(-65.5, Time.microsecond)

}
