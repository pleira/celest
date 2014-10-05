package be.angelcorp.celest.sofa.universe

import javax.inject.Singleton

import be.angelcorp.celest.sofa.time._
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations._
import be.angelcorp.celest.universe.UniverseBuilder
import net.codingwell.scalaguice.ScalaModule

trait SofaTime extends UniverseBuilder {

  modules += SofaTime.timeStandards

}

object SofaTime {
  
  def timeStandards = new ScalaModule {
    override def configure() {
      bind[SofaTT].in[Singleton]
      bind[SofaTT].annotatedWith[TT].to[SofaTT]
      bind[TimeStandard].annotatedWith[TT ].to[SofaTT ]
      bind[TimeStandard].annotatedWith[TDT].to[SofaTT ]

      bind[SofaTAI].in[Singleton]
      bind[SofaTAI].annotatedWith[TAI].to[SofaTAI]
      bind[TimeStandard].annotatedWith[TAI].to[SofaTAI]

      bind[SofaTCB].in[Singleton]
      bind[SofaTCB].annotatedWith[TCB].to[SofaTCB]
      bind[TimeStandard].annotatedWith[TCB].to[SofaTCB]

      bind[SofaTCG].in[Singleton]
      bind[SofaTCG].annotatedWith[TCG].to[SofaTCG]
      bind[TimeStandard].annotatedWith[TCG].to[SofaTCG]

      bind[SofaTDB].in[Singleton]
      bind[SofaTDB].annotatedWith[TDB].to[SofaTDB]
      bind[TimeStandard].annotatedWith[TDB].to[SofaTDB]

      bind[SofaUTC].in[Singleton]
      bind[SofaUTC].annotatedWith[UTC].to[SofaUTC]
      bind[TimeStandard].annotatedWith[UTC].to[SofaUTC]

      bind[SofaUT1].in[Singleton]
      bind[SofaUT1].annotatedWith[UT1].to[SofaUT1]
      bind[TimeStandard].annotatedWith[UT1].to[SofaUT1]
    }
  }

}
