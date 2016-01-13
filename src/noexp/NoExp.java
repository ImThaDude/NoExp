/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noexp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
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
    public HashMap<UUID, Short> PlayerOutExp;
    //This is the exp needed to get the hp
    public short HalfHeartLimit = 5;
    public double HealAmt = 1;
    public ArrayList<String> Whitelisted;

    //Activates the listener to listen for the exp.
    public void onEnable() {
        
        getServer().getPluginManager().registerEvents(new ExpListener(this), this);
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
        PlayerOutExp.remove(id);
    }
    
}
