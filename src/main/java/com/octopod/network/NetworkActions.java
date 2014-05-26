package com.octopod.network;

import java.util.Arrays;
import java.util.Map;

import com.google.gson.JsonSyntaxException;
import com.octopod.network.bukkit.BukkitUtils;
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

        //Generate a new ServerFlags object for this server.
        ServerFlags serverFlags = ServerFlags.generateFlags();

        //Broadcast the serverFlags across the network.
        NetworkPlus.broadcastServerFlags(serverFlags);

        //Then, request serverFlags from every server.
        NetworkPlus.requestServerFlags();
    }

    /**
     * Runs the action for when the server recieves flags from a server.
     * This method should cache the flags as a new ServerFlags object,
     * or merge into the existing ServerFlags object if one exists.
     * @param serverID The ID of the server.
     * @param flags The flags representing information.
     */
    public static void actionRecieveServerFlags(String serverID, ServerFlags flags) {

        ServerFlagsReceivedEvent event = new ServerFlagsReceivedEvent(serverID, flags);

        NetworkPlus.getEventManager().triggerEvent(event);

        if(!event.isCancelled()) {

            if(flags.hasFlag("version"))
            {
                //Version checking
                if(!NetworkPlus.isTestBuild()) {
                    //Checks for mismatched plugin versions between servers, and warns the server owners.
                    String version = flags.getVersion();
                    if(!version.equals("TEST_BUILD") && !version.equals(NetworkPlus.getPluginVersion())) {
                        NetworkPlus.getLogger().info("&a" + serverID + "&7: Running &6Net+&7 version &6" + (version.equals("") ? "No Version" : version));
                    }
                }
            }

            if(flags.hasFlag("hubPriority"))
            {
                //Checks if this server is a hub (priority above 0)
                int hubPriority = flags.getHubPriority();
                if(hubPriority >= 0) {
                    HubManager.addHub(serverID, hubPriority);
                    NetworkPlus.getLogger().info("Server &a" + serverID + "&7 registered as hub @ priority &e" + hubPriority);
                }
            }

            //Adds in the new flags
            ServerManager.addServer(serverID, flags);

            NetworkPlus.getLogger().verbose("Recieved server info from &a" + serverID + ":");
            for(Map.Entry<String, Object> entry: flags.toMap().entrySet()) {
                NetworkPlus.getLogger().verbose("    &b" + entry.getKey() + ": &e" + entry.getValue() + "&e");
            }

            NetworkPlus.getEventManager().triggerEvent(new PostServerFlagsReceivedEvent(serverID, flags));

        }
    }

    public static void actionPlayerJoinServer(String player, String serverID)
    {

    }

    public static void actionPlayerLeaveServer(String player, String serverID)
    {

    }

	/**
	 * Runs the action for when any players joins the queue on the network.
     * This method should add the player to the respective queue.
	 */
	public static void actionPlayerJoinQueueEvent(String player, String serverID,
        int queuePosition) {
		NetworkPlus.getLogger().debug(
				"&b" + player + " &7joined the queue for &a" + serverID
						+ " &7in position: &a" + queuePosition);
		// If the message is to this server, add to queue.
		if (serverID
				.equalsIgnoreCase(NetworkPlus.getServerFlags().getServerName())) {
			NetworkQueueManager.instance.add(player, queuePosition);
		}
	}

	/**
	 * Runs the action for when any players leaves the queue on the network.
     * This method should remove the player from the respective queue.
	 */
	public static void actionPlayerLeaveQueueEvent(String player, String serverID) {
		NetworkPlus.getLogger().debug(
				"&b" + player + " &7left the queue for &a" + serverID);
		// If the server is this server, update queue.
		if (serverID
				.equalsIgnoreCase(NetworkPlus.getServerFlags().getServerName())) {
			NetworkQueueManager.instance.updateQueue();
		}
	}

    /**
     * Listens for when any message is recieved from any server.
     * This method is a gateway to other events and features.
     * Refer to NetworkConfig.MessageChannel to what each channel does.
     */
    public static void actionRecieveMessage(String senderID, String channel, ServerMessage serverMessage)
    {
        MessageEvent event = new MessageEvent(senderID, channel, serverMessage);

        NetworkPlus.getEventManager().triggerEvent(event);

        if(event.isCancelled()) return;

        String message = serverMessage.toString();
        String[] args = serverMessage.getArgs();

        NetworkPlus.getLogger().verbose(
                "Recieved message from &a" + senderID +
                "&7 on channel &b" + channel +
                "&7 with &e" + args.length + "&7 arguments"
        );

		if(NetworkMessageChannel.SERVER_SENDALL.equals(channel))
		{
            for(String player: BukkitUtils.getPlayerNames())
                NetworkPlus.sendPlayer(player, message);
            return;
		}

		if(NetworkMessageChannel.PLAYER_MESSAGE.equals(channel))
		{
			//TODO: Use the ServerMessage arguments to correctly send the 2nd argument (message) to the 1st argument (player)
            return;
		}

		if (NetworkMessageChannel.PLAYER_JOIN_QUEUE.equals(channel)) {
			actionPlayerJoinQueueEvent(args[0], senderID, Integer.parseInt(args[1]));
            return;
        }

		if (NetworkMessageChannel.PLAYER_LEAVE_QUEUE.equals(channel)) {
			actionPlayerLeaveQueueEvent(args[0], senderID);
            return;
        }

		if(NetworkMessageChannel.SERVER_ALERT.equals(channel)) {
		    BukkitUtils.broadcastMessage(message);
            return;
        }

		if(NetworkMessageChannel.SERVER_FLAGS_REQUEST.equals(channel))
        {
            if(!senderID.equals(NetworkPlus.getServerID())) {
                NetworkPlus.sendServerFlags(senderID);
            }
            return;
        }

        if(NetworkMessageChannel.SERVER_FLAGS_CACHE.equals(channel))
        {
            if(args.length == 2) {
                String serverID = args[0];
                String json = args[1];
                ServerFlags flags;

                try {
                    flags = ServerFlags.fromJson(json);
                    actionRecieveServerFlags(serverID, flags);
                    return;
                } catch (JsonSyntaxException e) {
                    //Unable to parse JSON, just move to the bottom.
                    NetworkPlus.getLogger().debug("Unable to parse JSON String as HashMap");
                }
            }
        }

        //If the code reaches this point, something has gone wrong.
        NetworkPlus.getLogger().debug(
                "Recieved incorrect arguments from server &a" + senderID,
                "Channel: &b" + channel,
                "Arguments: &e" + Arrays.asList(args)
        );

	}

}
