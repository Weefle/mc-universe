package com.octopod.networkplus.server;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public enum ServerPermission
{
	LOGGER_INFO		("netplus.log.info"),
	LOGGER_WARNING	("netplus.log.warning"),
	LOGGER_DEBUG	("netplus.log.debug"),
	LOGGER_VERBOSE	("netplus.log.verbose");

	private String node;

	private ServerPermission(String node)
	{
		this.node = node;
	}

	public boolean hasPermission(ServerCommandSender sender)
	{
		return sender.hasPermission(this);
	}

	@Override
	public String toString()
	{
		return node;
	}
}
