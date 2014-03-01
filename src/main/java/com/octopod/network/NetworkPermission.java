package com.octopod.network;

import org.bukkit.command.CommandSender;

/**
 * @author Octopod
 * I don't want to put different permissions in different spots in the code, so just use an enum.
 */
public enum NetworkPermission {

    NETWORK_ALERT           ("network.alert"),
    NETWORK_FIND            ("network.find"),
    NETWORK_HELP            ("network.help"),
    NETWORK_MASTER          ("network.main"),
    NETWORK_MESSAGE         ("network.message"),
    NETWORK_HUB             ("network.hub"),
    NETWORK_RELOAD          ("network.reload"),
    NETWORK_SERVER_CONNECT  ("network.server.connect"),
    NETWORK_SERVER_LIST     ("network.server.list"),
    NETWORK_SERVER_PING     ("network.server.ping"),
    NETWORK_SERVER_SEND     ("network.server.send"),
    NETWORK_SERVER_SENDALL  ("network.server.sendall");

    private String node;
    private NetworkPermission(String node) {
        this.node = node;
    }

    public boolean senderHas(CommandSender sender) {
        return sender.hasPermission(node);
    }

}
