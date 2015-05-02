package com.octopod.switchcore;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.switchcore.event.events.NetworkConnectedEvent;
import com.octopod.switchcore.event.events.NetworkMessageInEvent;
import com.octopod.switchcore.event.events.NetworkPacketInEvent;
import com.octopod.switchcore.packets.*;
import com.octopod.switchcore.server.networked.NetworkedServer;
import com.octopod.switchcore.server.ServerStatus;
import com.octopod.switchcore.server.StaticProperties.LAST_PACKET;
import com.octopod.switchcore.server.StaticProperties.ONLINE_PLAYERS;
import com.octopod.switchcore.server.StaticProperties.STATUS;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class SwitchCoreListener
{
	/**
	 * Listens for when this plugin is connected according to the NetworkConnection instance.
	 * This method should request ServerValueManager and playerlists from every server.
	 */
	public static void onNetworkConnected()
	{
		NetworkConnectedEvent event = new NetworkConnectedEvent();

		SwitchCore.getInstance().getEventBus().post(event);
		SwitchCore.getInstance().getInterface().broadcast(
			SwitchCore.getInstance().prefix() + "&7Successfully connected via &a" +
			SwitchCore.getInstance().getConnection().getName() + "&7!"
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
		Logger logger = SwitchCore.getInstance().getLogger();
		NetworkedServer server = SwitchCore.getInstance().getServerDatabase().getServer(serverID);

		//Checks for mismatched plugin versions between servers, and warns the server owners.
		SwitchCoreVersion version = server.getSwitchVersion();
		if(version != SwitchCoreVersion.LATEST)
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
		//SwitchCore.getServerDB().set(server_id, flags);


//		logger.v("Recieved server info from &a" + server_id + ":");
//		for(Map.Entry<String, Object> entry: flags.toMap().entrySet()) {
//			logger.i("    &b" + entry.getKey() + ": &e" + entry.get() + "&e");
//		}

	}

	public static void onPlayerJoinServer(String playerID, String serverID)
	{
		NetworkedServer server = SwitchCore.getInstance().getServerDatabase().getServer(serverID);

		@SuppressWarnings("unchecked")
		List<String> players = server.getProperty(ONLINE_PLAYERS.class);
		if(!players.contains(playerID))
			players.add(playerID);
		server.setProperty(ONLINE_PLAYERS.class, players);
	}

	public static void onPlayerLeaveServer(String playerID, String serverID)
	{
		NetworkedServer server = SwitchCore.getInstance().getServerDatabase().getServer(serverID);

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
		Logger logger = SwitchCore.getInstance().getLogger();
		logger.i(
			"&b" + player + " &7joined the queue for &a" + serverID +
			" &7in position: &a" + queuePosition
		);
		// If the message is to this server, add to queue.
//		if (server_id.equalsIgnoreCase(SwitchCore.getServerFlags().getServerName()))
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
		Logger logger = SwitchCore.getInstance().getLogger();
		logger.i(
			"&b" + player + " &7left the queue for &a" + serverID);
		// If the server is this server, update queue.
//		if (server_id
//			.equalsIgnoreCase(SwitchCore.getServerFlags().getServerName())) {
//			QueueManager.instance.updateQueue();
//		}
	}

	/**
	 * Listens for when any message is recieved from any server.
	 * This method is a gateway to other events and features.
	 * Refer to NetworkConfig.MessageChannel to what each channel does.
	 */
	public static void onPacketInbound(String serverID, SwitchPacket packetRaw)
	{
		NetworkPacketInEvent event = new NetworkPacketInEvent(serverID, packetRaw);

		SwitchCore.getInstance().getEventBus().post(event);
		if(event.isCancelled()) return;

		SwitchCore.getInstance().getServer(serverID).setProperty(LAST_PACKET.class, System.currentTimeMillis());

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
			if(serverID.equals(SwitchCore.getInstance().getServerIdentifier())) return;
			SwitchCore.getInstance().getServerDatabase().setServer(((PacketInServerDiscover)packetRaw).getServer());
		}

		if(packetRaw instanceof PacketOutServerDiscover)
		{
			if(serverID.equals(SwitchCore.getInstance().getServerIdentifier())) return;
			SwitchCore.getInstance().getServerDatabase().setServer(((PacketOutServerDiscover)packetRaw).getServer());
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
				SwitchCore.getInstance().getLogger().w("Server \"" + serverID + "\" requested nonexistant property " + packet.getPropertyClassName());
			}
		}

		if(packetRaw instanceof PacketOutServerUpdate)
		{
			PacketOutServerUpdate packet = (PacketOutServerUpdate)packetRaw;
			try
			{
				NetworkedServer server = SwitchCore.getInstance().getServerDatabase().getServer(serverID);
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
				SwitchCore.getInstance().getLogger().w("Server \"" + serverID + "\" gave us nonexistant property " + packet.getPropertyClassName());
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
				SwitchCore.getInstance().getConnection().sendPlayer(packetIn.getPlayer().getPlayer(), serverID);
			}
		}

		if(packetRaw instanceof PacketInServerSwitchAll)
		{
			String destination = ((PacketInServerSwitchAll)packetRaw).getDestination();

			for(MinecraftPlayer player: SwitchCore.getInstance().getInterface().getOnlinePlayers())
			{
				new PacketInPlayerSwitch(player).send(destination);
			}
		}

		if(packetRaw instanceof PacketInServerBroadcast)
		{
			String message = ((PacketInServerBroadcast)packetRaw).getBroadcastMessage();
			SwitchCore.getInstance().getInterface().broadcast(message);
		}

		//TODO: Player Private Message Packet

		//TODO: Player Chat Packet

		//TODO: Player Join Queue Packet

		//TODO: Player Leave Queue Packet

		//TODO: Player Join Server Packet

		//TODO: Player Leave Server Packet
	}
}
