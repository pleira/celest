/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.angelcorp.libs.celest

import scala.io.{BufferedSource, Source}
import java.io.File
import com.google.common.io.Files
import java.nio.charset.Charset
import java.net.URI
import org.slf4j.LoggerFactory

package object data {
  val logger = LoggerFactory.getLogger( "data" )

  /**
   * Function to find the source of a file given a specific lookup id.
   *
   * If the file was locally cached, the content of the local file is returned. If no local cache exists, this
   * function tries to find the correct file on the celest content server. When no match is found on the celest content
   * server, it tries to load the data from the specified fallback uri's.
   *
   * @param id
   * @param fallbackUrls
   * @return
   */
  def getDataFile( id: String, fallbackUrls: Seq[URI] = Nil ): Option[Source] = {
    val file            = new File( id )
    val defaultFallback = "http://angelcorp.be/celest/resources/" + id

    val fallback = URI.create(defaultFallback) :: fallbackUrls.toList

    if ( file.exists() ) {
      // Load the local cached file
      Some( Source.fromFile(file) )
    } else {
      // Iterate over all the fallback url's and try to download the file:
      fallback.foldLeft( None: Option[BufferedSource] )( (source, uri) => source match {
        // File already downloaded
        case Some(data) => source
        // Try this new url
        case _ =>
          try {
            // Try the download
            val data = Source.fromURL( uri.toURL )
            try {
              // Download success, try and save the file locally (as cache)
              Files.createParentDirs(file)
              val writer = Files.newWriter( file, Charset.defaultCharset() )
              writer.write( data.toArray )
              writer.close()
            } catch { case _: Throwable => }
            Some(data)
          } catch {
            // Download failed
            case e: Throwable =>
              logger.info("Failed to retrieve data file %s from the uri %s:".format(id, uri), e)
              None
          }
      } )
    }
  }

}
