package be.angelcorp.libs.celest.time;

import java.util.Date;

/**
 * A container for a julian date
 * 
 * @author Simon Billemont
 * 
 */
public interface IJulianDate {

	/**
	 * Get the julian date in a standard date object
	 * 
	 * @return JD in normal date format
	 */
	public abstract Date getDate();

	/**
	 * Get the julian date
	 * 
	 * @return JD
	 */
	public abstract double getJD();

	/**
	 * Set the julian date
	 * 
	 * @param date
	 *            Standard date object
	 */
	public abstract void setDate(Date date);

	/**
	 * Set the julian date
	 * 
	 * @param date
	 *            JD
	 */
	public abstract void setJD(double date);

}