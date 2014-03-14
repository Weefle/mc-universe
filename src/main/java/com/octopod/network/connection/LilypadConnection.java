package com.octopod.network.connection;

import com.octopod.network.NetworkConfig;
import com.octopod.network.NetworkPlus;
import com.octopod.network.NetworkPlusPlugin;
import com.octopod.network.events.EventEmitter;
import com.octopod.network.events.network.NetworkConnectedEvent;
import com.octopod.network.util.BukkitUtils;

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

    /**
     * A nested class for Lilypad's listeners, which will just redirect to our event system.
     */
    private static class Listener
    {
        @EventListener
        public void messageReceived(MessageEvent event) {

            String channel = event.getChannel(), username, message;

            if(!channel.startsWith(NetworkConfig.getRequestPrefix())) {
                return;
            }

            try {
                username = event.getSender();
                message = event.getMessageAsString();
            } catch (UnsupportedEncodingException e) {
                return;
            }

            EventEmitter.getEmitter().triggerEvent(new com.octopod.network.events.relays.MessageEvent(username, channel, message));

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

    /**
     * Gets the Connect instance and ensures that it is connected.
     * This should fire the NetworkConnectedEvent after it connects.
     * @param plugin
     */
    public LilypadConnection(final NetworkPlusPlugin plugin) {

        connection = plugin.getServer().getServicesManager().getRegistration(Connect.class).getProvider();

        //Unregisters any listeners if they exist.
        if(listener != null) {
            connection.unregisterEvents(listener);
        }

        connection.registerEvents(listener = new Listener());

        if(connection != null && !connection.isConnected()) {

            new Thread(new Runnable() {

                @Override
                public void run()
                {
                    if(!connection.isConnected()) {
                        BukkitUtils.console("Waiting for LilyPad to connect...");
                        int connectionAttempts = 0;

                        //Waits for LilyPad to be connected
                        while(!connection.isConnected() && connectionAttempts < 10) {
                            try {
                                BukkitUtils.console("Waiting for LilyPad connection... " + connectionAttempts);
                                synchronized(this) {
                                    wait(1000);
                                }
                            } catch (InterruptedException e) {}
                            connectionAttempts++;
                        }

                        //If LilyPad still isn't connected, then most commands should be disabled.
                        if(!connection.isConnected())
                        {
                            NetworkPlus.getLogger().info("&cLilypad could not connect!");
                            plugin.disablePlugin();
                            return;
                        }
                    }
                    EventEmitter.getEmitter().triggerEvent(new NetworkConnectedEvent());
                }

            }).start();
        } else
        if(isConnected()) {
            EventEmitter.getEmitter().triggerEvent(new NetworkConnectedEvent());
        }

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
        if(result.getStatusCode() == StatusCode.SUCCESS) {
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
