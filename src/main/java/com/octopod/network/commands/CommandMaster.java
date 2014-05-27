package com.octopod.network.commands;

import com.octopod.network.NetworkPermission;
import com.octopod.network.CommandManager;
import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.octal.minecraft.ChatElement;
import com.octopod.octal.minecraft.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandMaster extends NetworkCommand {

	public final static String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "NXN" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;

	public CommandMaster(String root, String... aliases){
		super(root, aliases, "<command> <arguments...>", NetworkPermission.NETWORK_MASTER,

			"Network general command."

		);
	}

	public boolean exec(CommandSender sender, String vlabel, String[] vargs) {

		if(!(sender instanceof Player)){return false;}

		Player player = (Player)sender;

        List<String> argList = Arrays.asList(vargs);

		if(vargs.length == 0) {

            show_help(sender, 0);
            return true;

		} else {

            String label = argList.get(0);
            String[] args;

            if(argList.size() == 1) {
                args = new String[0];
            } else {
                args = new LinkedList<>(argList).subList(1, argList.size()).toArray(new String[argList.size() - 1]);
            }

            if(is_numeric(label)) {
                show_help(sender, Integer.valueOf(label));
                return true;
            }

			switch(label){
				case "help":
					return CommandManager.getCommand(CommandHelp.class).onCommand(sender, label, args);
				case "hub":
				    return CommandManager.getCommand(CommandHub.class).onCommand(sender, label, args);
				case "join":
                    return CommandManager.getCommand(CommandServerConnect.class).onCommand(sender, label, args);
				case "list":
                    return CommandManager.getCommand(CommandServerList.class).onCommand(sender, label, args);
				default:
					break;
			}

		}

		return true;

	}

    private boolean is_numeric(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int PAGE_SIZE = 5;

    private void show_help(CommandSender sender, int page) {

        List<NetworkCommand> commands = new ArrayList<>(CommandManager.getCommands().values());
        Collections.sort(commands, new Comparator<NetworkCommand>() {

            @Override
            public int compare(NetworkCommand cmd1, NetworkCommand cmd2) {
                return cmd1.compareTo(cmd2);
            }

        });

        int startIndex = page * PAGE_SIZE;

        List<NetworkCommand> subList = commands.subList(startIndex, startIndex + PAGE_SIZE);

        BukkitUtils.sendMessage(sender, "&8-----------------------------------------------------", "");
        BukkitUtils.sendMessage(sender, ChatUtils.toLegacy(ChatUtils.blockElement(new ChatElement("&bNetworkPlus Commands"), 320, 2)), "");
        BukkitUtils.sendMessage(sender, ChatUtils.toLegacy(ChatUtils.blockElement(new ChatElement("&8Page " + (page + 1)), 320, 2)), "");
        BukkitUtils.sendMessage(sender, "&8-----------------------------------------------------", "");

        for(NetworkCommand command: subList)
            BukkitUtils.sendMessage(sender, "&8[&b" + command.getUsage() + "&8]: &6" + command.getDescription(), "");

    }

}
