package com.octopod.network.commands;

import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.MessageResult;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.octopod.network.NetworkPlugin;
import com.octopod.network.LPRequestUtils;

public class CommandServerSend extends DocumentedCommand {
	
	public CommandServerSend() {
		super("nsend", "<command> <player> <server>", 
				
			"Sends a player to a server."
					
		);
	}
	
	Integer[] numArgs = new Integer[]{2};
	
	@Override
	public Integer[] numArgs() {
		return numArgs;
	}	
	
	@Override
	protected boolean exec(CommandSender sender, Command cmd, String label, String[] args) {

		String player = args[0];
		String server = args[1];
		
		//Checks if the player is online.
		if(!LPRequestUtils.isPlayerOnline(player)) {
			NetworkPlugin.sendMessage(sender, "&cThis player is not online.");
			return true;
		}

		//Sends a dummy message request to the server . If it fails, then the server doesn't exist.
		MessageResult result = LPRequestUtils.sendDummyMessage(server).awaitUninterruptibly();
		
		//Checks if the message went through before sending them there.
		if(result.getStatusCode() != StatusCode.SUCCESS) {
			NetworkPlugin.sendMessage(sender, "&cThis server is offline or does not exist.");
			return true;
		}

		//Attempts to send them to the server
		NetworkPlugin.sendMessage(sender, "&7Sending &6" + player + " &7to server &b" + server + "&7...");
		try {
			NetworkPlugin.connect.request(new RedirectRequest(server, player));
		} catch (RequestException e) {}
		
		return true;
		
	}
	
}
