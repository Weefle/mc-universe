package com.hyperfresh.mcuniverse.server.networked;

import com.hyperfresh.mcuniverse.PlayerSwitchResult;
import com.hyperfresh.mcuniverse.minecraft.MinecraftPlayer;

/**
 * @author Octopod <octopodsquad@gmail.com>
 */
public interface UniversePlayer extends MinecraftPlayer
{
	public PlayerSwitchResult redirect(String server);
}
