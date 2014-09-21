package com.octopod.networkplus.serializer;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface NetworkPlusSerializer
{
	public String encode(Object obj);

	public <T> T decode(String encoded, Class<T> type);
}
