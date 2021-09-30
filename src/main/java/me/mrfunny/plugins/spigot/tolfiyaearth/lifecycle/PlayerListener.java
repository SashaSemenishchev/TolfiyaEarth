package me.mrfunny.plugins.spigot.tolfiyaearth.lifecycle;

import me.mrfunny.plugins.spigot.tolfiyaearth.TolfiyaEarth;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener {

    private final TolfiyaEarth plugin;

    public PlayerListener(TolfiyaEarth tolfiyaEarth) {
        this.plugin = tolfiyaEarth;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        event.getEntity().kickPlayer("Вы умерли\nОстался 1 день");
        plugin.getConfig().set("deaths." + event.getEntity().getUniqueId(), System.currentTimeMillis());
        plugin.saveConfig();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event){
        if(plugin.getConfig().contains("deaths." + event.getPlayer().getUniqueId())){
            long deathTimestamp = plugin.getConfig().getLong("deaths." + event.getPlayer().getUniqueId());
            plugin.getLogger().info((System.currentTimeMillis() - deathTimestamp) + "");
            if((System.currentTimeMillis() - deathTimestamp) < 86400000){
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "Вы умерли\nОсталось " + format((deathTimestamp + 86400000 - System.currentTimeMillis())));
            } else {
                plugin.getConfig().set("deaths." + event.getPlayer().getUniqueId(), null);
            }
        }
    }

    public String format(long time){
        long minutes = time / (1000*60) % 60;
        long hours = time /  (1000*60*60) % 24;
        System.out.println(hours + ":" + minutes);
        StringBuilder sb = new StringBuilder();
        if(hours != 0){
            sb.append(hours).append(" ");
            if(hours == 1){
                sb.append("час");
            } else if(String.valueOf(hours).endsWith("2") || String.valueOf(hours).endsWith("3") || String.valueOf(hours).endsWith("4")){
                sb.append("часа");
            } else {
                sb.append("часов");
            }
            sb.append(" и ");
        }
        sb.append(minutes).append(" ");
        if(minutes == 1){
            sb.append("минута");
        } else if(String.valueOf(minutes).endsWith("2") || String.valueOf(minutes).endsWith("3") || String.valueOf(minutes).endsWith("4")){
            sb.append("минуты");
        } else {
            sb.append("минут");
        }
        return sb.toString();
    }
}
