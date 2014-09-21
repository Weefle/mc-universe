package com.octopod.networkplus.server;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface ServerPlayer extends ServerCommandSender
{
	public String getID();

	public void redirect(String serverID);
}
