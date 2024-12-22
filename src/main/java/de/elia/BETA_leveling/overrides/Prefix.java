package de.elia.overrides;

import de.elia.api.messages.prefix.PrefixClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

@Deprecated//IDK
public class Prefix extends PrefixClass {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public @NotNull Component prefix() {
        return this.miniMessage.deserialize("<dark_grey>[<#9545a3>SoulSMP LevelSystem</#9545a3>]</dark_grey> ");
    }

}
