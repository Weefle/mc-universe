package com.octopod.network.commands;

import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.MessageResult;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.octopod.network.NetworkPlugin;
import com.octopod.network.LPRequestUtils;

public class CommandServerConnect extends DocumentedCommand {

	public CommandServerConnect() {
		super(
			"nserver", "<command> <server>",
			"Attempts to send you to another server. The console cannot run this command."
		);
	}
	
	Integer[] numArgs = new Integer[]{1};
	
	@Override
	public Integer[] numArgs() {
		return numArgs;
	}	
	
	@Override
	protected boolean exec(CommandSender sender, Command command, String label, String[] args) {

		if(!(sender instanceof Player)) return false;

		Player player = (Player)sender;
		String server = args[0];
		
		//Checks if the server they're trying to connect to is this same server
		if(server.equals(NetworkPlugin.connect.getSettings().getUsername())) {
			NetworkPlugin.sendMessage(sender, "&cYou are already connected to this server.");
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
		try {
			NetworkPlugin.connect.request(new RedirectRequest(server, player.getName()));
		} catch (RequestException e) {}
		
		return true;
		
	}

}
