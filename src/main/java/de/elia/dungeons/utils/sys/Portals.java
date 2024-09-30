package de.elia.dungeons.utils.sys;

import org.jetbrains.annotations.NotNull;

public enum Portals {

  PORTAL_FOR_DUNGEON_ONE("portal_for_d1", Dungeons.DUNGEON.id(), PortalState.UNLOAD);

  private final String portalName;
  private final String id;
  private PortalState state;

  Portals(@NotNull String portalName, @NotNull String id, @NotNull PortalState state){
    this.portalName = portalName;
    this.id = id;
    this.state = state;
  }

  @NotNull
  public String portalName() {
    return this.portalName;
  }

  @NotNull
  public String id() {
    return this.id;
  }

  @NotNull
  public PortalState state() {
    return this.state;
  }

  public void state(@NotNull PortalState state) {
    this.state = state;
  }
}
