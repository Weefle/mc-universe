package com.octopod.network;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ServerInfo {

	public static ServerInfo decode(String encoded, String server) {

		try {
			List<String> args = new LinkedList<String>(Arrays.asList(encoded.split(" ")));

			//Get max players
			int maxPlayers = Integer.parseInt(args.get(0));

			//Get MOTD
			String motd = StringUtils.join(args.subList(1, args.size() - 1), " ");

			return new ServerInfo(server, maxPlayers, motd);
		} catch (Exception e) {
			return null;
		}

	}

	private String server;
	private int maxPlayers;
	private String motd;

	public ServerInfo(String server, int maxPlayers, String motd) {

		this.server = server;
		this.maxPlayers = maxPlayers;
		this.motd = motd;

	}

	public String getServerName() {
		return server;
	}

	public String getMotd() {
		return motd;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

}
