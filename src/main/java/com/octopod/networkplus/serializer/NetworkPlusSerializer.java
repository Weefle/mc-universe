package com.octopod.networkplus.serializer;

import com.octopod.networkplus.NetworkDecodeException;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface NetworkPlusSerializer
{
	public String encode(Object obj);

	public <T> T decode(String encoded, Class<T> type) throws NetworkDecodeException;
}
