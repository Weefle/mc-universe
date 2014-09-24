package com.octopod.networkplus.serializer;

import com.google.gson.*;
import com.octopod.networkplus.NetworkDecodeException;
import com.octopod.networkplus.ServerValue;

import java.lang.reflect.Type;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class GsonSerializer implements NetworkPlusSerializer
{
	private Gson gson;

	public GsonSerializer()
	{
		GsonBuilder builder = new GsonBuilder();
		builder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		builder.registerTypeAdapter(ServerValue.class, new ServerValueDeserializer());
		gson = builder.create();
	}

	@Override
	public String encode(Object obj)
	{
		return gson.toJson(obj);
	}

	@Override
	public <T> T decode(String encoded, Class<T> type) throws NetworkDecodeException
	{
		T obj = null;
		try {
			obj = gson.fromJson(encoded, type);
		} catch (Exception e) {}
		if(obj == null) throw new NetworkDecodeException("Unable to decode this string as " + type.getSimpleName());
		return obj;
	}

	private static class ServerValueDeserializer implements JsonDeserializer<ServerValue>
	{
		@Override
		public ServerValue deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
		{
			return ServerValue.valueOf(json.getAsJsonPrimitive().getAsString().toUpperCase());
		}
	}
}
