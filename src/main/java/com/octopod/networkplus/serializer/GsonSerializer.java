package com.octopod.networkplus.serializer;

import com.octopod.networkplus.ServerValue;
import com.octopod.networkplus.exceptions.DeserializationException;
import net.minecraft.util.com.google.gson.*;
import net.minecraft.util.com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class GsonSerializer implements NetworkPlusSerializer
{
	@Override
	public String getName() {return "Gson";}

	private Gson gson;

	public GsonSerializer()
	{
		GsonBuilder builder = new GsonBuilder();
		builder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		builder.enableComplexMapKeySerialization();
		gson = builder.create();
	}

	public String serialize(Object object)
	{
		return gson.toJson(object);
	}

	public <T> T deserialize(String encoded, Class<T> type) throws DeserializationException
	{
		T obj;
		try {
			obj = gson.fromJson(encoded, type);
		} catch (Exception e)
		{
			DeserializationException exception = new DeserializationException("Unable to decode this string as JSON: " + encoded);
			exception.setStackTrace(e.getStackTrace());
			throw exception;
		}
		if(obj == null) throw new DeserializationException("Unable to decode this string as JSON: " + encoded);
		return obj;
	}

	@Override
	public <K, V> Map<K, V> deserializeMap(String encoded, Class<K> key, Class<V> value) throws DeserializationException
	{
		return deserialize(encoded, new TypeToken<Map<K, V>>(){}.getType());
	}

	@Override
	public <T> List<T> deserializeList(String encoded, Class<T> type) throws DeserializationException
	{
		return deserialize(encoded, new TypeToken<List<T>>(){}.getType());
	}

	private <T> T deserialize(String encoded, Type type) throws DeserializationException
	{
		T obj;
		try {
			obj = gson.fromJson(encoded, type);
		} catch (Exception e)
		{
			DeserializationException exception = new DeserializationException("Unable to decode this string as JSON: " + encoded);
			exception.setStackTrace(e.getStackTrace());
			throw exception;
		}
		if(obj == null) throw new DeserializationException("Unable to decode this string as JSON: " + encoded);
		return obj;
	}

	private class ServerValueDeserializer implements JsonDeserializer<ServerValue>
	{
		@Override
		public ServerValue deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
		{
			for(ServerValue value: ServerValue.values())
			{
				if(value.name().equalsIgnoreCase(json.getAsString())) return value;
			}
			return null;
		}
	}

	private class ServerValueSerializer implements JsonSerializer<ServerValue>
	{
		@Override
		public JsonElement serialize(ServerValue value, Type type, JsonSerializationContext context)
		{
			return new JsonPrimitive(value.name());
		}
	}
}
