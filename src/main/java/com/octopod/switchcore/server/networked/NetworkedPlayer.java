package com.octopod.switchcore.server.networked;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.switchcore.PlayerSwitchResult;

/**
 * @author Octopod <octopodsquad@gmail.com>
 */
public interface NetworkedPlayer extends MinecraftPlayer
{
	public PlayerSwitchResult redirect(String server);
}
