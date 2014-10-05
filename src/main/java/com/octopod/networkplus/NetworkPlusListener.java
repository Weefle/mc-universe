package com.octopod.networkplus;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.networkplus.database.ServerDatabase;
import com.octopod.networkplus.event.events.NetworkConnectedEvent;
import com.octopod.networkplus.event.events.NetworkMessageEvent;
import com.octopod.networkplus.exceptions.DeserializationException;
import com.octopod.networkplus.messages.MessageInServerPing;
import com.octopod.networkplus.messages.MessageInServerRequest;
import com.octopod.networkplus.messages.MessageInServerValue;

import java.util.ArrayList;
import java.util.Arrays;
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
		NetworkPlus.getInterface().broadcast(
			NetworkPlus.prefix() + "&7Successfully connected via &a" +
			NetworkPlus.getConnection().getName() + "&7!"
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
	public static void onServerInfoRecieved(String serverID, NetworkMessageEvent event)
	{
		Logger logger = NetworkPlus.getLogger();
		Server server = NetworkPlus.getServerDatabase().getServer(serverID);

		//Checks for mismatched plugin versions between servers, and warns the server owners.
		String version = server.getServerVersion();
		if(!version.equals("TEST_BUILD") && !version.equals(NetworkPlus.getPlugin().getPluginVersion()))
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
		//NetworkPlus.getServerDB().set(serverID, flags);


//		logger.v("Recieved server info from &a" + serverID + ":");
//		for(Map.Entry<String, Object> entry: flags.toMap().entrySet()) {
//			logger.i("    &b" + entry.getKey() + ": &e" + entry.get() + "&e");
//		}

	}

	public static void onPlayerJoinServer(String playerID, String serverID)
	{
		Server server = NetworkPlus.getServerDatabase().getServer(serverID);

		@SuppressWarnings("unchecked")
		List<String> players = new ArrayList<>(Arrays.asList(server.getOnlinePlayers()));
		if(!players.contains(playerID))
			players.add(playerID);
		server.setValue(ServerValue.ONLINE_PLAYERS, players.toArray(new String[players.size()]));
	}

	public static void onPlayerLeaveServer(String playerID, String serverID)
	{
		Server server = NetworkPlus.getServerDatabase().getServer(serverID);

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
	public static void onRecieveMessage(String server, String channel, String message)
	{
		NetworkMessageEvent event = new NetworkMessageEvent(server, channel, message);

		NetworkPlus.getEventManager().callEvent(event);
		if(event.isCancelled()) return;

		StaticChannel netChannel = StaticChannel.getByString(channel);
		if(netChannel != null) //This is on an official channel
		{

			switch(netChannel)
			{
				case SERVER_PING_REQUEST:
					int id = Integer.parseInt(event.arg(0));
					new MessageInServerPing(id).send(server);
					break;
				case SERVER_PING_MESSAGE: break;

				case SERVER_INFO_REQUEST:
					new MessageInServerRequest().send(server);
					break;
				case SERVER_INFO_RETURN:
					if(server.equals(NetworkPlus.getServerIdentifier())) break;
					NetworkPlus.getServerDatabase().setServer(CachedServer.decode(server, event.arg(0)));
					break;

				case SERVER_VALUE_REQUEST:
					new MessageInServerValue(ServerValue.valueOf(event.arg(0))).send(server);
					break;
				case SERVER_VALUE_RETURN:
					try {
						ServerDatabase database = NetworkPlus.getServerDatabase();
						if(database.serverExists(server))
						{
							ServerValue value = ServerValue.valueOf(event.arg(0));
							Object object = NetworkPlus.getSerializer().deserialize(event.arg(1), value.expectedType());
							Server server2 = NetworkPlus.getServerDatabase().getServer(server);
							server2.setValue(value, object);

							if(value == ServerValue.LAST_ONLINE)
							{
								long time = (long)object;
								if(time == -1 && server2.getLastOnline() != -1) onServerOnline(server);
								if(time != -1 && server2.getLastOnline() == -1) onServerOffline(server);
							}
						}
					}
					catch (NullPointerException | DeserializationException e) {e.printStackTrace();}
					break;

				case SERVER_ONLINE:
					//TODO: run event for server going online
					break;
				case SERVER_OFFLINE:
					//TODO: run event for server going offline (with time as argument)
					break;
				case SERVER_SENDALL:
					//Message is the server name
					NetworkConnection connection = NetworkPlus.getConnection();
					for(MinecraftPlayer player: NetworkPlus.getInterface().getOnlinePlayers()) connection.redirectPlayer(player, message);
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
				case SERVER_BROADCAST:
					NetworkPlus.getInterface().broadcast(event.arg(0));
					break;
			}
		}
	}
}
