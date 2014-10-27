package be.angelcorp.celest.math.geometry

class Vec3(var x: Double, var y: Double, var z: Double) {

  def size = 3

  def xy = new Vec2(x, y)
  def xy_=(v: Vec2) {
    x = v.x
    y = v.y
  }
  def yz = new Vec2(y, z)
  def yz_=(v: Vec2) {
    y = v.x
    z = v.y
  }

  def apply( index: Int ) = index match {
    case 0 => x
    case 1 => y
    case 2 => z
    case _ => throw new IndexOutOfBoundsException
  }

  def +(s: Double) =
    Vec3( x+s, y+s, z+s )
  def :+=( s: Double ) {
    x += s
    y += s
    z += s
  }
  def +(v: Vec3) =
    Vec3( x+v.x, y+v.y, z+v.z )
  def +=( v: Vec3  ) {
    x += v.x
    y += v.y
    z += v.z
  }
  def -(s: Double) =
    Vec3( x-s, y-s, z-s )
  def -=( s: Double )  {
    x -= s
    y -= s
    z -= s
  }
  def -(v: Vec3) =
    Vec3( x-v.x, y-v.y, z-v.z )
  def -=( v: Vec3  ) {
    x -= v.x
    y -= v.y
    z -= v.z
  }
  def *(s: Double) =
    Vec3( x*s, y*s, z*s )
  def *=( s: Double ) {
    x *= s
    y *= s
    z *= s
  }
  def *(v: Vec3) =
    Vec3( x*v.x, y*v.y, z*v.z )
  def *=( v: Vec3  ) {
    x *= v.x
    y *= v.y
    z *= v.z
  }
  def /(s: Double) =
    Vec3( x/s, y/s, z/s )
  def /=( s: Double ) {
    x /= s
    y /= s
    z /= s
  }
  def /(v: Vec3) =
    Vec3( x/v.x, y/v.y, z/v.z )
  def /=( v: Vec3  ) {
    x /= v.x
    y /= v.y
    z /= v.z
  }
  def %(s: Double) =
    Vec3( x%s, y%s, z%s )
  def %= (s: Double) {
    x = x % s
    y = y % s
    z = z % s
  }
  def %(v: Vec3) =
    Vec3( x%v.x, y%v.y, z%v.z )
  def %= (v: Vec3 ) {
    x = x % v.x
    y = y % v.y
    z = z % v.z
  }

  def negate() {
    x = -x
    y = -y
    z = -z
  }

  def unary_- = Vec3(-x, -y, -z)

  def lengthSq = x * x + y * y + z * z
  def normSq = lengthSq

  def length = math.sqrt(lengthSq)
  def norm = length

  def dot( v: Vec3 ) = x * v.x + y * v.y + z * v.z

  def distance( v: Vec3 ) = (v - this).length

  /**
   * Compute the angular separation between two vectors.
   * <p>
   * This method computes the angular separation between two vectors using the dot product for well
   * separated vectors and the cross product for almost aligned vectors. This allows to have a good
   * accuracy in all cases, even for vectors very close to each other.
   * </p>
   * @param v2 Other vector.
   * @return The angular separation between this and v2
   * @throws ArithmeticException If either vector has a null norm
   */
  def angle(v2: Vec3): Double = {
    val normProduct = this.norm * v2.norm
    if (normProduct == 0)
      throw new ArithmeticException("Cannot calculate angle between vectos, dot product is 0.")

    val dot = this dot v2
    val threshold = normProduct * 0.9999
    if ((dot < -threshold) || (dot > threshold)) {
      val v3 = this cross v2
      if (dot >= 0)
        math.asin(v3.norm / normProduct)
      else
        math.Pi - math.asin(v3.norm / normProduct)
    } else
      math.acos(dot / normProduct)
  }

  /** Vector-vector cross product, stored in a new vector. */
  def cross(v: Vec3) = Vec3(
    y * v.z - v.y * z,
    z * v.x - v.z * x,
    x * v.y - v.x * y
  )

