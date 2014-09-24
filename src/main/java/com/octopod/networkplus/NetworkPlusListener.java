package com.octopod.networkplus;

import com.octopod.networkplus.event.events.NetworkConnectedEvent;
import com.octopod.networkplus.event.events.NetworkMessageEvent;
import com.octopod.networkplus.network.NetworkChannel;
import com.octopod.networkplus.requests.ServerInfoReturn;
import com.octopod.networkplus.requests.ServerOnlineMessage;
import com.octopod.networkplus.requests.ServerPingReturn;
import com.octopod.networkplus.server.ServerPlayer;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkPlusListener
{
	/**
	 * Listens for when this plugin is connected according to the NetworkConnection instance.
	 * This method should request ServerValueManager and playerlists from every server.
	 */
	public static void onNetworkConnected()
	{
		NetworkConnectedEvent event = new NetworkConnectedEvent();

		NetworkPlus.getEventManager().callEvent(event);
		NetworkPlus.getServer().broadcast(
			NetworkPlus.prefix() + "&7Successfully connected via &a" +
			NetworkPlus.getConnection().getName() + "&7!"
		);

		NetworkPlus.getConnection().broadcastNetworkRequest(new ServerOnlineMessage());
	}

	/**
	 * Runs the action for when the server recieves flags from a server.
	 * This method should cache the flags as a new ServerFlags object,
	 * or merge into the existing ServerFlags object if one exists.
	 * @param serverID the server's identifier
	 * @param event The flags representing information.
	 */
	public static void onServerInfoRecieved(String serverID, NetworkMessageEvent event)
	{
		Logger logger = NetworkPlus.getLogger();
		ServerInfo info = NetworkPlus.getDatabase().getServerInfo(serverID);

		if(info.containsValue(ServerValue.SERVER_VERSION))
		{
			//Checks for mismatched plugin versions between servers, and warns the server owners.
			String version = (String)info.getValue(ServerValue.SERVER_VERSION);
			if(!version.equals("TEST_BUILD") && !version.equals(NetworkPlus.getPlugin().getPluginVersion())) {
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

	public static void onPlayerJoinServer(String playerID, String serverID)
	{
		ServerInfo info = NetworkPlus.getDatabase().getServerInfo(serverID);

		if(info.containsValue(ServerValue.ONLINE_PLAYERS))
		{
			@SuppressWarnings("unchecked")
			List<String> players = (List<String>)info.getValue(ServerValue.ONLINE_PLAYERS);
			if(!players.contains(playerID))
				players.add(playerID);
			info.setValue(ServerValue.ONLINE_PLAYERS, players);
		}
	}

	public static void onPlayerLeaveServer(String playerID, String serverID)
	{
		ServerInfo info = NetworkPlus.getDatabase().getServerInfo(serverID);

		if(info.containsValue(ServerValue.ONLINE_PLAYERS))
		{
			@SuppressWarnings("unchecked")
			List<String> players = (List<String>)info.getValue(ServerValue.ONLINE_PLAYERS);
			if(players.contains(playerID)) players.remove(playerID);
			info.setValue(ServerValue.ONLINE_PLAYERS, players);
		}
	}

	/**
	 * Runs the action for when any players joins the queue on the network.
	 * This method should add the player to the respective queue.
	 */
	public static void onPlayerJoinQueue(String player, String serverID, int queuePosition)
	{
		Logger logger = NetworkPlus.getLogger();
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
	public static void onPlayerLeaveQueue(String player, String serverID)
	{
		Logger logger = NetworkPlus.getLogger();
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
	public static void onRecieveMessage(String senderID, String channel, String message)
	{
		NetworkMessageEvent event = new NetworkMessageEvent(senderID, channel, message);

		NetworkPlus.getEventManager().callEvent(event);
		if(event.isCancelled()) return;

		NetworkChannel netChannel = NetworkChannel.getByString(channel);
		if(netChannel != null) //This is on an official channel
		{
			String[] args = event.getParsed();
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
					new ServerInfoReturn().send(senderID);
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
					for(ServerPlayer player: NetworkPlus.getServer().getOnlinePlayers()) player.redirect(message);
				case PLAYER_MESSAGE:
					break;
				case PLAYER_JOIN_QUEUE:
					//onPlayerJoinQueue(args[0], senderID, Integer.parseInt(args[1]));
					break;
				case PLAYER_LEAVE_QUEUE:
					//onPlayerLeaveQueue(args[0], senderID);
					break;
				case PLAYER_JOIN_SERVER:
					//actionPlayerJoinServer(args[1], args[0]);
					break;
				case PLAYER_LEAVE_SERVER:
					//actionPlayerLeaveServer(args[1], args[0]);
					break;
				case SERVER_ALERT:
					NetworkPlus.getServer().broadcast(args[0]);
					break;
			}
		}
	}
}
