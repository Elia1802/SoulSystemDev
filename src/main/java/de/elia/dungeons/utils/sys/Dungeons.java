package de.elia.dungeons.utils.sys;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import org.jetbrains.annotations.NotNull;

public enum Dungeons {

  DUNGEON("dungeon_1", "353831",DungeonState.UNLOAD, Bukkit.getWorld("world"), 0, 100, 0);

  private final String dungeonName;
  private final String id;
  private DungeonState state;
  private final World world;
  private final double x;
  private final double y;
  private final double z;

  Dungeons(String dungeonName, String id, DungeonState state, @NotNull World world, double x, double y, double z){
    this.dungeonName = dungeonName;
    this.id = id;
    this.state = state;
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @NotNull
  public String dungeonName(){
    return this.dungeonName;
  }

  @NotNull
  public String id(){
    return this.id;
  }

  @NotNull
  public DungeonState state(){
    return this.state;
  }

  public void state(DungeonState newState){
    this.state = newState;
  }

  @NotNull
  public Location spawnLocation(){
    return new Location(this.world, this.x, this.y, this.z);
  }
}
