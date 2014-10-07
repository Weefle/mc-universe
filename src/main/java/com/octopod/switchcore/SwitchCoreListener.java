package com.octopod.switchcore;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.switchcore.event.events.NetworkConnectedEvent;
import com.octopod.switchcore.event.events.NetworkMessageInEvent;
import com.octopod.switchcore.event.events.NetworkPacketInEvent;
import com.octopod.switchcore.packets.*;

import java.util.ArrayList;
import java.util.Arrays;
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

		SwitchCore.getEventManager().callEvent(event);
		SwitchCore.getInterface().broadcast(
			SwitchCore.prefix() + "&7Successfully connected via &a" +
			SwitchCore.getConnection().getName() + "&7!"
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
		Logger logger = SwitchCore.getLogger();
		Server server = SwitchCore.getServerDatabase().getServer(serverID);

		//Checks for mismatched plugin versions between servers, and warns the server owners.
		String version = server.getServerVersion();
		if(!version.equals("TEST_BUILD") && !version.equals(SwitchCore.getPlugin().getPluginVersion()))
		{
			logger.i("&a" + serverID + "&7: Running &6Net+&7 version &6" + (version.equals("") ? "No Version" : version));
		}

//		if(info.containsValue(ServerValue.HUB_PRIORITY))
//		{
//			//Checks if this server is a hub (priority above 0)
//			int hubPriority = (int)info.get(ServerValue.HUB_PRIORITY);
//			if(hubPriority >= 0) {
//				HubManager.addHub(serverID, hubPriority);
//				logger.i("Server &a" + serverID + "&7 registered as hub @ priority &e" + hubPriority);
//			}
//		}

		//Adds in the new flags
		//SwitchCore.getServerDB().set(serverID, flags);


//		logger.v("Recieved server info from &a" + serverID + ":");
//		for(Map.Entry<String, Object> entry: flags.toMap().entrySet()) {
//			logger.i("    &b" + entry.getKey() + ": &e" + entry.get() + "&e");
//		}

	}

	public static void onPlayerJoinServer(String playerID, String serverID)
	{
		Server server = SwitchCore.getServerDatabase().getServer(serverID);

		@SuppressWarnings("unchecked")
		List<String> players = new ArrayList<>(Arrays.asList(server.getOnlinePlayers()));
		if(!players.contains(playerID))
			players.add(playerID);
		server.setValue(ServerValue.ONLINE_PLAYERS, players.toArray(new String[players.size()]));
	}

	public static void onPlayerLeaveServer(String playerID, String serverID)
	{
		Server server = SwitchCore.getServerDatabase().getServer(serverID);

		@SuppressWarnings("unchecked")
		List<String> players = new ArrayList<>(Arrays.asList(server.getOnlinePlayers()));
		if(players.contains(playerID)) players.remove(playerID);
		server.setValue(ServerValue.ONLINE_PLAYERS, players.toArray(new String[players.size()]));
	}

	/**
	 * Runs the action for when any players joins the queue on the network.
	 * This method should add the player to the respective queue.
	 */
	public static void onPlayerJoinQueue(String player, String serverID, int queuePosition)
	{
		Logger logger = SwitchCore.getLogger();
		logger.i(
			"&b" + player + " &7joined the queue for &a" + serverID +
			" &7in position: &a" + queuePosition
		);
		// If the message is to this server, add to queue.
//		if (serverID.equalsIgnoreCase(SwitchCore.getServerFlags().getServerName()))
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
		Logger logger = SwitchCore.getLogger();
		logger.i(
			"&b" + player + " &7left the queue for &a" + serverID);
		// If the server is this server, update queue.
//		if (serverID
//			.equalsIgnoreCase(SwitchCore.getServerFlags().getServerName())) {
//			QueueManager.instance.updateQueue();
//		}
	}

	/**
	 * Listens for when any message is recieved from any server.
	 * This method is a gateway to other events and features.
	 * Refer to NetworkConfig.MessageChannel to what each channel does.
	 */
	public static void onPacketInbound(String servername, SwitchPacket packet)
	{
		NetworkPacketInEvent event = new NetworkPacketInEvent(servername, packet);

		SwitchCore.getEventManager().callEvent(event);
		if(event.isCancelled()) return;

		if(packet instanceof PacketOutServerPing)
		{
			new PacketInServerPing(((PacketOutServerPing)packet).getPingID());
		}

		if(packet instanceof PacketInServerPing)
		{
			return;
		}

		if(packet instanceof PacketOutServerDiscover)
		{
			new PacketInServerDiscover().send(servername);
			if(servername.equals(SwitchCore.getServerIdentifier())) return;
			SwitchCore.getServerDatabase().setServer(((PacketOutServerDiscover)packet).getServer());
		}

		if(packet instanceof PacketInServerDiscover)
		{
			if(servername.equals(SwitchCore.getServerIdentifier())) return;
			SwitchCore.getServerDatabase().setServer(((PacketInServerDiscover)packet).getServer());
		}

		if(packet instanceof PacketOutServerValue)
		{
			new PacketInServerValue(((PacketOutServerValue)packet).getValue()).send(servername);
		}

		if(packet instanceof PacketInServerValue)
		{
			PacketInServerValue packet_value = (PacketInServerValue)packet;
			Server server = SwitchCore.getServerDatabase().getServer(servername);
			server.setValue(packet_value.getType(), packet_value.getValue());

			if(packet_value.getType() == ServerValue.LAST_ONLINE)
			{
				long time = (long)packet_value.getValue();
				if(time == -1 && server.getLastOnline() != -1) onServerOnline(servername);
				if(time != -1 && server.getLastOnline() == -1) onServerOffline(servername);
			}
		}

		if(packet instanceof PacketOutPlayerSwitch)
		{
			new PacketInPlayerSwitch(((PacketOutPlayerSwitch)packet).getUUID()).send(servername);
		}

		if(packet instanceof PacketInPlayerSwitch)
		{
			PacketInPlayerSwitch packetIn = (PacketInPlayerSwitch)packet;
			if(packetIn.getResult() == PlayerSwitchResult.SUCCESS)
			{
				SwitchCore.getConnection().sendPlayer(packetIn.getPlayer().getPlayer(), servername);
			}
		}

		if(packet instanceof PacketOutServerSwitchAll)
		{
			String destination = ((PacketOutServerSwitchAll)packet).getDestination();

			for(MinecraftPlayer player: SwitchCore.getInterface().getOnlinePlayers())
			{
				new PacketOutPlayerSwitch(player).send(destination);
			}
		}

		if(packet instanceof PacketOutServerBroadcast)
		{
			String message = ((PacketOutServerBroadcast)packet).getBroadcastMessage();
			SwitchCore.getInterface().broadcast(message);
		}

		//TODO: Player Private Message Packet

		//TODO: Player Chat Packet

		//TODO: Player Join Queue Packet

		//TODO: Player Leave Queue Packet

		//TODO: Player Join Server Packet

		//TODO: Player Leave Server Packet
	}
}
