package com.octopod.network.connection;

import com.octopod.network.NPActions;

import java.util.List;

/**
 * @author Octopod
 *         Created on 3/14/14
 */
public abstract class NetworkConnection {

    protected void triggerConnection() {
        NPActions.actionNetworkConnected();
    }

    public abstract String getConnectionType();

    /**
     * Gets if this server is connected to the other servers.
     * @return
     */
    public abstract boolean isConnected();

    public abstract void connect();

    public abstract void disconnect();

    /**
     * Gets the username of this server.
     * @return
     */
    public abstract String getUsername();

    public abstract boolean serverExists(String server);

    /**
     * Sends a message to another server on a channel.
     * Should fire the MessageRecievedEvent event on the target server.
     * @param server The name of the server to send the message to.
     * @param channel The channel to send the message on.
     * @param message The message.
     */
    public abstract void sendMessage(String server, String channel, String message);

    public abstract void sendMessage(List<String> servers, String channel, String message);

    public abstract void broadcastMessage(String channel, String message);

    public abstract List<String> getPlayers();

    /**
     * Sends a player to a server.
     * @param player The name of the player.
     * @param server The name of the server.
     * @return If the player was successfully sent.
     */
    public abstract boolean sendPlayer(String player, String server);

}
