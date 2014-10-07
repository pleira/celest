package be.angelcorp.celest.resources

import java.io.{RandomAccessFile, Reader, InputStream}
import java.net.URI
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.nio.file.{Files, Path}

import scala.io.Source

/**
 * Represents a resolved resource.
 */
trait Resource {

  /**
   * The URI that can be used to identify this resource.
   */
  def uri: URI

  /**
   * Creates a new InputStream, reading from this resource.
   */
  def openStream(): InputStream

  /**
   * Creates a new bytebuffer, containing the binary data of this resource.
   */
  def openByteBuffer(): ByteBuffer

  /**
   * Creates a new Reader, reading this resource.
   */
  def openReader(): Reader

  /**
   * Creates a new source stream reading in this resource.
   */
  def openSource(): Source

}
