package com.octopod.network.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.octopod.network.NetworkConfig;
import com.octopod.network.LPRequestUtils;

public class CommandAlert extends DocumentedCommand {
	
	public CommandAlert() {
		super("nalert", "<command> <message...>", 
				
			"Broadcasts a message to the entire network. " +
			"Only servers that are running this plugin will recieve the alert."
					
		);
	}
	
	@Override
	protected boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
		
		String message = StringUtils.join(args, " ");
		
		LPRequestUtils.broadcastNetworkMessage(String.format(NetworkConfig.getConfig().FORMAT_ALERT, message));
		
		return true;
		
	}

}
