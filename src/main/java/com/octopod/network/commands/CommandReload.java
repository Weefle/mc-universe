package com.octopod.network.commands;

import com.octopod.network.NetworkConfig;
import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlugin;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Octopod
 *         Last updated on 2/26/14
 */
public class CommandReload extends NetworkCommand {

    public CommandReload(String root) {
        super(root, "<command>", NetworkPermission.NETWORK_RELOAD,
            "Reloads the configuration of the plugin. Use -f as an argument to reload the entire plugin."
        );
    }

    @Override
    protected boolean exec(CommandSender sender, String label, String[] args) {

        if(Arrays.asList(args).contains("-f")) {
            NetworkPlugin.self.reload();
        } else {
            NetworkConfig.reloadConfig(sender);
        }

        return true;

    }

}
