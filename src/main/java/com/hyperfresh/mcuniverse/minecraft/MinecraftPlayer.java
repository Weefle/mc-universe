package com.hyperfresh.mcuniverse.minecraft;

import com.octopod.minecraft.Location;
import com.octopod.util.Angle;
import com.octopod.util.Vector;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface MinecraftPlayer extends MinecraftUser, MinecraftCommandSource
{
	public Object getHandle();

	public void sendJSONMessage(String str);

	public Location getLocation();

	public Vector getPosition();

	public Angle getAngle();

	public MinecraftWorld getWorld();

	public Location getCursor();

	public Vector forward();

	public Vector forward(double offsetYaw, double offsetPitch);

	/**
	 * 2.8 units above player location
	 *
	 * @return
	 */
	public Vector head();

	/**
	 * 2.62 units above player location
	 *
	 * @return
	 */
	public Vector eyes();

	public int getEmptyHotbarSlot();

	public int getSelectedSlot();

	public void setSelectedSlot(int slot);

	public void setExpBar(float shield);

	public void setExpLevel(int level);

	public void setMaxHealth(int health);

	public void setHealth(double health);

	public double getMaxHealth();

	public double getHealth();

	public void setWalkSpeed(float speed);

	public void setHunger(int hunger);

	public void setCanFly(boolean fly);

	public void setFly(boolean fly);

	public void setOp(boolean op);

	public boolean isOp();

	public void giveItem(int slot, int type, int data, String name, List<String> description);

	public void renameItem(int slot, String name);

	public void playEffectBlock(Vector pos, String block);

	public void playEffectHurt();
}
