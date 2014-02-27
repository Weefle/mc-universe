package com.octopod.network;

import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.GetPlayersRequest;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.GetPlayersResult;
import lilypad.client.connect.api.result.impl.MessageResult;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

/**
 * Utilities class to interface with LilyPad.
 * Try not to import Bukkit classes into here.
 * @author Octopod
 */
public class LPRequestUtils {

	public static Set<String> getNetworkedPlayers() {
		return getNetworkedPlayers(true).getPlayers();
	}

	public static boolean serverExists(String server) {
		try {
			return sendDummyMessage(server).await().getStatusCode() == StatusCode.SUCCESS;
		} catch (InterruptedException e) {
			return false;
		}
	}

	/**
	 * Server Info Requester
	 * These methods might cause the ServerInfoEvent to fire. Listen to it!
	 */

    public static void requestServerInfo() {
        Debug.verbose("Requesting info from all servers");
        broadcastMessage(NetworkConfig.getConfig().CHANNEL_INFO_REQUEST, NetworkPlugin.encodeServerInfo());
    }

	public static void requestServerInfo(final List<String> servers) {
		Debug.verbose("Requesting info from: &a" + servers);
        sendMessage(servers, NetworkConfig.getConfig().CHANNEL_INFO_REQUEST, NetworkPlugin.encodeServerInfo());
	}

    public static void requestPlayerList() {
        Debug.verbose("Requesting playerlist from all servers");
        broadcastMessage(NetworkConfig.getConfig().CHANNEL_PLAYERLIST_REQUEST, NetworkPlugin.encodePlayerList());
    }

    public static void requestPlayerList(final List<String> servers) {
        Debug.verbose("Requesting playerlist from: &a" + servers);
        sendMessage(servers, NetworkConfig.getConfig().CHANNEL_PLAYERLIST_REQUEST, NetworkPlugin.encodePlayerList());
    }

	public static boolean isPlayerOnline(String player) {
		GetPlayersResult result = getNetworkedPlayers(true);
		if(result.getStatusCode() != StatusCode.SUCCESS || !result.getPlayers().contains(player)) {
			return false;
		} else {
			return true;
		}
	}

	public static void broadcastServerMessage(String server, String message) {
		if(server.equals(NetworkPlugin.getServerName())) {
			NetworkPlugin.broadcastMessage(message);
		} else {
			sendMessage(server, NetworkConfig.getConfig().CHANNEL_BROADCAST, message);
		}
	}

	public static void broadcastNetworkMessage(String message) {
		broadcastMessage(NetworkConfig.getConfig().CHANNEL_BROADCAST, message);
	}

	public static void sendPlayerMessage(String player, String message) {
		if(NetworkPlugin.isPlayerOnline(player)) {
			NetworkPlugin.sendMessage(player, message);
		} else {
			broadcastMessage(NetworkConfig.getConfig().CHANNEL_MESSAGE, player + " " + message);
		}
	}

	public static void requestSendAll(String server, String destination) {
		sendMessage(server, NetworkConfig.getConfig().CHANNEL_SENDALL, destination);
	}

	public static void requestSendAll(String destination) {
		broadcastMessage(NetworkConfig.getConfig().CHANNEL_SENDALL, destination);
	}

	public static GetPlayersResult getNetworkedPlayers(boolean list) {
		try {
			return NetworkPlugin.connect.request(new GetPlayersRequest(list)).awaitUninterruptibly();
		} catch (RequestException e) {
			return null;
		}
	}

	public static void broadcastMessage(String channel, String message) {
		try {
			NetworkPlugin.connect.request(new MessageRequest((String)null, channel, message));
		} catch (UnsupportedEncodingException | RequestException e) {}
	}

	public static void sendMessage(String server, String channel, String message) {
		try {
			NetworkPlugin.connect.request(new MessageRequest(server, channel, message));
		} catch (UnsupportedEncodingException | RequestException e) {}
	}

	public static void sendMessage(List<String> servers, String channel, String message) {
		try {
			NetworkPlugin.connect.request(new MessageRequest(servers, channel, message));
		} catch (UnsupportedEncodingException | RequestException e) {}
	}

    public static void broadcastEmptyMessage(String channel) {
        broadcastMessage(channel, "");
    }

	public static void sendEmptyMessage(String server, String channel) {
		sendMessage(server, channel, "");
	}

	public static void sendEmptyMessage(List<String> servers, String channel) {
		sendMessage(servers, channel, "");
	}

	public static FutureResult<MessageResult> sendDummyMessage(String server) {
		try {
			return NetworkPlugin.connect.request(new MessageRequest(server, "", ""));
		} catch (UnsupportedEncodingException | RequestException e) {
			return null;
		}
	}

}
