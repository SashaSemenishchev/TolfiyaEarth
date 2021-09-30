package me.mrfunny.plugins.spigot.tolfiyaearth.crafts.cauldron;

import org.bukkit.Material;

public final class CauldronRecipePiece {
    private final Material material;
    private final int amount;

    public CauldronRecipePiece(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Material getMaterial() {
        return material;
    }
}
