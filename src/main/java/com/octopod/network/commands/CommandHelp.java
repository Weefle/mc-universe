package com.octopod.network.commands;

import com.octopod.network.NetworkPermission;
import com.octopod.network.cache.NetworkCommandCache;
import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.octolib.minecraft.ChatBuilder;
import com.octopod.octolib.minecraft.ChatElement;
import com.octopod.octolib.minecraft.ChatUtils;
import com.octopod.octolib.minecraft.ChatUtils.Color;
import org.bukkit.command.CommandSender;

public class CommandHelp extends NetworkCommand {

    public CommandHelp(String root, String... aliases) {
        super(root, aliases, "<command>", NetworkPermission.NETWORK_HELP,
			"Lists out avaliable commands."
		);
	}

	@Override
	public boolean exec(CommandSender sender, String label, String[] args) {

        BukkitUtils.sendMessage(sender, "&8-----------------------------------------------------", "");
		BukkitUtils.sendMessage(sender, ChatUtils.toLegacy(new ChatBuilder().appendBlock(new ChatElement("Network Commands").color(Color.AQUA), 320, 2)), "");
		BukkitUtils.sendMessage(sender, "&8-----------------------------------------------------", "");

		for(NetworkCommand command: NetworkCommandCache.getCommands().values()) {
			BukkitUtils.sendMessage(sender, "&8[&b" + command.getUsage() + "&8]: &6" + command.getDescription(), "");
		}

		return true;
	}
}
