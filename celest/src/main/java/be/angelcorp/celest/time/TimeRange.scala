package be.angelcorp.celest.time

import scala.collection.immutable.{Nil, IndexedSeq}

/**
 * Used to create an range between two epochs with a predifined step.
 * This is used to create an indexed sequence of intermediate epochs between the two boundries.
 *
 * {{{
 *     val r1 = 0 until 10
 *     val r2 = r1.start until r1.end by r1.step + 1
 *     println(r2.length) // = 5
 * }}}
 *
 * This class is based on [[scala.collection.immutable.Range]].
 *
 * @param start Starting epoch of the interval.
 * @param end   Ending epoch of the interval.
 * @param step  Time step in days used to step through the interval.
 *
 * @author Simon Billemont
 */
class TimeRange(val start: Epoch, val end: Epoch, val step: Double)
  extends Traversable[Epoch]
  with Iterable[Epoch]
  with Seq[Epoch]
  with IndexedSeq[Epoch]
  //with scala.collection.CustomParallelizable[Epoch, ParTimeRange]
  with Serializable {

  //override def par = new ParTimeRange(this)

  private def gap = end.relativeTo(start)

  private def isExact = gap % step == 0

  private def hasStub = isInclusive || !isExact

  private def longLength = gap / step + (if (hasStub) 1 else 0)

  // Check cannot be evaluated eagerly because we have a pattern where ranges are constructed like:
  //  "x to y by z"
  // The "x to y" piece should not trigger an exception. So the calculation is delayed, which means
  // it will not fail fast for those cases where failing was correct.
  override final val isEmpty = (start > end && step > 0) || (start < end && step < 0) || (start == end && !isInclusive)

  final val numRangeElements: Int = {
    if (step == 0) throw new IllegalArgumentException("step cannot be 0.")
    else if (isEmpty) 0
    else {
      val len = longLength
      if (len > scala.Int.MaxValue) -1
      else len.toInt
    }
  }

  final val lastElement = start + (numRangeElements - 1) * step
  final val terminalElement = start + numRangeElements * step

  override def last = if (isEmpty) Nil.last else lastElement

  protected def copy(start: Epoch, end: Epoch, step: Double): TimeRange = new TimeRange(start, end, step)

  /**
   * Create a new range with the `start` and `end` values of this range and a new `step`.
   *
   * @return a new range with a different step
   */
  def by(step: Double): TimeRange = copy(start, end, step)

  def isInclusive = false

  override def size = length

  override def length = if (numRangeElements < 0) fail() else numRangeElements

  private def description = "%s %s %s by %s".format(start, if (isInclusive) "to" else "until", end, step)

  private def fail() = throw new IllegalArgumentException(description + ": seqs cannot contain more than Int.MaxValue elements.")

  private def validateMaxLength() {
    if (numRangeElements < 0)
      fail()
  }

  def validateRangeBoundaries(f: Epoch => Any): Boolean = {
    validateMaxLength()

    /* start != Int.MinValue || end != Int.MinValue || */
    {
      var count = 0
      var num = start
      while (count < numRangeElements) {
        f(num)
        count += 1
        num += step
      }
      false
    }
  }

  final def apply(idx: Int): Epoch = {
    validateMaxLength()
    if (idx < 0 || idx >= numRangeElements) throw new IndexOutOfBoundsException(idx.toString)
    else start + (step * idx)
  }

  @inline final override def foreach[@specialized(Unit) U](f: Epoch => U) {
    if (validateRangeBoundaries(f)) {
      var i = start
      val terminal = terminalElement
      val step = this.step
      while (i != terminal) {
        f(i)
        i += step
      }
    }
  }

  /**
   * Creates a new range containing the first `n` elements of this range.
   *
   * $doesNotUseBuilders
   *
   * @param n  the number of elements to take.
   * @return   a new range consisting of `n` first elements.
   */
  final override def take(n: Int): TimeRange =
    if (n <= 0 || isEmpty) newEmptyRange(start)
    else if (n >= numRangeElements) this
    else new TimeRange.Inclusive(start, locationAfterN(n - 1), step)

  /**
   * Creates a new range containing all the elements of this range except the first `n` elements.
   *
   * $doesNotUseBuilders
   *
   * @param n  the number of elements to drop.
   * @return   a new range consisting of all the elements of this range except `n` first elements.
   */
  final override def drop(n: Int): TimeRange =
    if (n <= 0 || isEmpty) this
    else if (n >= numRangeElements) newEmptyRange(end)
    else copy(locationAfterN(n), end, step)

  /**
   * Creates a new range containing all the elements of this range except the last one.
   *
   * $doesNotUseBuilders
   *
   * @return  a new range consisting of all the elements of this range except the last one.
   */
  final override def init: TimeRange = {
    if (isEmpty)
      Nil.init

    dropRight(1)
  }

  /** Creates a new range containing all the elements of this range except the first one.
    *
    * $doesNotUseBuilders
    *
    * @return  a new range consisting of all the elements of this range except the first one.
    */
  final override def tail: TimeRange = {
    if (isEmpty)
      Nil.tail

    drop(1)
  }

  // Counts how many elements from the start meet the given test.
  private def skipCount(p: Epoch => Boolean): Int = {
    var current = start
    var counted = 0

    while (counted < numRangeElements && p(current)) {
      counted += 1
      current += step
    }
    counted
  }

  // Tests whether a number is within the endpoints, without testing
  // whether it is a member of the sequence (i.e. when step > 1.)
  private def isWithinBoundaries(elem: Epoch) = !isEmpty && (
    (step > 0 && start <= elem && elem <= last) ||
      (step < 0 && last <= elem && elem <= start)
    )

  // Methods like apply throw exceptions on invalid n, but methods like take/drop
  // are forgiving: therefore the checks are with the methods.
  private def locationAfterN(n: Int) = start + (step * n)

  // When one drops everything.  Can't ever have unchecked operations
  // like "end + 1" or "end - 1" because ranges involving Int.{ MinValue, MaxValue }
  // will overflow.  This creates an exclusive range where start == end
  // based on the given value.
  private def newEmptyRange(value: Epoch) = new TimeRange(value, value, step)

  final override def takeWhile(p: Epoch => Boolean): TimeRange = take(skipCount(p))

  final override def dropWhile(p: Epoch => Boolean): TimeRange = drop(skipCount(p))

  final override def span(p: Epoch => Boolean): (TimeRange, TimeRange) = splitAt(skipCount(p))

  /** Creates a pair of new ranges, first consisting of elements before `n`, and the second
    * of elements after `n`.
    *
    * $doesNotUseBuilders
    */
  final override def splitAt(n: Int) = (take(n), drop(n))

  /** Creates a new range consisting of the `length - n` last elements of the range.
    *
    * $doesNotUseBuilders
    */
  final override def takeRight(n: Int): TimeRange = drop(numRangeElements - n)

  /** Creates a new range consisting of the initial `length - n` elements of the range.
    *
    * $doesNotUseBuilders
    */
  final override def dropRight(n: Int): TimeRange = take(numRangeElements - n)

  /** Returns the reverse of this range.
    *
    * $doesNotUseBuilders
    */
  final override def reverse: TimeRange =
    if (isEmpty) this
    else new TimeRange.Inclusive(last, start, -step)

  /** Make range inclusive.
    */
  def inclusive =
    if (isInclusive) this
    else new TimeRange.Inclusive(start, end, step)

  final def contains(x: Epoch) = isWithinBoundaries(x) && (x.relativeTo(start) % step == 0)

  override def toIterable = this

  override def toSeq = this

  override def equals(other: Any) = other match {
    case x: TimeRange =>
      (x canEqual this) && (length == x.length) && (
        isEmpty || // all empty sequences are equal
          (start == x.start && last == x.last) // same length and same endpoints implies equality
        )
    case _ =>
      super.equals(other)
  }

  override def toString() = {
    val endStr = if (numRangeElements > 512) ", ... )" else ")"
    take(512).mkString("Range(", ", ", endStr)
  }
}

