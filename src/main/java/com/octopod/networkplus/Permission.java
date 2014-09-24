package com.octopod.networkplus;

import com.octopod.networkplus.server.ServerCommandSource;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public enum Permission
{
	LOGGER_INFO		("netplus.log.info"),
	LOGGER_WARNING	("netplus.log.warning"),
	LOGGER_DEBUG	("netplus.log.debug"),
	LOGGER_VERBOSE	("netplus.log.verbose"),

	COMMAND_PING	("netplus.command.ping");

	private String node;

	private Permission(String node)
	{
		this.node = node;
	}

	public boolean hasPermission(ServerCommandSource sender)
	{
		return sender.hasPermission(this);
	}

	public String getNode()
	{
		return node;
	}
}
