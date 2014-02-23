package com.octopod.network.commands;

import com.octopod.network.LPRequestUtils;
import com.octopod.network.NetworkPlugin;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.MessageResult;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandServerSendAll extends DocumentedCommand {
	
	public CommandServerSendAll() {
		super("nsendall", "<command> <server>", 
				
			"Sends all players to a server."
					
		);
	}
	
	Integer[] numArgs = new Integer[]{1, 2};
	
	@Override
	public Integer[] numArgs() {
		return numArgs;
	}	
	
	@Override
	protected boolean exec(CommandSender sender, Command cmd, String label, String[] args) {

		String from = null, server;
		if(args.length == 2) {
			from = args[0];
			server = args[1];
		} else {
			server = args[0];
		}

		//Sends a dummy message request to the server . If it fails, then the server doesn't exist.
		MessageResult result = LPRequestUtils.sendDummyMessage(server).awaitUninterruptibly();
		
		//Checks if the message went through before sending them there.
		if(result.getStatusCode() != StatusCode.SUCCESS) {
			NetworkPlugin.sendMessage(sender, "&cThis server is offline or does not exist.");
			return true;
		}

		if(from == null) {
			NetworkPlugin.sendMessage(sender, "&7Sending all players to server &6" + server + "&7...");
			LPRequestUtils.requestSendAll(server);			
		} else {
			NetworkPlugin.sendMessage(sender, "&7Sending all players from server &6" + from + "&7 to server &6 " + server + "&7...");
			LPRequestUtils.requestSendAll(from, server);
		}

		return true;
		
	}
	
}
