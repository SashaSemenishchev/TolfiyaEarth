package me.mrfunny.plugins.spigot.tolfiyaearth;

import me.mrfunny.plugins.spigot.tolfiyaearth.crafts.cauldron.CauldronListener;
import me.mrfunny.plugins.spigot.tolfiyaearth.lifecycle.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class TolfiyaEarth extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new CauldronListener(this), this);
        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(this, "string_from_wool"), new ItemStack(Material.STRING, 4));
        recipe.addIngredient(Material.WHITE_WOOL);
        Bukkit.addRecipe(recipe);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
