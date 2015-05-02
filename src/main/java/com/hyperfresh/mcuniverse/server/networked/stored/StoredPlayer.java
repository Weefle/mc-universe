package com.hyperfresh.mcuniverse.server.networked.stored;

import com.hyperfresh.mcuniverse.PlayerSwitchResult;
import com.hyperfresh.mcuniverse.minecraft.MinecraftPlayer;
import com.hyperfresh.mcuniverse.minecraft.MinecraftWorld;
import com.hyperfresh.mcuniverse.server.networked.UniversePlayer;
import com.octopod.minecraft.Location;
import com.octopod.util.Angle;
import com.octopod.util.Vector;

import java.util.List;

/**
 * @author Octopod <octopodsquad@gmail.com>
 */
public class StoredPlayer implements UniversePlayer
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
	public Vector getPosition()
	{
		return null;
	}

	@Override
	public Angle getAngle()
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
	public Vector forward()
	{
		return null;
	}

	@Override
	public Vector forward(double offsetYaw, double offsetPitch)
	{
		return null;
	}

	@Override
	public Vector head()
	{
		return null;
	}

	@Override
	public Vector eyes()
	{
		return null;
	}

	@Override
	public int getEmptyHotbarSlot()
	{
		return 0;
	}

	@Override
	public int getSelectedSlot()
	{
		return 0;
	}

	@Override
	public void setSelectedSlot(int slot)
	{

	}

	@Override
	public void setExpBar(float shield)
	{

	}

	@Override
	public void setExpLevel(int i)
	{

	}

	@Override
	public void setMaxHealth(int health)
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
	public void setWalkSpeed(float speed)
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

//	@Override
//	public <T> void playEffect(Location location, MinecraftEffect<T> minecraftEffect, T t)
//	{
//
//	}
//
//	@Override
//	public void playHurtEffect()
//	{
//
//	}

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
	public void giveItem(int slot, int type, int data, String name, List<String> description)
	{

	}

	@Override
	public void renameItem(int slot, String name)
	{

	}

	@Override
	public void playEffectBlock(Vector pos, String block)
	{

	}

	@Override
	public void playEffectHurt()
	{

	}

	@Override
	public MinecraftPlayer getPlayer()
	{
		return null;
	}
}
