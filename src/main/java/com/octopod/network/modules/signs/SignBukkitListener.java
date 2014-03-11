package com.octopod.network.modules.signs;

import com.octopod.network.NetworkPlugin;
import com.octopod.network.util.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 * @author Octopod
 *         Created on 3/8/14
 */
public class SignBukkitListener implements Listener {

    private String fromLoc(Location loc) {
        return "[" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", " + loc.getWorld().getName() + "]";
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent event) {

        Block block = event.getBlock();
        Location loc = block.getLocation();

        Sign sign = (Sign)event.getBlock().getState();

        if(event.getLine(0).equals("net+sign")) {

            String server = event.getLine(1);

            if(NetworkPlugin.self.isServerOnline(server)) {
                SignPlugin.self.getDatabase().addSign(server, loc);
                BukkitUtils.sendMessage(event.getPlayer(), "&6Net+ &7sign registered @ &e" + fromLoc(loc) + "&7!");
                SignPlugin.self.save();
            } else {
                BukkitUtils.sendMessage(event.getPlayer(), "&7Server &a'" + server + "'&7 doesn't exist or isn't online!");
            }

        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Location loc =event.getBlock().getLocation();
        SignDatabase db = SignPlugin.self.getDatabase();
        if(db.getSign(loc) != null) {
            db.removeSign(loc);
            BukkitUtils.sendMessage(event.getPlayer(), "&6Net+ &7sign unregistered @ &e" + fromLoc(loc) + "&7!");
            SignPlugin.self.save();
        }

    }

}
