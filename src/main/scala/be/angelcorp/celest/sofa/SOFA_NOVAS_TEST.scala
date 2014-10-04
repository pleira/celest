package be.angelcorp.celest.sofa

import be.angelcorp.sofa.SofaLibrary._
import org.bridj.Pointer

object SOFA_NOVAS_TEST extends App {

  SOFA_Test()

  def SOFA_Test() {
    /* Arcseconds to radians*/
    val as2r = 4.848136811095359935899141e-6
    /* 2Pi */
    val twopi = 6.283185307179586476925287

    val termat = Pointer.allocateDoubles(3,3)
    termat(0)(0) = 0.97310431770109063
    termat(1)(0) = 0.23036382622411070
    termat(2)(0) = -0.00070316348296544
    termat(0)(1) = -0.23036380044102267
    termat(1)(1) = 0.97310457063635536
    termat(2)(1) = 0.00011854535854669
    termat(0)(2) = 0.00071156016155651
    termat(1)(2) = 0.00004662641201635
    termat(2)(2) = 0.99999974575402495
    repmat ("termat", termat)

    val sofmat = Pointer.allocateDoubles(3,3)
    sofmat(0)(0) = +0.97310431757363003
    sofmat(1)(0) = +0.23036382624733387
    sofmat(2)(0) = -0.00070333224579995
    sofmat(0)(1) = -0.23036379880468646
    sofmat(1)(1) = +0.97310457073561185
    sofmat(2)(1) = +0.00012088727913663
    sofmat(0)(2) = +0.00071226387930021
    sofmat(1)(2) = +0.00004438635469672
    sofmat(2)(2) = +0.99999974535497649
    repmat ("sofmat", sofmat)

    /* utc. */
    val year = 2007
    val month = 4
    val day = 5
    val hour = 12
    val minute = 0
    val seconds = 0.0
    printf ("date %4d %2d %2d\n", year, month, day)
    printf ("time %4d %2d %5.1f UTC\n", hour, minute, seconds)
    /* Polar motion (arcsec->radians). */
    val xp = 0.0349282 * as2r
    val yp = 0.4833163 * as2r
    printf ("X_p = %13.9f arcsec\n",xp/as2r)
    printf ("Y_p = %13.9f arcsec\n",yp/as2r)
    /* ut1-utc (s). */
    val dut1 = -0.072073685
    printf ("ut1-utc = %16.12f\n", dut1)
    /* Nutation corrections wrt IAU 1976/1980 (mas->radians). */
    val ddp80 = -55.0655 * as2r / 1000.0
    val dde80 = -6.3580 * as2r / 1000.0
    printf ("dDpsi (1980) = %13.9f arcsec\n",ddp80 / as2r)
    printf ("dDeps (1980) = %13.9f arcsec\n",dde80 / as2r)
    /* CIP offsets wrt IAU 2000A (mas->radians). */
    val dx00 = 0.1725 * as2r / 1000.0
    val dy00 = -0.2650 * as2r / 1000.0
    printf ("dx (2000) = %13.9f arcsec\n",dx00 / as2r)
    printf ("dy (2000) = %13.9f arcsec\n",dy00 / as2r)
    /* CIP offsets wrt IAU 2006/2000A (mas->radians). */
    val dx06 = 0.1750 * as2r / 1000.0
    val dy06 = -0.2259 * as2r / 1000.0
    printf ("dx (2006) = %13.9f arcsec\n",dx06 / as2r)
    printf ("dx (2006) = %13.9f arcsec\n",dx06 / as2r)
    /* TT (MJD). */
    val djmjd0_p = Pointer.allocateDouble()
    val date_p   = Pointer.allocateDouble()
    iauCal2jd (year, month, day, djmjd0_p, date_p)
    val djmjd0 = Double.unbox( djmjd0_p.get )
    val date   = Double.unbox( date_p.get )
    val time = (60.0 * ((60.0 * hour) + minute) + seconds) / 86400.0
    val utc = date + time
    val dat_p = Pointer.allocateDouble()
    iauDat (year, month, day, time, dat_p)
    val dat = dat_p.get
    val tai = utc + dat / 86400.0
    val TT = tai + 32.184 / 86400.0
    printf ("TT = 2400000.5 + %22.15f\n", TT)
    /* ut1. */
    val tut = time + dut1 / 86400.0
    val ut1 = date + tut
    printf ("UT1 = 2400000.5 + %22.15f\n", ut1)
    /* =========================
    * IAU 2006/2000A, CIO-based
    * ========================= */
    printf ("=========================\n")
    printf ("IAU 2006/2000A, CIO-based\n")
    printf ("=========================\n")
    /* CIP and CIO, IAU 2006/2000A. */
    /* Report inputs to CIP calculation */
    printf ("djmjd0 = %22.15f tt= %22.15f\n", djmjd0,TT)
    val x_p = Pointer.allocateDouble()
    val y_p = Pointer.allocateDouble()
    val s_p = Pointer.allocateDouble()
    iauXys06a (djmjd0, TT, x_p, y_p, s_p)
    /* Add CIP corrections. */
    val x = x_p.get + dx06
    val y = y_p.get + dy06
    val s = s_p.get
    /* Report CIP and s. */
    printf ("x = %22.15f\n",x/as2r)
    printf ("y = %22.15f\n",y/as2r)
    printf ("s = %22.15f\n",s/as2r)
    /* GCRS to CIRS matrix. */
    val rc2i = Pointer.allocateDoubles(3,3)
    iauC2ixys (x, y, s, rc2i.get)
    /* Report. */
    repmat ("NPB matrix, CIO-based (rc2i)", rc2i)

    /* Earth rotation angle. */
    /* Set tut to the value used in Tercel */
    printf ("djmjd0 = %f date = %f tut = %f\n",djmjd0, date, tut)
    val era = iauEra00 (djmjd0, date+tut)
    printf ("era = %19.16f rad\n",era)
    printf ("era = %16.12f deg\n",era * 360.0/twopi)
    val IHMSF = Pointer.allocateInts(4)
    val pm = Pointer.allocateByte()
    iauD2tf (9, era/twopi, pm, IHMSF)

    /* Form celestial-terrestrial matrix (no polar motion yet). */
    val rc2ti = Pointer.allocateDoubles(3,3)
    iauCr (rc2i.get, rc2ti.get)
    iauRz (era, rc2ti.get)

    /* Report. */
    repmat ("celestial to terrestrial matrix (no polar motion)",rc2ti)
    val r3 = Pointer.allocateDoubles(3,3)
    iauCr (rc2ti.get, r3.get)

    /* Polar motion matrix (TIRS->ITRS, IERS 2003). */
    val rpom = Pointer.allocateDoubles(3,3)
    iauPom00 (xp, yp, iauSp00(djmjd0,TT), rpom.get)

    /* Form celestial-terrestrial matrix (including polar motion). */
    val rc2it = Pointer.allocateDoubles(3,3)
    iauRxr (rpom.get, rc2ti.get, rc2it.get)

    /* Transpose termat */
    val tertrans = Pointer.allocateDoubles(3,3)
    val softrans = Pointer.allocateDoubles(3,3)
    transpose (termat, tertrans)
    transpose (sofmat, softrans)
    /* Report. */
    repmat ("celestial to terrestrial matrix", rc2it)
    repmat ("tercel_transposed matrix", tertrans)
    /* Copy for later comparison. */
    val r4 = Pointer.allocateDoubles(3,3)
    iauCr (rc2it.get, r4.get)
    /* Compare result to TERCEL_Transposed matrix */
    val result1 = drot (r4, tertrans)
    printf ("%f\n", result1/ as2r)
    printf ("w/ pm result vs tercel = %20.10e uas\n", drot (r4, tertrans) * 1.0e6 / as2r)
    /* Compare SOFA w/P03 and SOFA w/o P03 */
    val result2 = drot (r4, softrans)
    printf ("%f\n", result2/ as2r)
    printf ("sofa w/p03 vs w/o p03 = %20.10e uas\n", drot (r4, softrans) * 1.0e6 / as2r)
  }

