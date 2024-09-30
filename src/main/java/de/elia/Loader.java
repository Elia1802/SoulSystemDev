package de.elia;

import de.elia.PluginInfo.SoulLibrary;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.JarLibrary;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;

import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static de.elia.PluginInfo.NAME;
import static de.elia.PluginInfo.VERSION;

//This class loading all libraries of this plugin
public class Loader implements PluginLoader {

  public static final Map<Class, Map<String, String>> BETA_CLASSES = new HashMap<>();

  public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
    var logger = classpathBuilder.getContext().getLogger();
    logger.info("Loading " + NAME + " " + VERSION + " libraries...");
    String soulLibrary = SoulLibrary.NAME + "-" + SoulLibrary.VERSION + ".jar";
    logger.info("Loading {}", soulLibrary);
    String projectPath = System.getProperty("user.dir");
    String libraryPath = projectPath + "/plugins/";
    classpathBuilder.addLibrary(new JarLibrary(Path.of(libraryPath + soulLibrary)));
    logger.info("{} loaded!", soulLibrary);
    logger.info("Loading Log4J-API 3.0.0-beta2 ...");
    MavenLibraryResolver log4jAPI = new MavenLibraryResolver();
    log4jAPI.addDependency(new Dependency(new DefaultArtifact("org.apache.logging.log4j:log4j-api:3.0.0-beta2"), null));
    log4jAPI.addRepository(new RemoteRepository.Builder("log4j-api", "default", "https://repo.papermc.io/repository/maven-public/").build());
    classpathBuilder.addLibrary(log4jAPI);
    logger.info("Log4J-API 3.0.0-beta2 loaded!");
    logger.info("Loading Log4J-Core 3.0.0-beta2 ...");
    MavenLibraryResolver log4jCore = new MavenLibraryResolver();
    log4jCore.addDependency(new Dependency(new DefaultArtifact("org.apache.logging.log4j:log4j-core:3.0.0-beta2"), null));
    log4jCore.addRepository(new RemoteRepository.Builder("log4j-core", "default", "https://repo.papermc.io/repository/maven-public/").build());
    classpathBuilder.addLibrary(log4jCore);
    logger.info("Log4J-Core 3.0.0-beta2 loaded!");
    logger.info(NAME + " " + VERSION + " libraries loaded!");
  }


}
