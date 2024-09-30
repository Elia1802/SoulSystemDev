package de.elia.systemclasses.messages.prefix;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.jetbrains.annotations.NotNull;

//This class set the prefix for this plugin
public class Prefix {
  @NotNull
  public static Component prefix() {
    MiniMessage miniMessage = MiniMessage.miniMessage();
    return miniMessage.deserialize("<dark_gray>[</dark_gray><dark_purple>Soul-BossFight</dark_purple><dark_gray>]</dark_gray> ");
  }
}
