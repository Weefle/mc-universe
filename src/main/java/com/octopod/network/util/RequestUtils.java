package com.octopod.network.util;

import com.octopod.network.NetworkConfig;
import com.octopod.network.NetworkDebug;
import com.octopod.network.NetworkPlugin;
import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.GetPlayersRequest;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.Result;
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
public class RequestUtils {

    /**
     * Raw request method. Sends and returns the result on a timeout.
     * @param request The request to send.
     * @return The Result of the request.
     */
    public static Result request(Request request) {
        try {
            return NetworkPlugin.getConnection().request(request).awaitUninterruptibly(NetworkConfig.getConfig().getRequestTimeout());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	public static void requestSendAll(String server, String destination) {
		sendMessage(server, NetworkConfig.getConfig().CHANNEL_SENDALL, destination);
	}

	public static void requestSendAll(String destination) {
		broadcastMessage(NetworkConfig.getConfig().CHANNEL_SENDALL, destination);
	}

    public static MessageResult sendMessage(String server, String channel, String message) {
        try {
            return (MessageResult)request(new MessageRequest(server, channel, message));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static MessageResult sendMessage(List<String> servers, String channel, String message) {
        try {
            return (MessageResult)request(new MessageRequest(servers, channel, message));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

	public static void broadcastMessage(String channel, String message) {
        sendMessage((String)null, channel, message);
    }

    public static void broadcastEmptyMessage(String channel) {
        broadcastMessage(channel, "");
    }

    /**
     * Sends an empty message to a server, containing no channel or message.
     * Used to test if sending messages to this server will yield any errors.
     * @param server The name of the server to message.
     * @return The MessageResult of the request.
     */
	public static MessageResult sendDummyMessage(String server) {
        return sendMessage(server, "", "");
    }

}
