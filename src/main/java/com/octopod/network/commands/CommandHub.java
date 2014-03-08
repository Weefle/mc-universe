package com.octopod.network.commands;

import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlugin;
import com.octopod.network.cache.NetworkHubCache;
import com.octopod.network.util.BukkitUtils;
import lilypad.client.connect.api.request.RequestException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Octopod
 *         Last updated on 2/26/14
 */
public class CommandHub extends NetworkCommand {

    public CommandHub(String root) {
        super(root, "<command>", NetworkPermission.NETWORK_HUB,

            "Sends you to the hub server, starting at priority 0. Cannot be run by the console."

        );
    }

    @Override
    public boolean weakCommand() {
        return false;
    }

    @Override
    protected boolean exec(CommandSender sender, String label, String[] args) {

        if(!(sender instanceof Player)) return false;

        String hub = NetworkHubCache.getHub();

        if(hub != null && !hub.equals(NetworkPlugin.self.getUsername())) {
            BukkitUtils.sendMessage(sender, "&7You've been moved to the hub server &a" + hub + "&7.");
            NetworkPlugin.self.sendPlayer(sender.getName(), hub);
        } else {
            BukkitUtils.sendMessage(sender, "&7You are already on the hub server.");
        }

        return true;

    }
}
