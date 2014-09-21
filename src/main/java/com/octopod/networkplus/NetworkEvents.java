package com.octopod.networkplus;

import com.octopod.networkplus.event.events.NetworkConnectedEvent;
import com.octopod.networkplus.event.events.NetworkMessageEvent;
import com.octopod.networkplus.network.NetworkChannel;
import com.octopod.networkplus.requests.ServerOnlineMessage;
import com.octopod.networkplus.requests.ServerPingRequest;
import com.octopod.networkplus.requests.ServerPingReturn;
import com.octopod.networkplus.server.*;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkEvents
{

	/**
	 * Listens for when this plugin is connected according to the NetworkConnection instance.
	 * This method should request ServerValueManager and playerlists from every server.
	 */
	public static void actionNetworkConnected()
	{
		NetworkConnectedEvent event = new NetworkConnectedEvent();
		NetworkPlus networkPlus = NetworkPlus.getInstance();

		networkPlus.getEventManager().triggerEvent(event);
		networkPlus.getServer().broadcast(
			networkPlus.prefix() + "&7Successfully connected via &a" +
			networkPlus.getConnection().getName() + "&7!"
		);

		networkPlus.getConnection().broadcastNetworkRequest(new ServerOnlineMessage());
		networkPlus.getConnection().broadcastNetworkRequest(new ServerPingRequest());
	}

	/**
	 * Runs the action for when the server recieves flags from a server.
	 * This method should cache the flags as a new ServerFlags object,
	 * or merge into the existing ServerFlags object if one exists.
	 * @param serverID the server's identifier
	 * @param event The flags representing information.
	 */
	public static void recieveServerInfoEvent(String serverID, NetworkMessageEvent event)
	{
		NetworkPlus networkPlus = NetworkPlus.getInstance();
		ServerLogger logger = networkPlus.getLogger();
		ServerInfo info = networkPlus.getDatabase().getServerInfo(serverID);

		if(info.containsValue(ServerValue.SERVER_VERSION))
		{
			//Checks for mismatched plugin versions between servers, and warns the server owners.
			String version = (String)info.getValue(ServerValue.SERVER_VERSION);
			if(!version.equals("TEST_BUILD") && !version.equals(networkPlus.getPlugin().getPluginVersion())) {
				logger.i("&a" + serverID + "&7: Running &6Net+&7 version &6" + (version.equals("") ? "No Version" : version));
			}
		}

//		if(info.containsValue(ServerValue.HUB_PRIORITY))
//		{
//			//Checks if this server is a hub (priority above 0)
//			int hubPriority = (int)info.getValue(ServerValue.HUB_PRIORITY);
//			if(hubPriority >= 0) {
//				HubManager.addHub(serverID, hubPriority);
//				logger.i("Server &a" + serverID + "&7 registered as hub @ priority &e" + hubPriority);
//			}
//		}

		//Adds in the new flags
		//NetworkPlus.getServerDB().set(serverID, flags);


//		logger.v("Recieved server info from &a" + serverID + ":");
//		for(Map.Entry<String, Object> entry: flags.toMap().entrySet()) {
//			logger.i("    &b" + entry.getKey() + ": &e" + entry.getValue() + "&e");
//		}

	}

	public static void playerJoinServerEvent(String playerID, String serverID)
	{
		NetworkPlus networkPlus = NetworkPlus.getInstance();
		ServerInfo info = networkPlus.getDatabase().getServerInfo(serverID);

		if(info.containsValue(ServerValue.ONLINE_PLAYERS))
		{
			@SuppressWarnings("unchecked")
			List<String> players = (List<String>)info.getValue(ServerValue.ONLINE_PLAYERS);
			if(!players.contains(playerID))
				players.add(playerID);
			info.setValue(ServerValue.ONLINE_PLAYERS, players);
		}
	}

	public static void playerLeaveServerEvent(String playerID, String serverID)
	{
		NetworkPlus networkPlus = NetworkPlus.getInstance();
		ServerInfo info = networkPlus.getDatabase().getServerInfo(serverID);

		if(info.containsValue(ServerValue.ONLINE_PLAYERS))
		{
			@SuppressWarnings("unchecked")
			List<String> players = (List<String>)info.getValue(ServerValue.ONLINE_PLAYERS);
			if(players.contains(playerID))
				players.remove(playerID);
			info.setValue(ServerValue.ONLINE_PLAYERS, players);
		}
	}

	/**
	 * Runs the action for when any players joins the queue on the network.
	 * This method should add the player to the respective queue.
	 */
	public static void playerJoinQueueEvent(String player, String serverID, int queuePosition)
	{
		NetworkPlus networkPlus = NetworkPlus.getInstance();
		ServerLogger logger = networkPlus.getLogger();
		logger.i(
			"&b" + player + " &7joined the queue for &a" + serverID +
			" &7in position: &a" + queuePosition
		);
		// If the message is to this server, add to queue.
//		if (serverID.equalsIgnoreCase(NetworkPlus.getServerFlags().getServerName()))
//		{
//			QueueManager.instance.add(player, queuePosition);
//		}
	}

	/**
	 * Runs the action for when any players leaves the queue on the network.
	 * This method should clear the player from the respective queue.
	 */
	public static void playerLeaveQueueEvent(String player, String serverID)
	{
		NetworkPlus networkPlus = NetworkPlus.getInstance();
		ServerLogger logger = networkPlus.getLogger();
		logger.i(
			"&b" + player + " &7left the queue for &a" + serverID);
		// If the server is this server, update queue.
//		if (serverID
//			.equalsIgnoreCase(NetworkPlus.getServerFlags().getServerName())) {
//			QueueManager.instance.updateQueue();
//		}
	}

	/**
	 * Listens for when any message is recieved from any server.
	 * This method is a gateway to other events and features.
	 * Refer to NetworkConfig.MessageChannel to what each channel does.
	 */
	public static void messageEvent(String senderID, String channel, String message)
	{
		NetworkMessageEvent event = new NetworkMessageEvent(senderID, channel, message);

		NetworkPlus networkPlus = NetworkPlus.getInstance();
		ServerLogger logger = networkPlus.getLogger();

		if(event.isCancelled()) return;

		logger.v
			(
				"Recieved message from &a" + senderID +
					"&7 on channel &b" + channel
			);

		NetworkChannel netChannel = NetworkChannel.getByString(channel);
		if(netChannel != null) //This is on an official channel
		{
			@SuppressWarnings("unchecked")
			List<String> decoded = networkPlus.getSerializer().decode(message, List.class);
			String[] args = decoded.toArray(new String[decoded.size()]);
			switch(netChannel)
			{
				case SERVER_PING_REQUEST:
					//Return the ping
					new ServerPingReturn().send(senderID);
					break;
				case SERVER_PING_RETURN:
					//Do nothing
					break;
				case SERVER_INFO_REQUEST:
					//Message: array of requested value names
					//TODO: serialize local values and return
					break;
				case SERVER_INFO_RETURN:
					//Message: map of value names and values, to be deserialized
					//TODO: deserialize the message and save in database
					break;
				case SERVER_ONLINE:
					//TODO: run event for server going online
					break;
				case SERVER_OFFLINE:
					//TODO: run event for server going offline (with time as argument)
					break;
				case SERVER_SENDALL:
					//Message is the server name
					for(ServerPlayer player: networkPlus.getServer().getOnlinePlayers()) player.redirect(message);
				case PLAYER_MESSAGE:
					break;
				case PLAYER_JOIN_QUEUE:
					//playerJoinQueueEvent(args[0], senderID, Integer.parseInt(args[1]));
					break;
				case PLAYER_LEAVE_QUEUE:
					//playerLeaveQueueEvent(args[0], senderID);
					break;
				case PLAYER_JOIN_SERVER:
					//actionPlayerJoinServer(args[1], args[0]);
					break;
				case PLAYER_LEAVE_SERVER:
					//actionPlayerLeaveServer(args[1], args[0]);
					break;
				case SERVER_ALERT:
					networkPlus.getServer().broadcast(args[0]);
					break;
			}
		}
	}
}
