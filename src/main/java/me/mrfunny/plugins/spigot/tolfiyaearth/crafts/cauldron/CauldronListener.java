package me.mrfunny.plugins.spigot.tolfiyaearth.crafts.cauldron;

import me.mrfunny.plugins.spigot.tolfiyaearth.TolfiyaEarth;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class CauldronListener implements Listener {

    private final TolfiyaEarth plugin;

    public CauldronListener(TolfiyaEarth plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Item){
            Item item = (Item) event.getEntity();
            CauldronProcessor.removeItem(item);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(CauldronProcessor.isValidType(event.getItemDrop().getItemStack().getType())){
            new ItemWaitTask(event.getItemDrop(), plugin).runTaskTimer(plugin, 0, 20);
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event){
        CauldronProcessor.removeItem(event.getItem());
    }
}
