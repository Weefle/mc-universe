package com.octopod.network.commands;

import com.octopod.network.cache.CommandCache;
import com.octopod.octolib.minecraft.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMaster extends DocumentedCommand{

	public final static String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "NXN" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;
	public final static String COLOR = ChatColor.AQUA + "";
	public final static String WIN_VER = ChatColor.DARK_GRAY + "+---------------------------------------------------+";
	public final static String WIN_HOR1 = ChatColor.DARK_GRAY + "\u2019| " + ChatColor.WHITE;
	public final static String WIN_HOR2 = ChatColor.DARK_GRAY + "|";
	public final static int WIN_SIZE = 306;
	
	public CommandMaster(String root){
		super(root, "<command> <arguments...>",

			"Network general command."

		);
	}

	public boolean exec(CommandSender sender, String label, String[] args) {
		
		if(!(sender instanceof Player)){return false;}
		
		Player player = (Player)sender;
		
		if(args.length == 0) {
			
			player.sendMessage(PREFIX + "Welcome to the Nixium Network!");
			player.sendMessage(PREFIX + "Type /net help to view commands.");
			
		} else {

			switch(label){
				case "help":
					CommandCache.getCache().getCommand("CommandHelp").onCommand(sender, label, args);
					break;
				case "core":
				    //player.connect(ProxyServer.getInstance().getServerInfo("core"));
				    break;
				case "join":
					if(args.length >= 2){
						//player.connect(ProxyServer.getInstance().getServerInfo(args[1]));
					}else{
						player.sendMessage(PREFIX + "/net join <server>");
					}
				    break;
				case "list":
					//commands.get("CommandList").execute(sender, args);
					break;
				case "fade":
					player.sendMessage(WIN_VER);
					player.sendMessage(ChatColor.BOLD + COLOR + " Nixium Fade");
					player.sendMessage(WIN_VER);
					for(String line: new String[]{
						"Nixium Fade is an experimental chat filter created",
						"by ProjectBarks for use in our network's servers.",
						"The filter processes on BungeeCord,",
						"giving our servers extra performance."
					}) {
						player.sendMessage(WIN_HOR1 + ChatUtils.blockString(COLOR + line, WIN_SIZE, 0) + WIN_HOR2); 
					}
					player.sendMessage(WIN_VER);
					break;
				default:
					break;
			}
		}
		
		return true;

	}

}
