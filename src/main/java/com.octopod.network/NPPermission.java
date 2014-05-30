package com.octopod.network;

import org.bukkit.command.CommandSender;

/**
 * @author Octopod
 * I don't want to put different permissions in different spots in the code, so just use an enum.
 */
public enum NPPermission {

    NETWORK_ALERT           ("network.alert"),
    NETWORK_SCAN            ("network.scan"),
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
    NETWORK_SERVER_SENDALL  ("network.server.sendall"),
    NETWORK_QUEUE_BYPASS    ("network.queue.bypass"),

	NETWORK_LOGGER_INFO		("network.log.info"),
	NETWORK_LOGGER_WARNING	("network.log.warning"),
	NETWORK_LOGGER_DEBUG	("network.log.debug"),
	NETWORK_LOGGER_VERBOSE	("network.log.verbose");

    private String node;
    private NPPermission(String node) {
        this.node = node;
    }

    public boolean senderHas(CommandSender sender) {
        return sender.hasPermission(node);
    }

}
