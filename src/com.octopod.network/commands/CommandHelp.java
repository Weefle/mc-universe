package com.octopod.network.commands;

import com.octopod.network.NetworkPermission;
import com.octopod.network.CommandManager;
import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.octal.minecraft.ChatElement;
import com.octopod.octal.minecraft.ChatUtils;
import com.octopod.octal.minecraft.ChatUtils.ChatColor;
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
		BukkitUtils.sendMessage(sender, ChatUtils.toLegacy(ChatUtils.blockElement(new ChatElement("Network Commands").setColor(ChatColor.AQUA), 320, 2)), "");
		BukkitUtils.sendMessage(sender, "&8-----------------------------------------------------", "");

		for(NetworkCommand command: CommandManager.getCommands().values()) {
			BukkitUtils.sendMessage(sender, "&8[&b" + command.getUsage() + "&8]: &6" + command.getDescription(), "");
		}

		return true;
	}
}
