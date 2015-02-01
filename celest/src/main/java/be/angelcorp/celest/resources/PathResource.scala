package be.angelcorp.celest.resources

import java.io.{File, RandomAccessFile, Reader, InputStream}
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.nio.file.{Files, Path}

import scala.io.Source

/**
 * Represents a resolved resource that exists on the local filesystem.
 */
case class PathResource( path: Path ) extends Resource {

  override def uri =
    path.toUri

  override def openStream(): InputStream =
    Files.newInputStream(path)

  /**
   * Creates a new (memory-mapped) bytebuffer, containing the binary data of this resource
   *
   * <a name="mode"><p> The <tt>mode</tt> argument specifies the access mode in which the file is to be opened (see [[java.io.RandomAccessFile]]).
   * The permitted values and their meanings are:
   *
   * <blockquote><table summary="Access mode permitted values and meanings">
   * <tr><th><p align="left">Value</p></th><th><p align="left">Meaning</p></th></tr>
   * <tr>
   *    <td valign="top"><tt>"r"</tt></td>
   *    <td> Open for reading only.  Invoking any of the <tt>write</tt> methods of the resulting object will cause an [[java.io.IOException]] to be thrown. </td>
   * </tr>
   * <tr>
   *   <td valign="top"><tt>"rw"</tt></td>
   *   <td> Open for reading and writing.  If the file does not already exist then an attempt will be made to create it. </td>
   * </tr>
   * <tr>
   *   <td valign="top"><tt>"rws"</tt></td>
   *   <td> Open for reading and writing, as with <tt>"rw"</tt>, and also require that every update to the file's content or metadata be written synchronously to the underlying storage device.  </td>
   * </tr>
   * <tr>
   *   <td valign="top"><tt>"rwd"&nbsp;&nbsp;</tt></td>
   *   <td> Open for reading and writing, as with <tt>"rw"</tt>, and also require that every update to the file's content be written synchronously to the underlying storage device. </td>
   * </tr>
   * </table></blockquote>
   *
   * @param mode the access mode, as described <a href="#mode">above</a> or in [[java.io.RandomAccessFile]]
   */
  def openByteBuffer(mode: String ): ByteBuffer = {
    val memoryMappedFile = new RandomAccessFile(path.toFile, "rw")
    val mapMode = if (mode == "r") FileChannel.MapMode.READ_ONLY else FileChannel.MapMode.READ_WRITE
    val size = Files.size(path)
    memoryMappedFile.getChannel.map(mapMode, 0, size)
  }

  override def openByteBuffer(): ByteBuffer =
    openByteBuffer("r")

  override def openReader(): Reader =
    Files.newBufferedReader( path, Charset.defaultCharset() )

  override def openSource() =
    Source.fromFile(path.toFile)

}

object PathResource {

  /**
   * Returns the specified resource as a PathResource.
   *
   * If the resource is not yet a PathResource, the resource content is written to a temporary file on the system, and a
   * PathResource to that temp-file is returned.
   *
   * This can be useful for algorithms requiring a file on the local filesystem.
   */
  def apply( res: Resource ): PathResource = res match {
    case pres: PathResource => pres
    case _ =>
      val path = res.uri.getPath
      val index = path.lastIndexOf('/')
      val fname = if (index < -1) path else  path.substring(index + 1)
      val tempPath = Files.createTempFile(fname, ".pathResource")
      tempPath.toFile.deleteOnExit()
      Files.copy(res.openStream(), tempPath)
      new PathResource( tempPath )
  }

}
