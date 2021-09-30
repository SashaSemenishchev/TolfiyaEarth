package me.mrfunny.plugins.spigot.tolfiyaearth.crafts.cauldron;

import me.mrfunny.plugins.spigot.tolfiyaearth.TolfiyaEarth;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

public class CauldronProcessor {

    private final static ArrayList<CauldronRecipe> recipes = new ArrayList<>();
    private final static ArrayList<CauldronProcessor> processors = new ArrayList<>();
    public static final HashMap<Item, ItemWaitTask> itemToTaskMap = new HashMap<>();

    static {
        recipes.add(new CauldronRecipe(Material.GUNPOWDER, 1,
                new CauldronRecipePiece(Material.COAL, 2),
                new CauldronRecipePiece(Material.RAW_IRON, 1),
                new CauldronRecipePiece(Material.GLOWSTONE_DUST, 2)
            ));
        recipes.add(new CauldronRecipe(Material.GLOWSTONE_DUST, 8,
                new CauldronRecipePiece(Material.GRAVEL, 1),
                new CauldronRecipePiece(Material.IRON_NUGGET, 1),
                new CauldronRecipePiece(Material.REDSTONE, 1),
                new CauldronRecipePiece(Material.RAW_GOLD, 1)
        ));
        recipes.add(new CauldronRecipe(Material.SLIME_BALL, 1,
                new CauldronRecipePiece(Material.SUGAR, 1),
                new CauldronRecipePiece(Material.GLOWSTONE_DUST, 1),
                new CauldronRecipePiece(Material.GREEN_DYE, 1)
        ));
    }

    private final HashMap<Material, Integer> howMuch = new HashMap<>();
    private final Block block;
    private final ArrayList<Item> droppedInThis = new ArrayList<>();
    private final SecureRandom random = new SecureRandom();
    private final TolfiyaEarth plugin;

    public CauldronProcessor(Block block, TolfiyaEarth plugin){
        this.block = block;
        this.plugin = plugin;
    }

    public static boolean isValidType(Material type){
        for(CauldronRecipe recipe : recipes){
            for(CauldronRecipePiece recipePiece : recipe.getPieces()){
                if(recipePiece.getMaterial().equals(type)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void removeItem(Item item){
        for(CauldronProcessor processor : processors){
            if(itemToTaskMap.containsKey(item)){
                itemToTaskMap.get(item).cancel();
            }
            itemToTaskMap.remove(item);
            processor.droppedInThis.remove(item);
            processor.howMuch.remove(item.getItemStack().getType());
        }
    }

    public void craft(TolfiyaEarth plugin){
        Levelled cauldronLevel = (Levelled) block.getBlockData();
        if(cauldronLevel.getLevel() != cauldronLevel.getMaximumLevel()) return;
        for (CauldronRecipe recipe : recipes) {
            int matched = 0;
            for (CauldronRecipePiece recipePiece : recipe.getPieces()) {
                if (howMuch.containsKey(recipePiece.getMaterial())) {
                    if (howMuch.get(recipePiece.getMaterial()) >= recipePiece.getAmount()) matched++;
                }
            }
            if (matched >= recipe.getPieces().length) {

                block.getWorld().playSound(block.getLocation(), Sound.ITEM_BOTTLE_FILL, 1f, 1f);
                for (CauldronRecipePiece recipePiece : recipe.getPieces()) {
                    int currentAmount = howMuch.get(recipePiece.getMaterial());
                    int removeAmount;
                    if((currentAmount - recipePiece.getAmount()) == 0){
                        howMuch.remove(recipePiece.getMaterial());
                        removeAmount = -1;
                    } else {
                        removeAmount = currentAmount - recipePiece.getAmount();
                        howMuch.replace(recipePiece.getMaterial(), removeAmount);
                    }
                    if(!droppedInThis.isEmpty()) {
                        for (Item item : droppedInThis) {
                            if (!item.getItemStack().getType().equals(recipePiece.getMaterial())) continue;
                            droppedInThis.remove(item);
                            if (removeAmount == -1) {
                                item.remove();
                                howMuch.remove(item);
                            } else {
                                ItemStack itemStack = item.getItemStack();
                                itemStack.setAmount(removeAmount);
                                item.setItemStack(itemStack);
                                howMuch.replace(recipePiece.getMaterial(), removeAmount);
                                droppedInThis.add(item);
                                item.setVelocity(new Vector(-0.07 + (0.07 - (-0.07)) * random.nextDouble(), 0.47, -0.07 + (0.07 - (-0.07)) * random.nextDouble()));
                            }
                            break;
                        }
                    }

                }
                if(droppedInThis.isEmpty()){
                    removeProcessor(this);
                }
                block.getWorld().dropItem(block.getLocation().clone().add(0.5, 1, 0.5), new ItemStack(recipe.getResult(), recipe.getResultAmount()));
                block.setType(Material.CAULDRON);
                break;
            }
        }
    }

    public void particle(Particle particle){
        block.getWorld().spawnParticle(particle, block.getLocation().clone().add(0.5, 1.0, 0.5), random.nextInt(10));
    }

    public void addItem(Item item){
        if(howMuch.containsKey(item.getItemStack().getType())){
            howMuch.replace(item.getItemStack().getType(), howMuch.get(item.getItemStack().getType()) + item.getItemStack().getAmount());
        } else {
            howMuch.put(item.getItemStack().getType(), item.getItemStack().getAmount());
        }

        particle(Particle.WATER_SPLASH);

        droppedInThis.add(item);

        craft(plugin);
    }


    public static CauldronProcessor getProcessor(Block block){
        for(CauldronProcessor processor : processors){
            if(processor.block.equals(block)){
                return processor;
            }
        }
        return null;
    }

    public static void addProcessor(CauldronProcessor processor){
        processors.add(processor);
    }

    public static void removeProcessor(CauldronProcessor processor){
        processors.remove(processor);
    }
}
