package com.octopod.networkplus.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class GsonSerializer implements NetworkPlusSerializer
{
	private Gson gson = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();

	@Override
	public String encode(Object obj)
	{
		return gson.toJson(obj);
	}

	@Override
	public <T> T decode(String encoded, Class<T> type)
	{
		return gson.fromJson(encoded, type);
	}
}
