package com.octopod.network;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ServerInfo {
	
	public static String encodeThisServer() {
		List<String> players = NetworkPlugin.getPlayerNames();
		int maxPlayers = NetworkPlugin.self.getServer().getMaxPlayers();
		String motd = NetworkPlugin.self.getServer().getMotd();
		return StringUtils.join(players, ",") + " " + maxPlayers + " " + motd;
	}
	
	public static ServerInfo decode(String encoded, String server) {
		
		try {
			List<String> args = new LinkedList<String>(Arrays.asList(encoded.split(" ")));
			
			//Get players
			List<String> players = new LinkedList<String>(Arrays.asList(args.get(0).split(",")));
			if(players.get(0).equals("")) {players.clear();}
			
			//Get max players
			int maxPlayers = Integer.parseInt(args.get(1));
			
			//Get MOTD
			String motd = StringUtils.join(args.subList(2, args.size() - 1), " ");

			return new ServerInfo(server, players, maxPlayers, motd, -1);
		} catch (Exception e) {
			return null;	
		}
		
	}
	
	private String server;
	private List<String> players;
	private int maxPlayers;
	private String motd;
	
	private long ping;

	public ServerInfo(String server, List<String> players, int maxPlayers, String motd, long ping) {
		
		this.server = server;
		this.ping = ping;
		this.players = players;
		this.maxPlayers = maxPlayers;
		this.motd = motd;
		
	}

	public String getServerName() {
		return server;
	}
	
	public long getPing() {
		return ping;
	}
	
	public String getMotd() {
		return motd;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public List<String> getPlayers() {
		return players;
	}

}
