package com.octopod.switchcore.server.networked.stored;

import com.octopod.minecraft.Location;
import com.octopod.minecraft.MinecraftEffect;
import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.minecraft.MinecraftWorld;
import com.octopod.switchcore.PlayerSwitchResult;
import com.octopod.switchcore.server.networked.NetworkedPlayer;

/**
 * @author Octopod <octopodsquad@gmail.com>
 */
public class StoredPlayer implements NetworkedPlayer
{
	StoredServer server;
	String UUID;

	public StoredPlayer(StoredServer server, String UUID)
	{
		this.server = server;
		this.UUID = UUID;
	}

	@Override
	public PlayerSwitchResult redirect(String server)
	{
		return null;
	}

	@Override
	public Object getHandle()
	{
		return null;
	}

	@Override
	public Location getLocation()
	{
		return null;
	}

	@Override
	public MinecraftWorld getWorld()
	{
		return null;
	}

	@Override
	public Location getCursor()
	{
		return null;
	}

	@Override
	public int getHotbarSelection()
	{
		return 0;
	}

	@Override
	public void setHotbarSelection(int i)
	{

	}

	@Override
	public void setExpBar(double v)
	{

	}

	@Override
	public void setExpLevel(int i)
	{

	}

	@Override
	public void setMaxHealth(double v)
	{

	}

	@Override
	public void setHealth(double v)
	{

	}

	@Override
	public double getMaxHealth()
	{
		return 0;
	}

	@Override
	public double getHealth()
	{
		return 0;
	}

	@Override
	public void setWalkSpeed(double v)
	{

	}

	@Override
	public void setHunger(int i)
	{

	}

	@Override
	public void setCanFly(boolean b)
	{

	}

	@Override
	public void setFly(boolean b)
	{

	}

	@Override
	public <T> void playEffect(Location location, MinecraftEffect<T> minecraftEffect, T t)
	{

	}

	@Override
	public void playHurtEffect()
	{

	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public void sendMessage(String s)
	{

	}

	@Override
	public void dispatchCommand(String s)
	{

	}

	@Override
	public boolean hasPermission(String s)
	{
		return false;
	}

	@Override
	public void sendJSONMessage(String s)
	{

	}

	@Override
	public String getUUID()
	{
		return null;
	}

	@Override
	public void setWhitelisted(boolean b)
	{

	}

	@Override
	public boolean isWhitelisted()
	{
		return false;
	}

	@Override
	public void setBanned(boolean b)
	{

	}

	@Override
	public boolean isBanned()
	{
		return false;
	}

	@Override
	public void setOp(boolean b)
	{

	}

	@Override
	public boolean isOp()
	{
		return false;
	}

	@Override
	public MinecraftPlayer getPlayer()
	{
		return null;
	}
}
