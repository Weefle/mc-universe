package com.octopod.network.events.server;

import com.octopod.network.events.Event;

import java.util.HashMap;

/**
 * Fires when the server recieves an updated ServerInfo
 * from a server that is already cached.
 */
public class ServerUpdatedEvent extends Event {

    String server;
    HashMap<String, Object> info;

    public ServerUpdatedEvent(String sender, HashMap<String, Object> info) {
        server = sender;
        this.info = info;
    }

    public String getServer() {
        return server;
    }

    public HashMap<String, Object> getInfo() {
        return info;
    }

}