  def repmat(s: String, r: Pointer[Pointer[java.lang.Double]]) {
    printf ("%s\n", s);
    printf ("%22.17f %22.17f %22.17f\n", r(0)(0),r(0)(1),r(0)(2))
    printf ("%22.17f %22.17f %22.17f\n", r(1)(0),r(1)(1),r(1)(2))
    printf ("%22.17f %22.17f %22.17f\n", r(2)(0),r(2)(1),r(2)(2))
    return;
  }

  /** Express the difference between two rotation matrices RMA,RMB as an amount of rotation R about some arbitrary axis. */
  def drot (rma: Pointer[Pointer[java.lang.Double]], rmb: Pointer[Pointer[java.lang.Double]]) =  {
    val rmbt = Pointer.allocateDoubles(3,3)
    val rm   = Pointer.allocateDoubles(3,3)
    val rv   = Pointer.allocateDoubles( 3 )
    /* Multiply the first matrix by the inverse of the second. */
    iauTr (rmb.get, rmbt.get)
    iauRxr (rmbt.get, rma.get, rm.get)
    /* Express the result as an r-vector. */
    iauRm2v (rm.get, rv)
    /* Return the magnitude (the amount of rotation in radians). */
    iauPm (rv)
  }

  /** Transpose 3x3 matrix R ---> RT */
  def transpose(r: Pointer[Pointer[java.lang.Double]], rt: Pointer[Pointer[java.lang.Double]]) {
    for (i <- 0 until 3; j <- 0 until 3)
      rt(i)(j) = r(j)(i)
  }

  //  def terceltest () {
  //    /* Transform vectors from ITRS to GCRS */
  ////    double tjdh, tjdl, xp, yp, vec1[3], vec2[3], tjd,mobl,tobl,ee;
  //    val delt = 65.25607389
  //    val num = 0
  //    val dx = +0.1750
  //    val dy = -0.2259
  //    /* Open the input file of Julian dates,CIO coords,ITRS vector */
  //    val In_Data = List(
  //      (1, 2400000.5, 54195.49999916581146, +0.0349282e0, +0.4833163e0, Vector3D(1.0, 0.0, 0.0)),
  //      (1, 2400000.5, 54195.49999916581146, +0.0349282e0, +0.4833163e0, Vector3D(0.0, 1.0, 0.0)),
  //      (1, 2400000.5, 54195.49999916581146, +0.0349282e0, +0.4833163e0, Vector3D(0.0, 0.0, 1.0))
  //    )
  //    for ( (num,tjdh,tjdl,xp,yp,vec1) <- In_Data ) {
  //      /* Set transformation method, accuracy level, and ut1-utc. */
  //      val tjd = tjdh + tjdl
  //      /* celpol (tjd,2,dx,dy);*/
  //      e_tilt (tjd,0, &mobl,&tobl,&ee,&dx,&dy)
  //      /* Rotate vec1 from ITRS to GCRS = vec2 */
  //      ter2cel (tjdh,tjdl,delt,1,0,0,xp,yp,vec1, vec2)
  //      printf ("%i %20.17f %20.17f%20.17f\n",num,vec2(0),vec2(1),vec2(2));
  //    }
  //  }


}
