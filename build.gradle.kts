//import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
  `java-library`
  //id("io.papermc.paperweight.userdev") version "1.7.3"
  id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
  id("xyz.jpenilla.run-paper") version "2.3.0" // Adds runServer and runMojangMappedServer tasks for testing
  //id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1" // Generates plugin.yml based on the Gradle config
}

group = "de.elia"
version = "4.0.0-BETA"
description = "Dev-Plugin für SoulSMP"

java {
  // Configure the java toolchain. This allows Gradle to auto-provision JDK 21 on systems that only have JDK 11 installed, for example.
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

// 1)
// For >=1.20.5 when you don't care about supporting spigot
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

// 2)
// For 1.20.4 or below, or when you care about supporting Spigot on >=1.20.5
// Configure reobfJar to run when invoking the build task
tasks.assemble {
  dependsOn(tasks.reobfJar)
}

repositories {
  mavenCentral()
  maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
  maven { url = uri("https://maven.enginehub.org/repo/") }
  flatDir {
    dirs("libraries")
  }
}

dependencies {
  paperweight.paperDevBundle("1.21.5-R0.1-SNAPSHOT")
  implementation(platform("com.intellectualsites.bom:bom-newest:1.37"))
  implementation("com.fastasyncworldedit:FastAsyncWorldEdit-Core:2.9.2")
  implementation(platform("com.intellectualsites.bom:bom-newest:1.52")) // Ref: https://github.com/IntellectualSites/bom
  compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
  compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }
  implementation("com.mysql:mysql-connector-j:9.2.0")
  implementation("de.elia.api:SoulLibrary:5.0.0")
  implementation("org.apache.logging.log4j:log4j-api:3.0.0-beta2")
  implementation("org.apache.logging.log4j:log4j-core:3.0.0-beta2")
  // paperweight.foliaDevBundle("1.21-R0.1-SNAPSHOT")
  // paperweight.devBundle("com.example.paperfork", "1.21-R0.1-SNAPSHOT")
}

tasks {
  compileJava {
    // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
    // See https://openjdk.java.net/jeps/247 for more information.
    options.release = 21

  }
  javadoc {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
  }

  // Only relevant when going with option 2 above
  /*
  reobfJar {
    // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
    // for a variety of reasons, however, it's asked frequently enough that an example of how to do it is included here.
    outputJar = layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar")
  }
   */
}

/*
// Configure plugin.yml generation
// - name, version, and description are inherited from the Gradle project.
bukkitPluginYaml {
  main = "io.papermc.paperweight.testplugin.TestPlugin"
  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
  authors.add("Author")
  apiVersion = "1.21"
}

 */
