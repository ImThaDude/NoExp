/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noexp;

//Gamerule backup object

import java.util.HashMap;
import java.util.List;
import org.bukkit.World;


public class gameruleBackup {
    
    //GameruleBackup is an object which will store the values of the worlds on a hashmap.
    //This will be used to restore and backup when needed on any gamerule.
    
    HashMap<String,String> worldKeepInv;
    String gm;
    String wd;

    public gameruleBackup(String gamerule, String worldDefault) {
        //Global gamerule and world default of gamerule.
        gm = gamerule;
        wd = worldDefault;
    }
    
    //Test if there is any values inside the worldKeepInv.
    public boolean isBackedUp() {
        if (worldKeepInv == null)
            return false;
        else
            return true;
    }
    
    //Backups and sets the values of the worlds and the state you want changed.
    public boolean backupandset(List<World> worlds, String mode) {
        
        if (!isBackedUp()) {
            //Creates a new hashmap.
            worldKeepInv = new HashMap<>();
            //Iterates through worlds and stores in worldKeepInv. Sets the value with mode.
            for (World world : worlds) {
                worldKeepInv.put(world.getName(), world.getGameRuleValue(gm));
                world.setGameRuleValue(gm, mode);
            }
            return true;
        } else {
            return false;
        }
    }
    
    //Restores values
    public boolean restore(List<World> worlds) {
        
        if (isBackedUp()) {
            //Iterates through all the values.
            for (World world : worlds) {
                //Tests if backed up data is within the hashmap.
                if (worldKeepInv.containsKey(world.getName())) {
                    //If it is then it sets the gamerule back to what it was.
                    world.setGameRuleValue(gm, worldKeepInv.get(world.getName()));
                } else {
                    //If its not existant in the hashmap then it sets it to the preset default.
                    world.setGameRuleValue(gm, wd);
                }
            }
            //Deletes the object just in case you want to backup again.
            worldKeepInv = null;
            return true;
        } else
            return false;
    }
    
}
