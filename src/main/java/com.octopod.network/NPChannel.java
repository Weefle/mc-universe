package com.octopod.network;

/**
 * @author Octopod
 *         Created on 5/14/14
 */

/**
 * The Messaging Channels to use.
 */
public enum NPChannel {

    /**
     *  Requests a server to add a player to their queue.
     *  ARGS: Player, Server ID, Place (?)
     */
    PLAYER_JOIN_QUEUE ("player.queue.join"),

    /**
     *  Requests a server to clear a player from their queue.
     *  ARGS: Player, Server ID, Place (?)
     */
    PLAYER_LEAVE_QUEUE ("player.queue.leave"),

    /**
     * Requests a server to add a player to their ServerFlags entry.
     * ARGS: Player, Server
     */
    PLAYER_JOIN_SERVER ("player.server.join"),

    /**
     * Requests a server to clear a player to their ServerFlags entry.
     * ARGS: Player, Server
     */
    PLAYER_LEAVE_SERVER ("player.server.leave"),

    /**
     *  To be used in combination with broadcastMessage()
     *  Requests a server to send a player a message, if they are online.
     *  ARGS: Player, Message
     */
    PLAYER_MESSAGE ("player.message"),

    /**
     *  Requests a server to broadcast a message to everyone.
     *  ARGS: Message
     */
    SERVER_ALERT ("server.alert"),

    /**
     *  Requests a server to send all players on it to another server.
     *  ARGS: Server ID
     */
    SERVER_SENDALL ("server.sendall"),

    /**
     *  Requests a server to send back their current ServerFlags object (via SERVER_FLAGS_CACHE channel)
     *  ARGS: (none)
     */
    SERVER_FLAGS_REQUEST("server.flags.request"),

    /**
     *  Requests a server to cache a ServerFlags object (via a JSON object of a HashMap)
     *  If the server is already cached, the HashMap will merge into the existing one instead.
     *  ARGS: Server ID, HashMap (full or partial) (in JSON)
     */
    SERVER_FLAGS_CACHE("server.flags.cache");

    private String suffix;

    private NPChannel(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Returns if the provided channel matches the toString() of this object.
     * @param channel The channel
     * @return if it matches toString()
     */
    public boolean equals(String channel) {
        return toString().equals(channel);
    }

    public String toString() {
        return NPConfig.getChannelPrefix() + '.' + suffix;
    }

}