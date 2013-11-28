package be.angelcorp.celest.util

import java.nio.{Buffer, ByteBuffer}
import java.nio.charset.Charset

class RichByteBuffer(val buffer: ByteBuffer, val alignment: AlignmentStrategy) {
  val ccharset = Charset.forName("ISO-8859-1")

  /**
   * Read a c-string from a bytebuffer.
   *
   * @param length Length of the string to read, including optional null char.
   * @return       The read string, until the null char (exclusive) or of the maximum length.
   */
  def getCString(length: Int) = {
    // Converts c++ single byte char to java's double byte
    val charView = ccharset.decode(buffer.slice())

    // Read the string
    val chars = Array.ofDim[Char](length)
    charView.get(chars)

    // Move the position on the data buffer forward with the amount of chars read
    buffer.position(buffer.position + length)

    // Return the string (until the null delimiter)
    if (chars.contains(0)) new String(chars, 0, chars.indexOf(0))
    else new String(chars)
  }

  /**
   * Store a string in the bytebuffer using c (1 byte, ISO-8859-1) encoding.
   *
   * After the string is stored, the position in the buffer is moved `length` number of bytes.
   *
   * @param str     String to store
   * @param length  Amount of charachters/bytes allocated for the string.
   */
  def putCString(str: String, length: Int) = {
    // Converts java's double byte to c++ single byte char
    val bytes = str.getBytes(ccharset)

    // End position after the string is inserted, with additional (optional) padding
    val endPosition = buffer.position + length

    // Store the encoded string
    buffer.put(bytes)

    // Move the position on the data buffer to the required end position
    buffer.position(endPosition)
  }

  /** Store a string in the bytebuffer using c (1 byte, ISO-8859-1) encoding. */
  def putCString(str: String) = {
    // Converts java's double byte to c++ single byte char
    val bytes = str.getBytes(ccharset)
    // Store the encoded string
    buffer.put(bytes)
  }

  /** Retrieve the string stored in the bytebuffer (1 byte, ISO-8859-1 encoding) starting at the current position, and fixed length (including null char) */
  def getAlignedCString(length: Int) = {
    alignment.alignToCChar(buffer)
    getCString(length)
  }

  /** Store a string stored in the bytebuffer (1 byte, ISO-8859-1 encoding) */
  def putAlignedCString(str: String) = {
    alignment.alignToCChar(buffer)
    putCString(str)
  }

  /** Store a string stored in the bytebuffer (1 byte, ISO-8859-1 encoding) over a predefined number of bytes */
  def putAlignedCString(str: String, length: Int) = {
    alignment.alignToCChar(buffer)
    putCString(str, length)
  }

  /** Aligns the buffer, reads the byte at the new aligned position, and increments the position by 1 byte. */
  def getAlignedByte =
    buffer.get

  /** Aligns the buffer, stores the byte at the new aligned position, and increments the position by 1 byte. */
  def putAlignedByte(b: Byte) =
    buffer.put(b)

  /** Aligns the buffer, reads the char at the new aligned position, and increments the position by 1 byte. */
  def getAlignedCChar =
    getAlignedCString(1)

  /** Aligns the buffer, stores the char at the new aligned position, and increments the position by 1 byte. */
  def putAlignedCChar(c: Char) =
    putAlignedCString(c.toString)

  /** Aligns the buffer, reads the short at the new aligned position, and increments the position by 2 bytes. */
  def getAlignedShort = {
    alignment.alignToShort(buffer)
    buffer.getShort
  }

  /** Aligns the buffer, stores the short at the new aligned position, and increments the position by 2 bytes. */
  def putAlignedShort(s: Short) = {
    alignment.alignToShort(buffer)
    buffer.putShort(s)
  }

  /** Aligns the buffer, reads the integer at the new aligned position, and increments the position by 4 bytes. */
  def getAlignedInt = {
    alignment.alignToInt(buffer)
    buffer.getInt
  }

  /** Aligns the buffer, stores the integer at the new aligned position, and increments the position by 4 bytes. */
  def putAlignedInt(i: Int) = {
    alignment.alignToInt(buffer)
    buffer.putInt(i)
  }

  /** Aligns the buffer, reads the long at the new aligned position, and increments the position by 8 bytes. */
  def getAlignedLong = {
    alignment.alignToLong(buffer)
    buffer.getLong
  }

  /** Aligns the buffer, stores the long at the new aligned position, and increments the position by 8 bytes. */
  def putAlignedLong(l: Long) = {
    alignment.alignToLong(buffer)
    buffer.putLong(l)
  }

