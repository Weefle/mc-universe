package com.octopod.network.connection;

import com.octopod.network.NetworkPlusPlugin;
import com.octopod.network.util.RequestUtils;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author Octopod
 *         Created on 3/14/14
 */
public class LilypadConnection extends NetworkConnection {

    /**
     * The Lilypad connection instancea
     */
    private Connect connection;

    public LilypadConnection(NetworkPlusPlugin plugin) {
        connection = plugin.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
    }

    /**
     * Sends a Lilypad request. Returns the result, or null if an exception occured.
     * @param request The request to send.
     * @return The Result object.
     */
    private Result sendRequest(Request request) {
        try {
            return connection.request(request).awaitUninterruptibly();
        } catch (RequestException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isConnected() {
        return connection.isConnected();
    }

    @Override
    public String getUsername() {
        return connection.getSettings().getUsername();
    }

    @Override
    //This method will just do nothing if the request somehow fails or can't be encoded.
    public void sendMessage(String server, String channel, String message) {
        try {
            sendRequest(new MessageRequest(server, channel, message));
        } catch (UnsupportedEncodingException e) {}
    }

    @Override
    public void sendMessage(List<String> servers, String channel, String message) {
        try {
            sendRequest(new MessageRequest(servers, channel, message));
        } catch (UnsupportedEncodingException e) {}
    }

    @Override
    public void broadcastMessage(String channel, String message) {
        try {
            sendRequest(new MessageRequest((String)null, channel, message));
        } catch (UnsupportedEncodingException e) {}
    }

    @Override
    public boolean sendPlayer(String player, String server) {
        return RequestUtils.request(new RedirectRequest(server, player)).getStatusCode() == StatusCode.SUCCESS;
    }

}