  /** Vector-vector cross product, stored in the given vector. */
  def cross(v: Vec3, result: Vec3) =  {
    result.x = y * v.z - v.y * z
    result.y = z * v.x - v.z * x
    result.z = x * v.y - v.x * y
  }

  def normalize(result: Vec3) = {
    val len = length
    result.x = x / len
    result.y = y / len
    result.z = z / len
  }


  def normalized = this / length

  def copyOf( v: Vec3 ) {
    x = v.x
    y = v.y
    z = v.z
  }

  def interpolate( vFinal: Vec3, t: Double ): Vec3 = Vec3(
    (1f-t) * x + t * vFinal.x,
    (1f-t) * y + t * vFinal.y,
    (1f-t) * z + t * vFinal.z
  )

  /**
   * Creates a new Vec3 by combining the components of this vector with another vector via a function:
   * <pre>
   *   Vec3( op(x, other.x), op(y, other.y), op(z, other.z) )
   * </pre>
   *
   * @param other Other vector to combine with
   * @param op Operation to combine the two components into the component of the new vector.
   * @return A new vector.
   */
  def combine( other: Vec3 )( op: (Double, Double) => Double ) =
    Vec3(
      op(x, other.x),
      op(y, other.y),
      op(z, other.z)
    )

  def combineSelf( other: Vec3 )( op: (Double, Double) => Double ) {
    x = op(x, other.x)
    y = op(y, other.y)
    z = op(z, other.z)
  }

  def map( op: Double => Double ) = {
    Vec3( op(x), op(y), op(z) )
  }

  def mapSelf( op: Double => Double ) {
    x = op(x)
    y = op(y)
    z = op(z)
  }

  override def toString: String = s"Vec3($x, $y, $z)"

  override def equals(obj: Any) = obj match {
    case v: Vec3 => v.x == x && v.y == y && v.z == z
    case _ => false
  }

  override def hashCode() =
    ((3 * 31 + Double.box(x).hashCode()) * 31 + Double.box(y).hashCode()) * 31 + Double.box(z).hashCode()

}

object Vec3 {

  def zero = Vec3(0, 0, 0)
  def ones = Vec3(1, 1, 1)

  def x: Vec3  = Vec3(1, 0, 0)
  def y: Vec3  = Vec3(0, 1, 0)
  def z: Vec3  = Vec3(0, 0, 1)

  /** A vector containing <0, 0, 0> */
  def apply(): Vec3 = Vec3.zero

  def apply(v: Vec3): Vec3 =
    Vec3(v.x, v.y, v.z)

  def apply( s: Double ): Vec3 =
    Vec3(s, s, s)

  def apply( x: Double, y: Double, z: Double ): Vec3 =
    new Vec3(x, y, z)

  def apply( xy: Vec2, z: Double ): Vec3 =
    Vec3( xy.x, xy.y, z )

  def apply( x: Double, yz: Vec2 ): Vec3 =
    Vec3( x, yz.x, yz.y )

  def apply( xyz: Array[Double] ): Vec3 = {
    require( xyz.length >= 3 )
    Vec3( xyz(0), xyz(1), xyz(2) )
  }

  /**
   * Build a vector from its special coordinates.
   * @param azimuth Azimuth or longitude (&alpha;) around Z (0 is +X, &pi;/2 is +Y, &pi; is -X and 3&pi;/2 is -Y)
   * @param elevation Elevation or latitude (&delta;) above (XY) plane, from -&pi;/2 to +&pi;/2
   */
  def spherical(azimuth: Double, elevation: Double, radius: Double = 1): Vec3 = {
    val cosDelta = math.cos(elevation)
    Vec3(
      radius * math.cos(azimuth) * cosDelta,
      radius * math.sin(azimuth) * cosDelta,
      radius * math.sin(elevation)
    )
  }

  def random(): Vec3 =
    Vec3( util.Random.nextDouble(), util.Random.nextDouble(), util.Random.nextDouble() )

}
