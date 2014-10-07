package be.angelcorp.celest.resources

import java.net.URL
import java.nio.file.Path
import java.util.zip.ZipFile

import scala.collection.JavaConverters._
import scala.util.Try

class ZipResource( zipPath: Path ) extends PathResource( zipPath ) with ArchiveResource {
  val zip = new ZipFile( zipPath.toFile )

  override def entries: Iterator[String] =
    zip.entries().asScala.map( _.toString )

  override def findEntry(path: String): Try[Resource] = Try({
    val entry = zip.getEntry( path )
    val in = zip.getInputStream( entry )
    val url = new URL("jar:" + uri.toURL + "!/" + path)
    new InputStreamResource( in, url.toURI )
  })

}
