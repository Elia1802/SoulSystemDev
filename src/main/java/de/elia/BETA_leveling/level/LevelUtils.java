package de.elia.BETA_leveling.level;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.elia.Main;
import de.elia.api.events.level.LevelUpEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class LevelUtils {

    private static final NamespacedKey POINT_KEY = new NamespacedKey(Main.main(), "points");

    public static void addPoints(@NotNull Player player, double amount){
        double oldPoints = getPoints(player);
        double newPoints = oldPoints + amount;
        setPoints(player, newPoints);
        callEventIfLevelUp(player, oldPoints, newPoints);
    }

    public static int getPoints(@NotNull Player player){
        var points = player.getPersistentDataContainer().get(POINT_KEY, PersistentDataType.INTEGER);
        if (points == null) {
            setPoints(player, 0);
            return 0;
        }else {
            return points;
        }
    }

    private static void setPoints(@NotNull Player player, double value){
        PLAYER_LEVEL_CACHE.invalidate(player.getUniqueId());
        player.getPersistentDataContainer().set(POINT_KEY,PersistentDataType.DOUBLE, value);
    }

    private static final Cache<UUID, Double> PLAYER_LEVEL_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .build();

    public static Double getLevel(@NotNull Player player, boolean useCache){
        Function<Player, Double> pointToLevelFunction = player1 -> convertPointToLevel(getPoints(player1));
        if (!useCache) return pointToLevelFunction.apply(player);
        return getOrFillCacheLevel(player, pointToLevelFunction);
    }

    private static Double getOrFillCacheLevel(@NotNull Player player, Function<Player, Double> fallback){
        Double cacheLevel = PLAYER_LEVEL_CACHE.getIfPresent(player.getUniqueId());
        if (cacheLevel != null) return cacheLevel;
        double level = fallback.apply(player);
        PLAYER_LEVEL_CACHE.put(player.getUniqueId(), level);
        return level;
    }

    @NotNull
    private static Double convertPointToLevel(double points){
        return Math.floor(Math.sqrt(points/10F));
    }

    private static void callEventIfLevelUp(Player player, double oldPoints, double newPoints){
        double oldLevels = convertPointToLevel(oldPoints);
        double newLevels = convertPointToLevel(newPoints);
        if (oldLevels < newLevels) {
            LevelUpEvent event = new LevelUpEvent(player, oldLevels, newLevels);
            Main.main().getServer().getPluginManager().callEvent(event);
        }
    }
}
