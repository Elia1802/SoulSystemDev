package de.elia.systemclasses.keys;

import de.elia.Main;

import org.bukkit.NamespacedKey;

//Here all final NamespacedKeys saved of this plugin
public enum NameSpacedKeys {

  ZOMBIE_KEY(new NamespacedKey(Main.main(), "680035753")),
  ZOMBIE_RECIPE_KEY(new NamespacedKey(Main.main(), "v4j23hdu-df4e-36ta-r4sf-2n0c3n8cky9x")),
  CREEPER_KEY(new NamespacedKey(Main.main(), "212429935")),
  CREEPER_RECIPE_KEY(new NamespacedKey(Main.main(), "f0d3j29s-9s2a-wdas-2eay-c93ks0all2sa"));

  private final NamespacedKey key;

  private NameSpacedKeys(NamespacedKey key) {
    this.key = key;
  }

  public NamespacedKey key() {
    return this.key;
  }
}
