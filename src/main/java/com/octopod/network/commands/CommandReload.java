package com.octopod.network.commands;

import com.octopod.network.NetworkDebug;
import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlugin;
import org.bukkit.command.CommandSender;

/**
 * @author Octopod
 *         Last updated on 2/26/14
 */
public class CommandReload extends NetworkCommand {

    public CommandReload(String root) {
        super(root, "<command>", NetworkPermission.NETWORK_RELOAD,
            "Reloads the configuration of the plugin."
        );
    }

    @Override
    protected boolean exec(CommandSender sender, String label, String[] args) {

        NetworkDebug.info("Reloading plugin...");
        NetworkPlugin.self.reload();
        NetworkDebug.info("Finished reloading!");
        return true;

    }

}
