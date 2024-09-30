package de.elia.soulboss.entitys.creeper;

import de.elia.soulboss.entitys.BossEntity;
import de.elia.systemclasses.keys.NameSpacedKeys;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;

import org.bukkit.craftbukkit.CraftWorld;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

//This class creates the creeper boss
public class CreeperBoss extends Creeper implements BossEntity {

  private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
  private final String name;
  private final int explosionRadius = 2;

  public CreeperBoss(@NotNull Location location, String name, @Nullable Float health, UUID uuid) {
    super(EntityType.CREEPER, ((CraftWorld)location.getWorld()).getHandle());
    LivingEntity entity = (LivingEntity) this.getBukkitEntity();
    this.name = name;
    this.getBukkitEntity().getPersistentDataContainer().set(NameSpacedKeys.CREEPER_KEY.key(), PersistentDataType.BYTE, (byte) 1);
    if (uuid == null) {
      this.setUUID(UUID.randomUUID());
    }else this.setUUID(uuid);
    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000D);
    if (health == null) {
      this.setHealth(750F);
    }else this.setHealth(health);
    this.setCustomName(Component.literal(name));
    entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999999, 255, false, false, false));
    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 999999999, 5, false, false, false));
    entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 3, false, false, false));
    this.setPos(location.x(), location.y(), location.z());
    this.goalRegister();
    ((CraftWorld)location.getWorld()).getHandle().addFreshEntity(this);
  }

  public void goalRegister(){
    this.goalSelector.removeGoal(new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 20.F));
  }

  @NotNull
  public String name() {
    return this.name;
  }

  @Override
  public int getMaxFallDistance() {
    return this.getTarget() == null ? 3 : 3 + (int) (this.getHealth() - 0.0F); //Edit by Elia
  }

  @Override
  public float health(){
    return this.getHealth();
  }

  @Override
  public float maxHealth(){
    return this.getMaxHealth();
  }

}