  /** Aligns the buffer, reads the float at the new aligned position, and increments the position by 4 bytes. */
  def getAlignedFloat = {
    alignment.alignToFloat(buffer)
    buffer.getFloat
  }

  /** Aligns the buffer, stores the float at the new aligned position, and increments the position by 4 bytes. */
  def putAlignedFloat(f: Float) = {
    alignment.alignToFloat(buffer)
    buffer.putFloat(f)
  }

  /** Aligns the buffer, reads the double at the new aligned position, and increments the position by 8 bytes. */
  def getAlignedDouble = {
    alignment.alignToDouble(buffer)
    buffer.getDouble
  }

  /** Aligns the buffer, stores the double at the new aligned position, and increments the position by 8 bytes. */
  def putAlignedDouble(d: Double) = {
    alignment.alignToDouble(buffer)
    buffer.putDouble(d)
  }

}

/**
 * Strategy to realign a ByteBuffer to the next byte/char/short/int/long/float/double
 *
 * This strategy is c-compiler dependant.
 */
trait AlignmentStrategy {
  def alignToByte(bb: ByteBuffer): Buffer

  def alignToCChar(bb: ByteBuffer): Buffer

  def alignToShort(bb: ByteBuffer): Buffer

  def alignToInt(bb: ByteBuffer): Buffer

  def alignToLong(bb: ByteBuffer): Buffer

  def alignToFloat(bb: ByteBuffer): Buffer

  def alignToDouble(bb: ByteBuffer): Buffer

  /**
   * Method that computes the next aligned position from the current offset and the byte alignment.
   * @param offset Position in the data buffer.
   * @param align  Number of bytes to align to.
   * @return Position of the next aligned byte.
   */
  protected def align(offset: Int, align: Int): Int =
    offset + (align - (offset % align)) % align

}

/** Alignment strategy that inserts no additional padding. */
object PackedAlignment extends AlignmentStrategy {
  def instance = this

  def alignToByte(bb: ByteBuffer) = bb

  def alignToCChar(bb: ByteBuffer) = bb

  def alignToShort(bb: ByteBuffer) = bb

  def alignToInt(bb: ByteBuffer) = bb

  def alignToLong(bb: ByteBuffer) = bb

  def alignToFloat(bb: ByteBuffer) = bb

  def alignToDouble(bb: ByteBuffer) = bb
}

/** Default alignment strategy used by Microsoft visual studio x86 compiler */
object MsvcX86Alignment extends AlignmentStrategy {
  def instance = this

  def alignToByte(bb: ByteBuffer) = bb

  def alignToCChar(bb: ByteBuffer) = bb

  def alignToShort(bb: ByteBuffer) = bb.position(align(bb.position, 2))

  def alignToInt(bb: ByteBuffer) = bb.position(align(bb.position, 4))

  def alignToLong(bb: ByteBuffer) = bb.position(align(bb.position, 4))

  def alignToFloat(bb: ByteBuffer) = bb.position(align(bb.position, 4))

  def alignToDouble(bb: ByteBuffer) = bb.position(align(bb.position, 8))
}

/** Default alignment strategy used by gcc x86 compiler */
object GccX86Alignment extends AlignmentStrategy {
  def instance = this

  def alignToByte(bb: ByteBuffer) = bb

  def alignToCChar(bb: ByteBuffer) = bb

  def alignToShort(bb: ByteBuffer) = bb.position(align(bb.position, 2))

  def alignToInt(bb: ByteBuffer) = bb.position(align(bb.position, 4))

  def alignToLong(bb: ByteBuffer) = bb.position(align(bb.position, 4))

  def alignToFloat(bb: ByteBuffer) = bb.position(align(bb.position, 4))

  def alignToDouble(bb: ByteBuffer) = bb.position(align(bb.position, 4))
}

/** Default alignment strategy used by gcc x64 and Microsoft visual studio x64 compiler */
object X64Alignment extends AlignmentStrategy {
  def instance = this

  def alignToByte(bb: ByteBuffer) = bb

  def alignToCChar(bb: ByteBuffer) = bb

  def alignToShort(bb: ByteBuffer) = bb.position(align(bb.position, 2))

  def alignToInt(bb: ByteBuffer) = bb.position(align(bb.position, 4))

  def alignToLong(bb: ByteBuffer) = bb.position(align(bb.position, 8))

  def alignToFloat(bb: ByteBuffer) = bb.position(align(bb.position, 4))

  def alignToDouble(bb: ByteBuffer) = bb.position(align(bb.position, 8))
}
