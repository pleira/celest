import be.angelcorp.celest.time.Epochs._
import be.angelcorp.celest.time.{Epoch, JulianDate}
import be.angelcorp.celest.time.timeStandard.TimeStandard
implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse

// Create a new time standard
val myTime = new TimeStandard {
  // Number of seconds to add to the TT time standard to get myTime
  def offsetToTT(jd_myTime: Epoch): Double = 1.0 / jd_myTime.jd
  // Number of seconds to add to myTime to get the TT time standard
  def offsetFromTT(jd_TT: Epoch): Double = {
    val x = jd_TT.jd
    (x + math.sqrt(-4 + x*x)) / 2 - x
  }
}

// Verify that the time standard is symmetrical, so that converting to TT and then from TT returns the original time
// fromTT(jd + toTT(jd)) + toTT(jd) = 0
val toTT   = myTime.offsetToTT(J2000)
val fromTT = myTime.offsetFromTT(J2000 addS toTT)
println("Error [days]:" + (toTT + fromTT) )

// Make a new Epoch based on myTime
val epoch = new JulianDate(2451545.0, myTime)
println( s"J2000.0 in myTime: $epoch" )
