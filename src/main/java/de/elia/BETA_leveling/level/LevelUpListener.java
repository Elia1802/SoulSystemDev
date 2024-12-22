package de.elia.level;

import de.elia.api.events.level.LevelUpEvent;
import de.elia.api.messages.builder.MessageBuilder;
import de.elia.overrides.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import static de.elia.api.messages.builder.MessageBuilder.*;

public class LevelUpListener implements Listener {

    @EventHandler
    private void onLevelUp(@NotNull LevelUpEvent event){
        Player player = event.getPlayer();
        Message message = new Message();
        message(player, gray("Du bist von Level ").append(aqua(String.valueOf(event.getOldLevel()))).append(gray(" zu Level ")).append(aqua(String.valueOf(event.getNewLevel()))).append(gray(" aufgestiegen!")));
    }

}
