package com.octopod.network.events.network;

import com.octopod.network.events.Event;

/**
 * @author Octopod
 *         Last updated on 2/26/14
 */
public class NetworkHubCacheEvent extends Event {

    private String server;
    private int priority;

    public NetworkHubCacheEvent(String server, int priority) {
        this.server = server;
        this.priority = priority;
    }

    public String getServer() {
        return server;
    }

    public int getPriority() {
        return priority;
    }

}
