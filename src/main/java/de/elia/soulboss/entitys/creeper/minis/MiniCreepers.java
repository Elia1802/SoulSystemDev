package de.elia.soulboss.entitys.creeper.minis;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.jetbrains.annotations.NotNull;

import java.lang.String;

//This class creates a creeper for the mini attack
public class MiniCreepers extends Creeper {

  private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);

  public MiniCreepers(@NotNull Location location, String name) {
    super(EntityType.CREEPER, ((CraftWorld)location.getWorld()).getHandle());
    this.setCustomName(Component.literal(name));
    this.setPos(location.x(), location.y(), location.z());
    LivingEntity entity = (LivingEntity) this.getBukkitEntity();
    entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999999, 255, false, false, false));
    ((CraftWorld)location.getWorld()).getHandle().addFreshEntity(this);
  }

  @Override
  public void explodeCreeper() {
    if (!this.level().isClientSide) {
      float f = this.isPowered() ? 2.0F : 1.0F;

      // CraftBukkit start
      ExplosionPrimeEvent event = CraftEventFactory.callExplosionPrimeEvent(this, this.explosionRadius * f, false);
      if (!event.isCancelled()) {
        // CraftBukkit end
        this.dead = true;
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), event.getRadius(), event.getFire(), Level.ExplosionInteraction.MOB); // CraftBukkit
        this.discard();
        //this.spawnLingeringCloud(); //Removed by Elia
        // CraftBukkit start
      } else {
        this.swell = 0;
        this.entityData.set(DATA_IS_IGNITED, Boolean.valueOf(false)); // Paper
      }
      // CraftBukkit end
    }

  }

}
