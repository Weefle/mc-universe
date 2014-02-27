package com.octopod.network.events.server;

import com.octopod.network.events.Event;

/**
 * @author Octopod
 * Last updated on 2/26/14
 */
public class ServerPlayerListEvent extends Event {

    private String[] players;
    private String server;

    public ServerPlayerListEvent(String server, String encodedList) {
        this.server = server;
        if(encodedList.equals("")) {
            this.players = new String[0];
        } else {
            this.players = encodedList.split(",");
        }
    }

    public String getServer() {
        return server;
    }

    public String[] getPlayers() {
        return players;
    }

}
