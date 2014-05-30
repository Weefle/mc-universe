package com.octopod.network.connection;

import com.google.gson.JsonParseException;
import com.octopod.network.*;
import com.octopod.network.bukkit.BukkitUtils;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;
import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.GetPlayersRequest;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.GetPlayersResult;
import lilypad.client.connect.api.result.impl.MessageResult;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Octopod
 *         Created on 3/14/14
 */
public class LilypadConnection extends NetworkConnection {

    public String getConnectionType() {
        return "LilyPad";
    }

    /**
     * A nested class for Lilypad's listeners, which will just redirect to our event system.
     */
    public static class Listener
    {
        private Listener() {}

        @EventListener
        public void messageReceived(MessageEvent event)
        {
            String channel = event.getChannel(), serverID, message;

            if(!channel.startsWith(NPConfig.getChannelPrefix())) {
                return;
            }

            try {
                serverID = event.getSender();
                message = event.getMessageAsString();
            } catch (UnsupportedEncodingException e) {
                return;
            }

            try {
                NPMessage serverMessage = NPMessage.parse(message);
                NPActions.actionRecieveMessage(serverID, channel, serverMessage);
            } catch (JsonParseException e) {
                //The message probably isn't even a JSON, just ignore it.
            }

        }
    }

    /**
     * The current instance of Lilypad listeners.
     */
    private Listener listener;

    /**
     * The Lilypad connection instance.
     */
    private Connect connection;

    private NetworkPlusPlugin plugin;

    /**
     * Gets the Connect instance and ensures that it is connected.
     * This should fire the NetworkConnectedEvent after it connects.
     * @param plugin
     */
    public LilypadConnection(final NetworkPlusPlugin plugin) {

        this.plugin = plugin;

        connection = plugin.getServer().getServicesManager().getRegistration(Connect.class).getProvider();

        //Unregisters any listeners if they exist.
        if(listener != null) {
            connection.unregisterEvents(listener);
        }

    }

    @Override
    public void disconnect()
    {
        connection.unregisterEvents(listener);
    }

    @Override
    public void connect()
    {
        connection.registerEvents(listener = new Listener());

        if(connection != null && !connection.isConnected()) {

            new Thread(new Runnable() {

                @Override
                public void run()
                {
                    if(!connection.isConnected()) {
                        BukkitUtils.console("Waiting for LilyPad to connect...");

                        int connectionAttempts = 0;
						int maxConnectionAttempts = NPConfig.getConnectionMaxAttempts();
						long connectionAttemptInterval = NPConfig.getConnectionAttemptInterval();

                        while(!connection.isConnected() && connectionAttempts < maxConnectionAttempts) {
                            try {
                                BukkitUtils.console("Waiting for LilyPad connection... " + connectionAttempts);
                                synchronized(this) {
                                    wait(connectionAttemptInterval);
                                }
                            } catch (InterruptedException e) {}
                            connectionAttempts++;
                        }

                        //If LilyPad still isn't connected, then most commands should be disabled.
                        if(!connection.isConnected())
                        {
                            BukkitUtils.console("&cLilypad could not connect!");
                            plugin.disablePlugin();
                            return;
                        }
                    }
                    triggerConnection();
                }

            }).start();
        } else
        if(isConnected()) {
            triggerConnection();
        }
    }

    /**
     * Sends a Lilypad request. Returns the result, or null if an exception occured.
     * @param request The request to send.
     * @return The Result object.
     */
    private Result sendRequest(Request request) {
        try {
            return connection.request(request).awaitUninterruptibly(NPConfig.getRequestTimeout());
        } catch (RequestException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isConnected() {
        return (connection != null && connection.isConnected());
    }

    @Override
    public String getUsername() {
        if(connection == null) return "";
        return connection.getSettings().getUsername();
    }

    @Override
    public boolean serverExists(String server) {
        return sendMessageRequest(server, "", "").getStatusCode() == StatusCode.SUCCESS;
    }

    //This method will just do nothing if the request somehow fails or can't be encoded.
    private MessageResult sendMessageRequest(String server, String channel, String message) {
        try {
            return (MessageResult)sendRequest(new MessageRequest(server, channel, message));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Override
    public void sendMessage(String server, String channel, String message) {
        sendMessageRequest(server, channel, message);
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
    public List<String> getPlayers() {
        GetPlayersResult result = (GetPlayersResult)sendRequest(new GetPlayersRequest(true));
        if(result != null && result.getStatusCode() == StatusCode.SUCCESS) {
            return new ArrayList<>(result.getPlayers());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean sendPlayer(String player, String server) {
        return sendRequest(new RedirectRequest(server, player)).getStatusCode() == StatusCode.SUCCESS;
    }

}
