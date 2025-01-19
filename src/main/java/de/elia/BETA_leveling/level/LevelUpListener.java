package de.elia.BETA_leveling.level;

import de.elia.api.events.level.LevelUpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import static de.elia.api.messages.builder.MessageBuilder.*;

public class LevelUpListener implements Listener {

    @EventHandler
    private void onLevelUp(@NotNull LevelUpEvent event){
        Player player = event.getPlayer();
        message(player, gray("Du bist von Level ").append(aqua(String.valueOf(event.getOldLevel()))).append(gray(" zu Level ")).append(aqua(String.valueOf(event.getNewLevel()))).append(gray(" aufgestiegen!")));
    }

}
