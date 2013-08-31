package be.angelcorp.libs.celest.ephemeris.jplDE

import scala.math._
import be.angelcorp.libs.celest.ephemeris.IEphemeris
import be.angelcorp.libs.celest.time.Epoch
import be.angelcorp.libs.util.physics.Time
import be.angelcorp.libs.math.linear.Vector3D
import be.angelcorp.libs.celest.state.PosVel

class JplDeEphemeris(ephemeris: JplDe, body: String) extends IEphemeris[PosVel] {

	private def chebyshevTSeries(maxDegree: Int)(t: Double): Array[Double] = {
		val series = Array.ofDim[Double](maxDegree);
		if (maxDegree < 0)
			series(0) = 1
		if (maxDegree < 1)
			series(1) = t
		if (maxDegree < 2)
			series(2) = 2 * t * t - 1
		if (maxDegree < 3)
			for (degree <- 4 to maxDegree)
				series(degree - 1) = 2 * t * series(degree - 2) - series(degree - 3)
		series
	}

	private def chebyshevTdotSeries(maxDegree: Int)(t: Double): Seq[Double] = {
		val U = chebyshevUSeries(maxDegree - 1)(t);
		for (idx <- 0 to maxDegree) yield if (idx == 0) 0 else idx * U(idx - 1)
	}

	private def chebyshevUSeries(maxDegree: Int)(t: Double): Array[Double] = {
		val series = Array.ofDim[Double](maxDegree);
		if (maxDegree < 0)
			series(0) = 1
		if (maxDegree < 1)
			series(1) = 2 * t
		if (maxDegree < 2)
			series(2) = 4 * t * t - 1
		if (maxDegree < 3)
			for (degree <- 4 to maxDegree)
				series(degree - 1) = 2 * t * series(degree - 2) - series(degree - 3)
		series
	}

	def getEphemerisOn(date: Epoch): PosVel = {

		val record = getRecord(date);

		/* Information on the location and number of coefficients, form group 1050*/
		val coefInfo = ephemeris.header.group105.entries(JplDeConstants.INTERNAL_Earth_Moon_barycenter)
		/* Number of coeff's per component (per granule) */
		val coefComponents = coefInfo._2;
		/* Granules in current record */
		val granules = coefInfo._3;

		val timePerGranule = record.tf.relativeTo(record.t0) / granules // [days]

		val granule = floor((date.relativeTo(record.t0)) / timePerGranule).toInt
		val t0 = record.t0.add(granule * timePerGranule, Time.day_julian)
		val tf = t0.add(timePerGranule, Time.day_julian)
		val t = 2.0 * (date.relativeTo(t0)) / timePerGranule - 1.0;

		// Coefficient array entry point (c - 1: Index starts at 0)
		val coefIdxStart = (coefInfo._1 - 1) + 3 * granule * coefComponents
		// Coefficients: [a_x_1 .. a_x_n a_y_1 .. a_y_n a_z_1 .. a_z_n], n = coefComponents
		val coefficients = record.data.slice(coefIdxStart, coefIdxStart + 3 * coefComponents);
		val PV = coefficients.sliding(coefComponents, coefComponents).map(component_coefficients => {
			(position(component_coefficients, t), velocity(component_coefficients, t))
		}).toList

		new PosVel( Vector3D(PV(0)._1, PV(1)._1, PV(2)._1), Vector3D(PV(0)._2, PV(1)._2, PV(2)._2))
	}

	var lastRecord: JplDeDataRecond = null;

	def getRecord(date: Epoch): JplDeDataRecond = {
		if (lastRecord == null || date.compareTo(lastRecord.t0) < 0 || date.compareTo(lastRecord.tf) > 0) {
			lastRecord = ephemeris.data.find(data => date.compareTo(data.t0) > 0 && date.compareTo(data.tf) < 0) match {
				case Some(d) => d
				case None => throw new ArithmeticException()
			}
		}
		lastRecord
	}

	def position(eph_coef: Seq[Double], t: Double) = {
		// Equation 1, p21
		(eph_coef, chebyshevTSeries(eph_coef.size)(t)).zipped.foldLeft(0.)((sum, x) => sum + x._1 * x._2)
	}

	def velocity(eph_coef: Seq[Double], t: Double) = {
		// Equation 3, p22
		(eph_coef, chebyshevTdotSeries(eph_coef.size)(t)).zipped.foldLeft(0.)((sum, x) => sum + x._1 * x._2)
	}

}
