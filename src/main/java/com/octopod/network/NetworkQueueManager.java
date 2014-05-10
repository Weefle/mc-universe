package com.octopod.network;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;

import com.octopod.network.bukkit.BukkitUtils;

public class NetworkQueueManager {

	/**
	 * This class handles the queue system
	 */

	/**
	 * The current instance of the NetworkQueueManager
	 */
	public static NetworkQueueManager instance = new NetworkQueueManager();

	/**
	 * The current instance of this servers queue
	 */
	public ArrayList<String> queue;

	/**
	 * Holds the number of VIP members in the queue
	 */
	private int vipInQueue;

	public NetworkQueueManager() {
		queue = new ArrayList<>();
	}

	/**
	 * Add Player to queue
	 * 
	 * @param player
	 * @param queuePosition
	 *            0 if the end of list, else if they're being added to a
	 *            different position.
	 */
	public void add(String player, int queuePosition) {
		if (queuePosition == 0) {
			queue.add(player);
			alertPlayers(getQueuePosition(player));
		} else {
			// Create temp queue then add every player to the new list.
			ArrayList<String> tempQueue = new ArrayList<>();
			for (String p : queue) {
				tempQueue.add(p);
				if (queue.indexOf(p) == queuePosition - 1) {
					tempQueue.add(player);
				}
			}
			vipInQueue++;
			queue = tempQueue;
			alertPlayers(getQueuePosition(player));
		}
	}

	/**
	 * Remove player from queue
	 * 
	 * @param player
	 */
	public void remove(String player) {
		int queuePos = getQueuePosition(player);
		queue.remove(player);
		alertPlayers(queuePos);

		String channel_queueleave = NetworkConfig.Channels.PLAYER_LEAVE_QUEUE.toString();
		NetworkPlus.broadcastMessage(channel_queueleave, new ServerMessage(player));
	}

	/**
	 * Checks players online and updates the queue
	 */
	public void updateQueue() {
		int serverSize = NetworkPlus.getServerInfo().getOnlinePlayers().size();
		int maxServerSize = NetworkPlus.getServerInfo().getMaxPlayers();
		String toJoin = getQueueMembers().get(0);
		if (serverSize < maxServerSize) {
			NetworkPlus.sendPlayer(toJoin, NetworkPlus.getServerID());
			remove(toJoin);
		}
	}

	/**
	 * Gets total Queue Members
	 * 
	 * @return
	 */
	public ArrayList<String> getQueueMembers() {
		return queue;
	}

	/**
	 * Gets total VIP members in the queue
	 * 
	 * @return
	 */
	public int getVIPQueueMembers() {
		return vipInQueue;
	}

	/**
	 * Get the players position in the queue
	 * 
	 * @param player
	 * @return
	 */
	public int getQueuePosition(String player) {
		return queue.indexOf(player) + 1;
	}

	public boolean isQueued(String player) {
		if (queue.contains(player)) {
			return true;
		}
		return false;
	}

	/**
	 * Alert players of their new queue position
	 * 
	 * @param fromQueuePos
	 */
	@SuppressWarnings("deprecation")
	public void alertPlayers(int fromQueuePos) {
		for (String player : queue) {
			OfflinePlayer p = Bukkit.getOfflinePlayer(player);
			if (getQueuePosition(p.getName()) >= fromQueuePos) {
				if (p.isOnline()) {
					BukkitUtils.sendMessage(p.getName(),
							"&cThere are currently &e"
									+ NetworkPlus.getServerInfo().getOnlinePlayers()
									+ "/"
									+ NetworkPlus.getServerInfo()
											.getMaxPlayers()
									+ "&c in "
									+ NetworkPlus.getServerInfo()
											.getServerName());
					BukkitUtils.sendMessage(p.getName(),
							"&cYour new queue position is: "
									+ getQueuePosition(p.getName()));
					p.getPlayer().playSound(p.getPlayer().getLocation(),
							Sound.NOTE_PIANO, 1, 1);
				}
			}
		}
	}
}
