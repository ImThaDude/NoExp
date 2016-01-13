package noexp;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.event.Listener;
import java.util.UUID;
import noexp.NoExp;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class ExpListener implements Listener {
	
    NoExp plugin;
	
    public ExpListener(NoExp instance) {
	plugin = instance;
    }
	
    //This is the event triggered as people get exp.
    @EventHandler
    public void onExp(PlayerExpChangeEvent event) {
        Player p = event.getPlayer();
        UUID id = event.getPlayer().getUniqueId();
        //This initializes the variable within the data structure.
        if (!plugin.PlayerOutExp.containsKey(id))
            plugin.PlayerOutExp.put(id, (short) 0);
        //This adds the health amount to the changed hp
        if (event.getAmount() > 0) {
            plugin.PlayerOutExp.put(id, (short) (plugin.PlayerOutExp.get(id) + event.getAmount()));
            //Implemented action bar GUI for players to easily visulize in game
            ActionBarAPI.sendActionBar(p, "§e§l" + plugin.PlayerOutExp.get(id));
        }
        //This subtracts the amount by the difference and adds hp to the player
        processExp(p);
        //This will prevent on getting exp depending on the world the player is in.
        if (!plugin.Whitelisted.contains(p.getWorld().getName()) && event.getAmount() > 0)
            event.setAmount(0);
    }
    
    private void processExp(Player p) {
        UUID id = p.getUniqueId();
        while (plugin.PlayerOutExp.get(id) >= plugin.HalfHeartLimit) {
            plugin.PlayerOutExp.put(id, (short) (plugin.PlayerOutExp.get(id) - plugin.HalfHeartLimit));
            //Makes sure that the player is not being set to an hp above the normal.
            if ((p.getHealth() + plugin.HealAmt) <= p.getHealthScale()) {
                p.setHealth(p.getHealth() + plugin.HealAmt);
                p.sendMessage("You Healed!");
            }
        }
    }
}