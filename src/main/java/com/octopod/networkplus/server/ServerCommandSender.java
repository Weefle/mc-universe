package com.octopod.networkplus.server;

import com.octopod.util.minecraft.ChatReciever;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface ServerCommandSender extends ChatReciever
{
	public String getName();

	public void sendMessage(String message);

	public boolean hasPermission(ServerPermission permission);
}
