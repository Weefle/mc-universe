package com.octopod.network;

import com.octopod.network.util.BukkitUtils;

/**
 * @author Octopod
 *         Created on 3/13/14
 */
public class PreparedPlayerMessage {

    String player, message;

    public PreparedPlayerMessage(String player, String message) {
        this.player = player;
        this.message = message;
    }

    public void send() {
        BukkitUtils.sendMessage(player, message);
    }

}
