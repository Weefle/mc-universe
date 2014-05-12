package com.octopod.network.commands;

import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlus;
import org.bukkit.command.CommandSender;

/**
 * @author Octopod
 *         Last updated on 3/4/14
 */
public class CommandScan extends NetworkCommand {

    public CommandScan(String root) {
        super(root, "<command>", NetworkPermission.NETWORK_SCAN,

                "Re-requests current server information from other servers."

        );
    }

    @Override
    protected boolean exec(CommandSender sender, String label, String[] args) {

        NetworkPlus.requestServerFlags();
        return true;

    }
}
