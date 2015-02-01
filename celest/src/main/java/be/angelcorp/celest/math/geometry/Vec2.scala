package be.angelcorp.celest.math.geometry

case class Vec2( var x: Double, var y: Double ) {

  def size = 2

  def r = x
  def r_=(s: Double) = x = s
  def g = y
  def g_=(s: Double) = y = s

  def s = x
  def s_=(s: Double) = x = s
  def t = y
  def t_=(s: Double) = y = s

  def apply( index: Int ) = index match {
    case 0 => x
    case 1 => y
    case _ => throw new IndexOutOfBoundsException
  }

  def +(s: Double) =
    new Vec2( x+s, y+s )
  def +=( s: Double ) {
    x += s
    y += s
  }
  def +(v: Vec2) =
    new Vec2( x+v.x, y+v.y )
  def +=( v: Vec2 ) {
    x += v.x
    y += v.y
  }
  def -(s: Double) =
    new Vec2( x-s, y-s )
  def -=( s: Double )  {
    x -= s
    y -= s
  }
  def -(v: Vec2) =
    new Vec2( x-v.x, y-v.y )
  def -=( v: Vec2  ) {
    x -= v.x
    y -= v.y
  }
  def *(s: Double) =
    new Vec2( x*s, y*s )
  def *=( s: Double ) {
    x *= s
    y *= s
  }
  def *(v: Vec2) =
    new Vec2( x*v.x, y*v.y )
  def *=( v: Vec2  ) {
    x *= v.x
    y *= v.y
  }
  def /(s: Double) =
    new Vec2( x/s, y/s )
  def /=( s: Double ) {
    x /= s
    y /= s
  }
  def /(v: Vec2) =
    new Vec2( x/v.x, y/v.y )
  def /=( v: Vec2  ) {
    x /= v.x
    y /= v.y
  }
  def %(s: Double) =
    new Vec2( x%s, y%s )
  def %= (s: Double) {
    x = x % s
    y = y % s
  }
  def %(v: Vec2) =
    new Vec2( x%v.x, y%v.y )
  def %= (v: Vec2 ) {
    x = x % v.x
    y = y % v.y
  }

  def negate() {
    x = -x
    y = -y
  }

  def unary_- = new Vec2(-x, -y)

  def length = {
    val sqr = x * x + y * y
    math.sqrt(sqr).toDouble
  }

  def distance( other: Vec2 ) = (other - this).length

  def dot( v: Vec2 ) = x * v.x + y * v.y

  def normalize = this / length

  def normalize(result: Vec2) = {
    val len = length
    result.x = x / len
    result.y = y / len
  }

  override def toString: String = s"Vec2($x, $y)"

  override def equals(obj: Any) = obj match {
    case v: Vec2 => v.x == x && v.y == y
    case _ => false
  }

  override def hashCode() =
    (2 * 31 + Double.box(x).hashCode()) * 31 + Double.box(y).hashCode()

}

object Vec2 {

  def apply(): Vec2  =
    new Vec2(0, 0)

  def apply(v: Vec2): Vec2 =
    new Vec2(v.x, v.y)

  def apply( s: Double ): Vec2 =
    new Vec2(s, s)

}