/**
 * A companion object for the `TimeRange` class.
 */
object TimeRange {
  /**
   * Counts the number of range elements.
   * pre:  step != 0
   *
   * If the size of the range exceeds Int.MaxValue, the result will be negative.
   */
  def count(start: Int, end: Int, step: Int, isInclusive: Boolean): Int = {
    if (step == 0)
      throw new IllegalArgumentException("step cannot be 0.")

    val isEmpty =
      if (start == end) !isInclusive
      else if (start < end) step < 0
      else step > 0
    if (isEmpty) 0
    else {
      // Counts with Longs so we can recognize too-large ranges.
      val gap: Long = end.toLong - start.toLong
      val jumps: Long = gap / step
      // Whether the size of this range is one larger than the
      // number of full-sized jumps.
      val hasStub = isInclusive || (gap % step != 0)
      val result: Long = jumps + (if (hasStub) 1 else 0)

      if (result > scala.Int.MaxValue) -1
      else result.toInt
    }
  }

  def count(start: Int, end: Int, step: Int): Int =
    count(start, end, step, isInclusive = false)

  class Inclusive(start: Epoch, end: Epoch, step: Double) extends TimeRange(start, end, step) {
    //    override def par = new ParRange(this)
    override def isInclusive = true

    override protected def copy(start: Epoch, end: Epoch, step: Double): TimeRange = new Inclusive(start, end, step)
  }

  /** Make a range from `start` until `end` (exclusive) with given step value.
    * @note step != 0
    */
  def apply(start: Epoch, end: Epoch, step: Double): TimeRange = new TimeRange(start, end, step)

  /** Make a range from `start` until `end` (exclusive) with step value 1.
    */
  def apply(start: Epoch, end: Epoch): TimeRange = new TimeRange(start, end, 1)

  /** Make an inclusive range from `start` to `end` with given step value.
    * @note step != 0
    */
  def inclusive(start: Epoch, end: Epoch, step: Double): TimeRange.Inclusive = new Inclusive(start, end, step)

  /** Make an inclusive range from `start` to `end` with step value 1.
    */
  def inclusive(start: Epoch, end: Epoch): TimeRange.Inclusive = new Inclusive(start, end, 1)


}