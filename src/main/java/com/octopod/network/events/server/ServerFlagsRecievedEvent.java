package com.octopod.network.events.server;

import com.octopod.network.events.CancellableEvent;

import java.util.HashMap;

/**
 * @author Octopod
 *         Created on 3/26/14
 */
public class ServerFlagsRecievedEvent extends CancellableEvent {

    String server;
    HashMap<String, Object> flags;

    public ServerFlagsRecievedEvent(String serverID, HashMap<String, Object> flags) {
        server = serverID;
        this.flags = flags;
    }

    public String getServer() {
        return server;
    }

    public HashMap<String, Object> getFlags() {
        return flags;
    }

    public void setFlags(HashMap<String, Object> flags) {
        this.flags = flags;
    }

}
