package be.angelcorp.celest.ephemeris.jplEphemeris

import be.angelcorp.celest.time.Epoch

sealed abstract class JplDeNode

case class JplDe(size: JplDeSize, header: JplDeHeader, data: Seq[JplDeDataRecond]) extends JplDeNode

case class JplDeSize(KSIZE: Int, NCOEFF: Int) extends JplDeNode

case class JplDeHeader(group101: JplDeHeaderGroup101, group103: JplDeHeaderGroup103, group104: JplDeHeaderGroup104, group105: JplDeHeaderGroup105) extends JplDeNode

case class JplDeHeaderGroup101(lines: List[String]) extends JplDeNode

case class JplDeHeaderGroup103(start: Epoch, stop: Epoch, interval: Double) extends JplDeNode

case class JplDeHeaderGroup104(entries: Map[String, Double]) extends JplDeNode

case class JplDeHeaderGroup105(entries: List[(Int, Int, Int)]) extends JplDeNode

case class JplDeDataRecond(recond: Int, recordsInFile: Int, t0: Epoch, tf: Epoch, data: Seq[Double]) extends JplDeNode
