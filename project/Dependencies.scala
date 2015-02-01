
import sbt._

object Dependencies {
  val aetherVersion = "1.0.0.v20140518"
  val mavenVersion  = "3.1.0"
  val wagonVersion  = "1.0"

  val commonsMath =     "org.apache.commons"          %  "commons-math3"            % "3.3"
  val orekit =          "org.orekit"                  %  "orekit"                   % "6.1"
  val sofa =            "be.angelcorp"                %  "sofa"                     % "1.1-SNAPSHOT"

  val config =          "com.typesafe"                %  "config"                   % "1.2.1"
  val guava =           "com.google.guava"            %  "guava"                    % "17.0"
  val jgrapht =         "net.sf.jgrapht"              %  "jgrapht"                  % "0.8.3"
  val jsr305 =          "com.google.code.findbugs"    %  "jsr305"                   % "2.0.3" // Required for guava
  val jfreechart =      "org.jfree"                   %  "jfreechart"               % "1.0.19"
  val logbackClassic =  "ch.qos.logback"              %  "logback-classic"          % "1.0.13"
  val miglayout =       "com.miglayout"               %  "miglayout-swing"          % "5.0"
  val reflections =     "org.reflections"             %  "reflections"              % "0.9.9-RC1" exclude( "com.google.guava", "guava" )
  val scalaGlsl =       "be.angelcorp.scala-glsl"     %% "core"                     % "1.0.0-SNAPSHOT"
  val scalaGuice =      "net.codingwell"              %% "scala-guice"              % "4.0.0-beta5"
  val scalaLogging =    "com.typesafe.scala-logging"  %% "scala-logging-slf4j"      % "2.1.2"
  val scalaParser =     "org.scala-lang.modules"      %% "scala-parser-combinators" % "1.0.2"
  val scalaTest =       "org.scalatest"               %% "scalatest"                % "2.2.1"       % "test"
  val servletApi =      "javax.servlet"               %  "servlet-api"              % "2.5" // Required for reflections

  val indexer =         "org.apache.maven.indexer"    %  "indexer-core"             % "6.0-SNAPSHOT"
  val wagonHttp =       "org.apache.maven.wagon"      %  "wagon-http-lightweight"   % "2.3"
  val indexerAll = Seq( indexer, wagonHttp )

  val aetherApi =       "org.eclipse.aether"          %  "aether-api"               % aetherVersion
  val aetherImpl =      "org.eclipse.aether"          %  "aether-impl"              % aetherVersion
  val aetherBasic =     "org.eclipse.aether"          %  "aether-connector-basic"   % aetherVersion
  val aetherFile =      "org.eclipse.aether"          %  "aether-transport-file"    % aetherVersion
  val aetherHttp =      "org.eclipse.aether"          %  "aether-transport-http"    % aetherVersion
  val aetherWagon =     "org.eclipse.aether"          %  "aether-transport-wagon"   % aetherVersion
  val apacheAether =    "org.apache.maven"            %  "maven-aether-provider"    % mavenVersion
  val apacheWagonSsh =  "org.apache.maven.wagon"      %  "wagon-ssh"                % wagonVersion
  val aetherAll = Seq( aetherApi, aetherImpl, aetherBasic, aetherFile, aetherHttp, aetherWagon, apacheAether, apacheWagonSsh )

}
