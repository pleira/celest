package be.angelcorp.libs.celest.universe.modules

import javax.inject.{Named, Singleton}
import com.google.inject.{Provides, AbstractModule}
import com.google.inject.name.Names
import org.apache.maven.repository.internal.MavenAetherModule
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.transport.file.FileTransporterFactory
import org.eclipse.aether.transport.http.HttpTransporterFactory
import be.angelcorp.libs.celest.data.AetherInterface
import java.nio.file.{Paths, Path}
import org.eclipse.aether.repository.RemoteRepository

/**
 * This module configures Aether, and how it resolves resource dependencies using different repositories.
 *
 * This modules relies on a basic maven layout of the resources, and stores the retrieved resource in a local
 * repository in the "./repository" director.
 */
class DefaultAether extends AbstractModule {

  override def configure() {
    install( new MavenAetherModule() )
    bind( classOf[RepositoryConnectorFactory] ).annotatedWith( Names.named( "basic" ) ).to( classOf[BasicRepositoryConnectorFactory] )
    bind( classOf[TransporterFactory] ).annotatedWith( Names.named( "file" ) ).to( classOf[FileTransporterFactory] )
    bind( classOf[TransporterFactory] ).annotatedWith( Names.named( "http" ) ).to( classOf[HttpTransporterFactory] )
  }

  @Provides
  @Named("local_repository")
  @Singleton
  def provideLocalRepository = Paths.get("repository")

  @Provides
  @Singleton
  def provideRepositoryConnectorFactories( @Named( "basic" ) basic: RepositoryConnectorFactory ): java.util.Set[RepositoryConnectorFactory] = {
    val collection = new java.util.HashSet[RepositoryConnectorFactory]()
    collection.add( basic )
    collection
  }

  @Provides
  @Singleton
  def provideTransporterFactories( @Named( "file" ) file: TransporterFactory, @Named( "http" ) http: TransporterFactory ): java.util.Set[TransporterFactory] = {
    val collection = new java.util.HashSet[TransporterFactory]()
    collection.add( file )
    collection.add( http )
    collection
  }

}
