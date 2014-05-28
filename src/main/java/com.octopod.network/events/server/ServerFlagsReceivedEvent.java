package com.octopod.network.events.server;

import com.octopod.network.ServerFlags;
import com.octopod.network.events.CancellableEvent;

/**
 * @author Octopod
 *         Created on 3/26/14
 */
public class ServerFlagsReceivedEvent extends CancellableEvent {

    String server;
    ServerFlags flags;

    public ServerFlagsReceivedEvent(String serverID, ServerFlags flags) {
        server = serverID;
        this.flags = flags;
    }

    public String getServer() {
        return server;
    }

    public ServerFlags getFlags() {
        return flags;
    }

    public void setFlags(ServerFlags flags) {
        this.flags = flags;
    }

}
