package com.octopod.networkplus;

import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface ServerInfo
{
	public void setValue(ServerValue infoType, Object value);

	public Object getValue(ServerValue infoType);

	public Map<ServerValue, Object> getValues();

	public boolean containsValue(ServerValue infoType);

	public String encode();
}
