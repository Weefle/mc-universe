package com.hyperfresh.mcuniverse;

import com.hyperfresh.mcuniverse.event.events.NetworkConnectedEvent;
import com.hyperfresh.mcuniverse.event.events.NetworkMessageInEvent;
import com.hyperfresh.mcuniverse.event.events.NetworkPacketInEvent;
import com.hyperfresh.mcuniverse.minecraft.MinecraftPlayer;
import com.hyperfresh.mcuniverse.packets.*;
import com.hyperfresh.mcuniverse.server.ServerStatus;
import com.hyperfresh.mcuniverse.server.networked.UniversePlayer;
import com.hyperfresh.mcuniverse.server.networked.UniverseServer;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class UniverseEventPoster
{
	/**
	 * Listens for when this plugin is connected according to the NetworkConnection instance.
	 * This method should request ServerValueManager and playerlists from every server.
	 */
	public static void onNetworkConnected()
	{
		NetworkConnectedEvent event = new NetworkConnectedEvent();

		UniverseAPI.getInstance().getEventBus().post(event);
		UniverseAPI.getInstance().getInterface().broadcast(
			UniverseAPI.getInstance().PREFIX + "&7Successfully connected via &a" +
			UniverseAPI.getInstance().getConnection().getName() + "&7!"
		);
	}

	public static void onServerOnline(String server)
	{

	}

	public static void onServerOffline(String server)
	{

	}

	/**
	 * Runs the action for when the server recieves flags from a server.
	 * This method should cache the flags as a new ServerFlags object,
	 * or merge into the existing ServerFlags object if one exists.
	 * @param serverID the server's identifier
	 * @param event The flags representing information.
	 */
	public static void onServerInfoRecieved(String serverID, NetworkMessageInEvent event)
	{
		Logger logger = UniverseAPI.getInstance().getLogger();
		UniverseServer server = UniverseAPI.getInstance().getServerDatabase().getServer(serverID);

		//Checks for mismatched plugin versions between servers, and warns the server owners.
		UniverseVersion version = server.getPluginVersion();
		if(version != UniverseVersion.LATEST)
		{
			logger.i("&a" + serverID + "&7: Running &6{switch}&7 version &6" + version.toString());
		}

//		if(info.containsValue(ServerValue.HUB_PRIORITY))
//		{
//			//Checks if this server is a hub (priority above 0)
//			int hubPriority = (int)info.get(ServerValue.HUB_PRIORITY);
//			if(hubPriority >= 0) {
//				HubManager.addHub(server_id, hubPriority);
//				logger.i("Server &a" + server_id + "&7 registered as hub @ priority &e" + hubPriority);
//			}
//		}

		//Adds in the new flags
		//UniverseAPI.getServerDB().set(server_id, flags);


//		logger.v("Recieved server info from &a" + server_id + ":");
//		for(Map.Entry<String, Object> entry: flags.toMap().entrySet()) {
//			logger.i("    &b" + entry.getKey() + ": &e" + entry.get() + "&e");
//		}

	}

	public static void onPlayerJoinServer(UniversePlayer player, String serverUsername)
	{
		UniverseServer server = UniverseAPI.getInstance().getServerDatabase().getServer(serverUsername);

		@SuppressWarnings("unchecked")
		List<MinecraftPlayer> players = server.getOnlinePlayers();
		if(!players.contains(player))
			players.add(player);
		server.setProperty(ONLINE_PLAYERS.class, players);
	}

	public static void onPlayerLeaveServer(String playerID, String serverID)
	{
		UniverseServer server = UniverseAPI.getInstance().getServerDatabase().getServer(serverID);

		@SuppressWarnings("unchecked")
		List<String> players = server.getProperty(ONLINE_PLAYERS.class);
		if(players.contains(playerID)) players.remove(playerID);
		server.setProperty(ONLINE_PLAYERS.class, players);
	}

	/**
	 * Runs the action for when any players joins the queue on the network.
	 * This method should add the player to the respective queue.
	 */
	public static void onPlayerJoinQueue(String player, String serverID, int queuePosition)
	{
		Logger logger = UniverseAPI.getInstance().getLogger();
		logger.i(
			"&b" + player + " &7joined the queue for &a" + serverID +
			" &7in position: &a" + queuePosition
		);
		// If the message is to this server, add to queue.
//		if (server_id.equalsIgnoreCase(UniverseAPI.getServerFlags().getServerName()))
//		{
//			QueueManager.instance.add(player, queuePosition);
//		}
	}

	/**
	 * Runs the action for when any players leaves the queue on the network.
	 * This method should clear the player from the respective queue.
	 */
	public static void onPlayerLeaveQueue(String player, String serverID)
	{
		Logger logger = UniverseAPI.getInstance().getLogger();
		logger.i(
			"&b" + player + " &7left the queue for &a" + serverID);
		// If the server is this server, update queue.
//		if (server_id
//			.equalsIgnoreCase(UniverseAPI.getServerFlags().getServerName())) {
//			QueueManager.instance.updateQueue();
//		}
	}

	/**
	 * Listens for when any message is recieved from any server.
	 * This method is a gateway to other events and features.
	 * Refer to NetworkConfig.MessageChannel to what each channel does.
	 */
	public static void onPacketInbound(String serverID, Packet packetRaw)
	{
		NetworkPacketInEvent event = new NetworkPacketInEvent(serverID, packetRaw);

		UniverseAPI.getInstance().getEventBus().post(event);
		if(event.isCancelled()) return;

		UniverseAPI.getInstance().getServer(serverID).setProperty(LAST_PACKET.class, System.currentTimeMillis());

		if(packetRaw instanceof PacketInServerPing)
		{
			new PacketOutServerPing(((PacketInServerPing)packetRaw).getPingID()).send(serverID);
		}

		if(packetRaw instanceof PacketOutServerPing)
		{
			return;
		}

		if(packetRaw instanceof PacketInServerDiscover)
		{
			new PacketOutServerDiscover().send(serverID);
			if(serverID.equals(UniverseAPI.getInstance().getServerIdentifier())) return;
			UniverseAPI.getInstance().getServerDatabase().addServer(((PacketInServerDiscover) packetRaw).getServer());
		}

		if(packetRaw instanceof PacketOutServerDiscover)
		{
			if(serverID.equals(UniverseAPI.getInstance().getServerIdentifier())) return;
			UniverseAPI.getInstance().getServerDatabase().addServer(((PacketOutServerDiscover) packetRaw).getServer());
		}

		if(packetRaw instanceof PacketInServerUpdate)
		{
			PacketInServerUpdate packet = (PacketInServerUpdate)packetRaw;
			try
			{
				new PacketOutServerUpdate(packet.getPropertyClass()).send(serverID);
			}
			catch (ClassNotFoundException e)
			{
				UniverseAPI.getInstance().getLogger().w("Server \"" + serverID + "\" requested nonexistant property " + packet.getPropertyClassName());
			}
		}

		if(packetRaw instanceof PacketOutServerUpdate)
		{
			PacketOutServerUpdate packet = (PacketOutServerUpdate)packetRaw;
			try
			{
				UniverseServer server = UniverseAPI.getInstance().getServerDatabase().getServer(serverID);
				server.setProperty(packet.getPropertyClass(), packet.getValue());

				if(packet.getPropertyClass() == STATUS.class)
				{
					ServerStatus new_status = (ServerStatus)packet.getValue();
					ServerStatus old_status = server.getProperty(STATUS.class);
					if(new_status == ServerStatus.ONLINE && old_status == ServerStatus.OFFLINE) onServerOnline(serverID);
					if(new_status == ServerStatus.OFFLINE && old_status == ServerStatus.ONLINE) onServerOffline(serverID);
				}
			}
			catch (ClassNotFoundException e)
			{
				UniverseAPI.getInstance().getLogger().w("Server \"" + serverID + "\" gave us nonexistant property " + packet.getPropertyClassName());
			}
		}

		if(packetRaw instanceof PacketInPlayerSwitch)
		{
			new PacketOutPlayerSwitch(((PacketInPlayerSwitch)packetRaw).getUUID()).send(serverID);
		}

		if(packetRaw instanceof PacketOutPlayerSwitch)
		{
			PacketOutPlayerSwitch packetIn = (PacketOutPlayerSwitch)packetRaw;
			if(packetIn.getResult() == PlayerSwitchResult.SUCCESS)
			{
				UniverseAPI.getInstance().getConnection().sendPlayer(packetIn.getPlayer().getPlayer(), serverID);
			}
		}

		if(packetRaw instanceof PacketInServerSwitchAll)
		{
			String destination = ((PacketInServerSwitchAll)packetRaw).getDestination();

			for(MinecraftPlayer player: UniverseAPI.getInstance().getInterface().getOnlinePlayers())
			{
				new PacketInPlayerSwitch(player).send(destination);
			}
		}

		if(packetRaw instanceof PacketInServerBroadcast)
		{
			String message = ((PacketInServerBroadcast)packetRaw).getBroadcastMessage();
			UniverseAPI.getInstance().getInterface().broadcast(message);
		}

		//TODO: Player Private Message Packet

		//TODO: Player Chat Packet

		//TODO: Player Join Queue Packet

		//TODO: Player Leave Queue Packet

		//TODO: Player Join Server Packet

		//TODO: Player Leave Server Packet
	}
}
