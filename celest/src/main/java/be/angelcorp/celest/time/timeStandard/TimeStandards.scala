package be.angelcorp.celest.time.timeStandard

import be.angelcorp.celest.universe.Universe

/**
 * A set of short-hand functions to retrieve predefined time standards (see [[be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations]]) from the universe.
 */
object TimeStandards {

  /** See [[be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.TT]] */
  def TT(implicit universe: Universe) = universe.instance[TimeStandard, TimeStandardAnnotations.TT]

  /** See [[be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.TDT]] */
  def TDT(implicit universe: Universe) = universe.instance[TimeStandard, TimeStandardAnnotations.TDT]

  /** See [[be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.TAI]] */
  def TAI(implicit universe: Universe) = universe.instance[TimeStandard, TimeStandardAnnotations.TAI]

  /** See [[be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.TCB]] */
  def TCB(implicit universe: Universe) = universe.instance[TimeStandard, TimeStandardAnnotations.TCB]

  /** See [[be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.TCG]] */
  def TCG(implicit universe: Universe) = universe.instance[TimeStandard, TimeStandardAnnotations.TCG]

  /** See [[be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.TDB]] */
  def TDB(implicit universe: Universe) = universe.instance[TimeStandard, TimeStandardAnnotations.TDB]

  /** See [[be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.UTC]] */
  def UTC(implicit universe: Universe) = universe.instance[TimeStandard, TimeStandardAnnotations.UTC]

  /** See [[be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.UT1]] */
  def UT1(implicit universe: Universe) = universe.instance[TimeStandard, TimeStandardAnnotations.UT1]

  /** See [[be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.GPS]] */
  def GPS(implicit universe: Universe) = universe.instance[TimeStandard, TimeStandardAnnotations.GPS]

}
