package me.mrfunny.plugins.spigot.tolfiyaearth.crafts.cauldron;

import me.mrfunny.plugins.spigot.tolfiyaearth.TolfiyaEarth;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemWaitTask extends BukkitRunnable {

    private final Item item;
    private final TolfiyaEarth plugin;
    public int timer = 0;

    public ItemWaitTask(Item item, TolfiyaEarth plugin){
        CauldronProcessor.itemToTaskMap.put(item, this);
        this.item = item;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        //plugin.getLogger().info(item.getLocation().getBlock().getType().name());
        if(item.getLocation().getBlock().getType().name().contains("CAULDRON")){
            CauldronProcessor processor = CauldronProcessor.getProcessor(item.getLocation().getBlock());
            if(processor == null){
                processor = new CauldronProcessor(item.getLocation().getBlock(), plugin);
                CauldronProcessor.addProcessor(processor);
            }
            processor.addItem(item);
            cancel();
        }

        if(++timer == 20){
            this.cancel();
        }
    }
}

