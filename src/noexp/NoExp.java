/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noexp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*This plugin will prevent exp players from getting exp, except if it has a whitelisted world
  This plugin will also contain the exp to health conversion.
  Players will regenerate with exp instead
  There is no regen on this game so you regen with exp
*/
public class NoExp extends JavaPlugin implements Listener {
    
    //Data structure to hold the exp obtained by players before its cancelled.
    HashMap<UUID, Short> PlayerOutExp;
    //This is the exp needed to get the hp
    short HalfHeartLimit = 5;
    double HealAmt = 1;
    ArrayList<String> Whitelisted;

    //Activates the listener to listen for the exp.
    public void onEnable() {
        
        Whitelisted  = new ArrayList<>();
        getServer().getPluginManager().registerEvents(this, this);
        PlayerOutExp = new HashMap<>();
        //This will add the world strings to the whitelisted
        Whitelisted = (ArrayList<String>) this.getConfig().getList("Whitelist");
        //This will initialize the other values according to config
        HalfHeartLimit = (short) this.getConfig().getInt("HalfHeartLimit");
        HealAmt = this.getConfig().getDouble("HealAmt");

    }

    public void onDisable() {
        
        PlayerOutExp = null;
        Whitelisted = null;
        
    }
    
    //This removes the values as soon as the players leave the server. Memory management.
    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        Whitelisted.remove(id);
    }
    
    //This is the event triggered as people get exp.
    @EventHandler
    public void onExp(PlayerExpChangeEvent event) {
        Player p = event.getPlayer();
        UUID id = event.getPlayer().getUniqueId();
        //This initializes the variable within the data structure.
        if (!PlayerOutExp.containsKey(id))
            PlayerOutExp.put(id, (short) 0);
        //This adds the health amount to the changed hp
        if (event.getAmount() > 0)
            PlayerOutExp.put(id, (short) (PlayerOutExp.get(id) + event.getAmount()));
        //This subtracts the amount by the difference and adds hp to the player
        while (PlayerOutExp.get(id) >= HalfHeartLimit) {
            PlayerOutExp.put(id, (short) (PlayerOutExp.get(id) - HalfHeartLimit));
            //Makes sure that the player is not being set to an hp above the normal.
            if ((p.getHealth() + HealAmt) <= p.getHealthScale()) {
                p.setHealth(p.getHealth() + HealAmt);
                p.sendMessage("You Healed!");
            }
        }
        //This will prevent on getting exp depending on the world the player is in.
        if (!Whitelisted.contains(p.getWorld().getName()) && event.getAmount() > 0)
            event.setAmount(0);
    }
    
}
