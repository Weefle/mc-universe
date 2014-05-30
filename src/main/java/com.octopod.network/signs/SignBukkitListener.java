package com.octopod.network.signs;

import com.octopod.network.NPLogger;
import com.octopod.network.NetworkPlus;
import com.octopod.network.CommandManager;
import com.octopod.network.commands.CommandServerConnect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Octopod
 *         Created on 3/8/14
 */
public class SignBukkitListener implements Listener {

    private NPLogger logger = NetworkPlus.getLogger();

    @EventHandler
    public void onSignPlace(SignChangeEvent event) {

        Block block = event.getBlock();
        Location loc = block.getLocation();

        if(event.getLine(0).equals("netsign") && event.getPlayer().hasPermission("network.sign.create")) {

            String server = event.getLine(1);

            if(NetworkPlus.isServerOnline(server)) {
                SignPlugin.instance.getDatabase().addSign(server, loc);
                SignPlugin.instance.saveDatabase();
                SignPlugin.instance.updateSign(event, new SignFormat(server));
            } else {
                logger.i("&7Server &a'" + server + "'&7 doesn't exist or isn't online!");
            }

        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Location loc = event.getBlock().getLocation();
        SignDatabase db = SignPlugin.instance.getDatabase();
        if(db.getSign(loc) != null) {
            if(!event.getPlayer().hasPermission("network.sign.clear")) {
                event.setCancelled(true);
            } else {
                db.removeSign(loc);
                SignPlugin.instance.saveDatabase();
            }
        }

    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Location loc = event.getClickedBlock().getLocation();
            SignDatabase db = SignPlugin.instance.getDatabase();
            if(db.getSign(loc) != null) {
                CommandManager.getCommand(CommandServerConnect.class).onCommand(event.getPlayer(), "", new String[] {db.getSign(loc)});
            }

        }

    }

}
