package be.angelcorp.celest.math.geometry

case class Vec4( var x: Double, var y: Double, var z: Double, var w: Double ) {

  def size = 4

  def r = x
  def r_=(s: Double) = x = s
  def g = y
  def g_=(s: Double) = y = s
  def b = z
  def b_=(s: Double) = z = s
  def a = w
  def a_=(s: Double) = w = s

  def s = x
  def s_=(s: Double) = x = s
  def t = y
  def t_=(s: Double) = y = s
  def p = z
  def p_=(s: Double) = z = s
  def q = w
  def q_=(s: Double) = w = s

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
  def zw = new Vec2(z, w)
  def zw_=(v: Vec2) {
    z = v.x
    w = v.y
  }
  def xyz = Vec3(x, y, z)
  def xyz_=(v: Vec3) {
    x = v.x
    y = v.y
    z = v.z
  }
  def yzw = Vec3(y, z, w)
  def yzw_=(v: Vec3) {
    y = v.x
    z = v.y
    w = v.z
  }
  def apply( index: Int ) = index match {
    case 0 => x
    case 1 => y
    case 2 => z
    case 3 => w
    case _ => throw new IndexOutOfBoundsException
  }

  def +(s: Double) =
    new Vec4( x+s, y+s, z+s, w+s )
  def +=( s: Double ) {
    x += s
    y += s
    z += s
    w += s
  }
  def +(v: Vec4) =
    new Vec4( x+v.x, y+v.y, z+v.z, w+v.w )
  def +=( v: Vec4  ) {
    x += v.x
    y += v.y
    z += v.z
    w += v.w
  }
  def -(s: Double) =
    new Vec4( x-s, y-s, z-s, w-s )
  def -=( s: Double )  {
    x -= s
    y -= s
    z -= s
    w -= s
  }
  def -(v: Vec4) =
    new Vec4( x-v.x, y-v.y, z-v.z, w-v.w )
  def -=( v: Vec4  ) {
    x -= v.x
    y -= v.y
    z -= v.z
    w -= v.w
  }
  def *(s: Double) =
    new Vec4( x*s, y*s, z*s, w*s )
  def *=( s: Double ) {
    x *= s
    y *= s
    z *= s
    w *= s
  }
  def *(v: Vec4) =
    new Vec4( x*v.x, y*v.y, z*v.z, w*v.w )
  def *=( v: Vec4  ) {
    x *= v.x
    y *= v.y
    z *= v.z
    w *= v.w
  }
  def /(s: Double) =
    new Vec4( x/s, y/s, z/s, w/s )
  def /=( s: Double ) {
    x /= s
    y /= s
    z /= s
    w /= s
  }
  def /(v: Vec4) =
    new Vec4( x/v.x, y/v.y, z/v.z, w/v.w )
  def /=( v: Vec4  ) {
    x /= v.x
    y /= v.y
    z /= v.z
    w /= v.w
  }
  def %(s: Double) =
    new Vec4( x%s, y%s, z%s, w%s )
  def %= (s: Double) {
    x = x % s
    y = y % s
    z = z % s
    w = w % s
  }
  def %(v: Vec4) =
    new Vec4( x%v.x, y%v.y, z%v.z, w%v.w )
  def %= (v: Vec4 ) {
    x = x % v.x
    y = y % v.y
    z = z % v.z
    w = w % v.w
  }

  def length = {
    val sqr = x * x + y * y + z * z + w * w
    math.sqrt(sqr)
  }

  def negate() {
    x = -x
    y = -y
    z = -z
    w = -w
  }

  def unary_- = new Vec4(-x, -y, -z, -w)

  def distance( other: Vec4 ) = (other - this).length

  def dot( v: Vec4 ) = x * v.x + y * v.y + z * v.z + w * v.w

  def normalize = this / length

  def normalize(result: Vec4) = {
    val len = length
    result.x = x / len
    result.y = y / len
    result.z = z / len
    result.w = w / len
  }

  override def toString: String = s"Vec4($x, $y, $z, $w)"

  override def equals(obj: Any) = obj match {
    case v: Vec4 => v.x == x && v.y == y && v.z == z && v.w == w
    case _ => false
  }

  override def hashCode() =
    (((4 * 31 + Double.box(x).hashCode()) * 31 + Double.box(y).hashCode()) * 31 + Double.box(z).hashCode()) * 31 + Double.box(w).hashCode()

}

object Vec4 {

  def zeros = new Vec4(0, 0, 0, 0)
  def ones  = new Vec4(1, 1, 1, 1)

  def x = new Vec4(1, 0, 0, 0)
  def y = new Vec4(0, 1, 0, 0)
  def z = new Vec4(0, 0, 1, 0)
  def w = new Vec4(0, 0, 0, 1)

  def apply(): Vec4  = Vec4.zeros

  def apply(v: Vec4): Vec4 =
    new Vec4(v.x, v.y, v.z, v.w)

  def apply( s: Double ): Vec4 =
    new Vec4(s, s, s, s)

  def apply( xy: Vec2, z: Double, w: Double  ): Vec4 =
    new Vec4( xy.x, xy.y, z, w )

  def apply( x: Double, yz: Vec2, w: Double  ): Vec4 =
    new Vec4( x, yz.x, yz.y, w )

  def apply( x: Double, y: Double, zw: Vec2 ): Vec4 =
    new Vec4( x, y, zw.x, zw.y )

  def apply( xyz: Vec3, w: Double ): Vec4 =
    new Vec4( xyz.x, xyz.y, xyz.z, w )

  def apply( x: Double, yzw: Vec3 ): Vec4 =
    new Vec4( x, yzw.x, yzw.y, yzw.z )

  def apply( xy: Vec2, zw: Vec2 ): Vec4 =
    new Vec4( xy.x, xy.y, zw.x, zw.y )

}
