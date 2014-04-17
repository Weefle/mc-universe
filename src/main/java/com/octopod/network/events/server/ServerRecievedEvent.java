package com.octopod.network.events.server;

import com.octopod.network.events.CancellableEvent;

import java.util.HashMap;

/**
 * @author Octopod
 *         Created on 3/26/14
 */
public class ServerRecievedEvent extends CancellableEvent {

    String server;
    HashMap<String, Object> info;

    public ServerRecievedEvent(String sender, HashMap<String, Object> info) {
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
