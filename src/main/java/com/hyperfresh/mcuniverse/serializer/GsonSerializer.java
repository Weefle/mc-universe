package com.hyperfresh.mcuniverse.serializer;

import com.hyperfresh.mcuniverse.CachedServer;
import com.hyperfresh.mcuniverse.Server;
import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.exceptions.DeserializationException;
import com.hyperfresh.mcuniverse.packets.Packet;
import net.minecraft.util.com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class GsonSerializer implements PacketSerializer
{
	@Override
	public String getName() {return "Gson";}

	private Gson gson;

	public GsonSerializer()
	{
		GsonBuilder builder = new GsonBuilder();
		//builder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		builder.registerTypeAdapter(Packet.class, new NetworkPacketAdapter());
		builder.registerTypeAdapter(Server.class, new ServerAdapter());
		builder.enableComplexMapKeySerialization();
		gson = builder.create();
	}

	@Override
	public String serialize(Packet packet)
	{
		return gson.toJson(packet, Packet.class);
	}

	@Override
	public Packet deserialize(String encoded) throws DeserializationException
	{
		Packet packet;
		try {
			packet = gson.fromJson(encoded, Packet.class);
		} catch (Exception e)
		{
			DeserializationException exception = new DeserializationException(e.getMessage());
			exception.setStackTrace(e.getStackTrace());
			throw exception;
		}
		if(packet == null) throw new DeserializationException("Unable to deserilize this string as SwitchPacket: " + encoded);
		return packet;
	}

	public static class NetworkPacketAdapter implements JsonSerializer<Packet>, JsonDeserializer<Packet>
	{
		@Override
		public JsonElement serialize(Packet packet, Type ttype, JsonSerializationContext context)
		{
			JsonObject object = new JsonObject();
			object.add("_package", new JsonPrimitive(packet.getClass().getName()));
			object.add("_json", context.serialize(packet, packet.getClass()));
			return object;
		}

		@Override
		public Packet deserialize(JsonElement element, Type ttype, JsonDeserializationContext context)
			throws JsonParseException
		{
			JsonObject object = element.getAsJsonObject();
			String pack = object.get("_package").getAsString();
			JsonElement json = object.get("_json");

			try
			{
				return context.deserialize(json, Class.forName(pack));
			} catch (ClassNotFoundException e)
			{
				UniverseAPI.getInstance().getLogger().w("Packet not found with package: " + pack);
			}
			return null;
		}
	}

	public static class ServerAdapter implements JsonSerializer<Server>, JsonDeserializer<Server>
	{
		@Override
		public JsonElement serialize(Server server, Type ttype, JsonSerializationContext context)
		{
			return context.serialize(new CachedServer(server));
		}

		@Override
		@SuppressWarnings("unchecked")
		public Server deserialize(JsonElement element, Type ttype, JsonDeserializationContext context)
			throws JsonParseException
		{
			return context.deserialize(element, CachedServer.class);
		}
	}
}
