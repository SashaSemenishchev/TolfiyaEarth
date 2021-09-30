package me.mrfunny.plugins.spigot.tolfiyaearth.crafts.cauldron;

import org.bukkit.Material;

public class CauldronRecipe {
    private final CauldronRecipePiece[] pieces;
    private final Material result;
    private final int resultAmount;

    public CauldronRecipe(Material result, int resultAmount, CauldronRecipePiece... pieces){
        this.result = result;
        this.resultAmount = resultAmount;
        this.pieces = pieces;
    }

    public int getResultAmount() {
        return resultAmount;
    }

    public CauldronRecipePiece[] getPieces() {
        return pieces;
    }

    public Material getResult() {
        return result;
    }
}
