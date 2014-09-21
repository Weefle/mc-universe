package com.octopod.networkplus.server;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface ServerInfo
{
	public void setValue(ServerValue infoType, Object value);

	public Object getValue(ServerValue infoType);

	public boolean containsValue(ServerValue infoType);
}
