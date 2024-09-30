package de.elia.systemclasses.worldsettings;

import org.bukkit.block.Biome;

import org.jetbrains.annotations.Nullable;

//This record set the settings for the Chunkgenerator of the World "world_bossfight"
public record WorldSettings(@Nullable Biome biome,
                            boolean caves,
                            boolean decorations,
                            boolean mobs,
                            boolean noise,
                            boolean structures,
                            boolean surface) {

}

