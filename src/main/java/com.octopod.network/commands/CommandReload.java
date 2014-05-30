package com.octopod.network.commands;

import com.octopod.network.NPConfig;
import com.octopod.network.NPPermission;
import com.octopod.network.NetworkPlus;
import com.octopod.octal.minecraft.ChatUtils.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Octopod
 *         Last updated on 2/26/14
 */
public class CommandReload extends NetworkCommand {

    public CommandReload(String root, String... aliases) {
        super(root, aliases, "<command>", NPPermission.NETWORK_RELOAD,
            "Reloads the configuration of the plugin. Use -f as an argument to reload the entire plugin."
        );
    }

    @Override
    protected boolean exec(CommandSender sender, String label, String[] args) {

        if(Arrays.asList(args).contains("-f")) {
            NetworkPlus.getPlugin().reload();
        } else {
            try {
				NPConfig.load();
				NetworkPlus.broadcastServerFlags(NetworkPlus.getServerFlags());
			} catch (Exception e) {
				NetworkPlus.getLogger().i(ChatColor.RED + "Unable to reload!");
			}

        }

        return true;

    }

}
