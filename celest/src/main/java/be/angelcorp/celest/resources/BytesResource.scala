package be.angelcorp.celest.resources

import java.io.{InputStreamReader, ByteArrayInputStream, Reader, InputStream}
import java.net.URI
import java.nio.ByteBuffer

import scala.io.Source

class BytesResource( val bytes: Array[Byte], val uri: URI ) extends Resource {

  /**
   * Creates a new source stream reading in this resource.
   */
  override def openSource(): Source =
    Source.fromBytes( bytes )

  /**
   * Creates a new Reader, reading this resource.
   */
  override def openReader(): Reader =
    new InputStreamReader( openStream() )

  /**
   * Creates a new InputStream, reading from this resource.
   */
  override def openStream(): InputStream =
    new ByteArrayInputStream(bytes)

  /**
   * Creates a new bytebuffer, containing the binary data of this resource.
   */
  override def openByteBuffer(): ByteBuffer =
    ByteBuffer.wrap( bytes )

}
