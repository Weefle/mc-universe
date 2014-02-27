package com.octopod.network;

import com.octopod.octolib.common.StringUtils;

import java.util.List;

public class ServerInfo {

	public static ServerInfo decode(String encoded, String server) {

        String serverName = server;
        int maxPlayers = -1;
        String motd = "";

		try {
			List<String> args = StringUtils.parseArgs(encoded);

            serverName = args.get(0);

			//Get max players
			maxPlayers = Integer.parseInt(args.get(1));

			//Get MOTD
			motd = args.get(2);
		} catch (Exception e) {}

        return new ServerInfo(server, serverName, maxPlayers, motd);

	}

	private String server;
    private String serverName;
	private int maxPlayers;
	private String motd;

	public ServerInfo(String server, String serverName, int maxPlayers, String motd) {

		this.server = server;
        this.serverName = serverName;
		this.maxPlayers = maxPlayers;
		this.motd = motd;

	}

	public String getServerName() {
		return serverName;
	}

    public String getUsername() {
        return server;
    }

	public String getMotd() {
		return motd;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

}
