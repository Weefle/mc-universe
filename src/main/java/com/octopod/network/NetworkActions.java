package com.octopod.network;

import java.util.Arrays;
import java.util.HashMap;

import com.google.gson.JsonSyntaxException;
import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.network.cache.NetworkHubCache;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.events.network.NetworkConnectedEvent;
import com.octopod.network.events.relays.MessageEvent;
import com.octopod.network.events.server.PostServerFlagsReceivedEvent;
import com.octopod.network.events.server.ServerFlagsReceivedEvent;

/**
 * @author Octopod
 */
public class NetworkActions {

    /**
     * Listens for when this plugin is connected according to the NetworkConnection instance.
     * This method should request ServerInfo and playerlists from every server.
     */
    public static void actionNetworkConnected()
    {
        NetworkConnectedEvent event = new NetworkConnectedEvent();

        NetworkPlus.getEventManager().triggerEvent(event);

        BukkitUtils.broadcastMessage(
                "&7Successfully connected via &a" +
                NetworkPlus.getConnection().getConnectionType() +
                "&7!"
        );

        NetworkPlus.requestServerInfo();
    }

    /**
     * Runs the action for when the server recieves flags from a server.
     * This method should cache the flags as a new ServerFlags object,
     * or merge into the existing ServerFlags object if one exists.
     * @param server The ID of the server.
     * @param flags The flags representing information.
     */
    public static void actionRecieveServerFlags(String server, HashMap<String, Object> flags) {

        ServerFlagsReceivedEvent event = new ServerFlagsReceivedEvent(server, flags);

        NetworkPlus.getEventManager().triggerEvent(event);

        if(!event.isCancelled()) {

            //Adds in the new flags and returns the resultant ServerFlags object.
            ServerFlags serverInfo = NetworkServerCache.addServer(server, flags);

            //Checks if this server is a hub (priority above 0)
            int hubPriority = serverInfo.getHubPriority();
            if(hubPriority >= 0) {
                NetworkHubCache.addHub(server, hubPriority);
                NetworkPlus.getLogger().info("Server &a" + server + "&7 registered as hub @ priority &e" + hubPriority);
            }

            //Version checking
            if(!NetworkPlus.isTestBuild()) {
                //Checks for mismatched plugin versions between servers, and warns the server owners.
                String version = serverInfo.getVersion();
                if(!version.equals("TEST_BUILD") && !version.equals(NetworkPlus.getPluginVersion())) {
                    NetworkPlus.getLogger().info("&a" + server + "&7: Running &6Net+&7 version &6" + (version.equals("") ? "No Version" : version));
                }
            }

            NetworkPlus.getEventManager().triggerEvent(new PostServerFlagsReceivedEvent(serverInfo));

        }

    }

	/**
	 * Runs the action for when any players joins the queue on the network.
     * This method should add the player to the respective queue.
	 */
	public static void actionPlayerJoinQueueEvent(String player, String server,
        int queuePosition) {
		NetworkPlus.getLogger().debug(
				"&b" + player + " &7joined the queue for &a" + server
						+ " &7in position: &a" + queuePosition);
		// If the message is to this server, add to queue.
		if (server
				.equalsIgnoreCase(NetworkPlus.getServerInfo().getServerName())) {
			NetworkQueueManager.instance.add(player, queuePosition);
		}
	}

	/**
	 * Runs the action for when any players leaves the queue on the network.
     * This method should remove the player from the respective queue.
	 */
	public static void actionPlayerLeaveQueueEvent(String player, String server) {
		NetworkPlus.getLogger().debug(
				"&b" + player + " &7left the queue for &a" + server);
		// If the server is this server, update queue.
		if (server
				.equalsIgnoreCase(NetworkPlus.getServerInfo().getServerName())) {
			NetworkQueueManager.instance.updateQueue();
		}
	}

    /**
     * Listens for when any message is recieved from any server.
     * This method is a gateway to other events and features.
     * Refer to NetworkConfig.Channels to what each channel does.
     */
    public static void actionRecieveMessage(String sender, String channel, ServerMessage serverMessage)
    {
        //Return if sender is this server; Cannot self-message.
        //In case different APIs don't allow self-messaging.
        if(sender.equals(NetworkPlus.getUsername())) return;

        MessageEvent event = new MessageEvent(sender, channel, serverMessage);

        NetworkPlus.getEventManager().triggerEvent(event);

        if(event.isCancelled()) return;

        String message = serverMessage.toString();
        String[] args = serverMessage.getArgs();

		if(NetworkConfig.Channels.SERVER_SENDALL.equals(channel))
		{
            for(String player: BukkitUtils.getPlayerNames())
                NetworkPlus.sendPlayer(player, message);
            return;
		}

		if(NetworkConfig.Channels.PLAYER_MESSAGE.equals(channel))
		{
			//TODO: Use the ServerMessage arguments to correctly send the 2nd argument (message) to the 1st argument (player)
            return;
		}

		if (NetworkConfig.Channels.PLAYER_JOIN_QUEUE.equals(channel)) {
			actionPlayerJoinQueueEvent(message.split(":")[0], sender, Integer.parseInt(message.split(":")[1]));
            return;
        }

		if (NetworkConfig.Channels.PLAYER_LEAVE_QUEUE.equals(channel)) {
			actionPlayerLeaveQueueEvent(message.split(":")[0], sender);
            return;
        }

		if(NetworkConfig.Channels.SERVER_ALERT.equals(channel)) {
		    BukkitUtils.broadcastMessage(message);
            return;
        }

		if(NetworkConfig.Channels.SERVER_FLAGS_REQUEST.equals(channel))
        {
            NetworkPlus.sendMessage(sender, NetworkConfig.Channels.SERVER_FLAGS_CACHE.toString(),
                    NetworkPlus.getServerInfo().asMessage()
            );
            return;
        }

        if(NetworkConfig.Channels.SERVER_FLAGS_CACHE.equals(channel))
        {
            if(args.length == 2) {
                String server = args[0];
                String json = args[1];
                HashMap<String, Object> flags;

                try {
                    flags = Util.mapFromJson(json);
                    actionRecieveServerFlags(server, flags);
                    return;
                } catch (JsonSyntaxException e) {
                    //Unable to parse JSON, just move to the bottom.
                    NetworkPlus.getLogger().debug("Unable to parse JSON String as HashMap");
                }
            }
        }

        //If the code reaches this point, something has gone wrong.
        NetworkPlus.getLogger().debug(
                "Recieved incorrect arguments from server &a" + sender,
                "Channel: &b" + channel,
                "Arguments: &e" + Arrays.asList(args)
        );

	}

}
