package com.hyperfresh.mcuniverse.server.networked.local;

import com.hyperfresh.mcuniverse.PlayerSwitchResult;
import com.hyperfresh.mcuniverse.event.AsyncEventHandler;
import com.hyperfresh.mcuniverse.event.events.NetworkPacketInEvent;
import com.hyperfresh.mcuniverse.minecraft.MinecraftPlayer;
import com.hyperfresh.mcuniverse.minecraft.MinecraftWorld;
import com.hyperfresh.mcuniverse.packets.Packet;
import com.hyperfresh.mcuniverse.packets.PacketInPlayerSwitch;
import com.hyperfresh.mcuniverse.packets.PacketOutPlayerSwitch;
import com.hyperfresh.mcuniverse.server.networked.UniversePlayer;
import com.octopod.minecraft.Location;
import com.octopod.util.Angle;
import com.octopod.util.Vector;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An implementation for a player that is "networked", as in
 * connected to a server that is cached on this server.
 *
 * All methods in this implementation are expected to be slower than their more "direct" counterparts,
 * especially the ones that return something, as they wait for packet returns.
 *
 * @author Octopod <octopodsquad@gmail.com>
 */
public class LocalPlayer implements UniversePlayer
{
	MinecraftPlayer player;

	public LocalPlayer(MinecraftPlayer player)
	{
		this.player = player;
	}

	@Override
	public PlayerSwitchResult redirect(String server)
	{
		final AtomicReference<PlayerSwitchResult> result = new AtomicReference<>();
		final Packet message = new PacketInPlayerSwitch(player);

		AsyncEventHandler<NetworkPacketInEvent> listener = new AsyncEventHandler<NetworkPacketInEvent>()
		{
			public boolean handle(NetworkPacketInEvent event)
			{
				if(event.getPacket() instanceof PacketOutPlayerSwitch)
				{
					PacketOutPlayerSwitch packet = (PacketOutPlayerSwitch)event.getPacket();
					if(player.getUUID().equals(packet.getUUID()))
					{
						result.set(packet.getResult());
					}
					return true;
				}
				return false;
			}
		};

		message.send(server);
		listener.waitFor(500);

		return result.get();
	}

	@Override
	public Object getHandle()
	{
		return player;
	}

	@Override
	public Location getLocation()
	{
		return player.getLocation();
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
		return player.getWorld();
	}

	@Override
	public Location getCursor()
	{
		return player.getCursor();
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
		return player.getSelectedSlot();
	}

	@Override
	public void setSelectedSlot(int i)
	{
		player.setSelectedSlot(i);
	}

	@Override
	public void setExpBar(float v)
	{
		player.setExpBar(v);
	}

	@Override
	public void setExpLevel(int i)
	{
		player.setExpLevel(i);
	}

	@Override
	public void setMaxHealth(int v)
	{
		player.setMaxHealth(v);
	}

	@Override
	public void setHealth(double v)
	{
		player.setHealth(v);
	}

	@Override
	public double getMaxHealth()
	{
		return player.getMaxHealth();
	}

	@Override
	public double getHealth()
	{
		return player.getHealth();
	}

	@Override
	public void setWalkSpeed(float v)
	{
		player.setWalkSpeed(v);
	}

	@Override
	public void setHunger(int i)
	{
		player.setHunger(i);
	}

	@Override
	public void setCanFly(boolean b)
	{
		player.setCanFly(b);
	}

	@Override
	public void setFly(boolean b)
	{
		player.setFly(b);
	}

//	@Override
//	public <T> void playEffect(Location location, MinecraftEffect<T> effect, T t)
//	{
//		player.playEffect(location, effect, t);
//	}
//
//	@Override
//	public void playHurtEffect()
//	{
//		player.playHurtEffect();
//	}

	@Override
	public String getName()
	{
		return player.getName();
	}

	@Override
	public void sendMessage(String s)
	{
		player.sendMessage(s);
	}

	@Override
	public void dispatchCommand(String s)
	{
		player.dispatchCommand(s);
	}

	@Override
	public boolean hasPermission(String s)
	{
		return player.hasPermission(s);
	}

	@Override
	public void sendJSONMessage(String s)
	{
		player.sendJSONMessage(s);
	}

	@Override
	public String getUUID()
	{
		return player.getUUID();
	}

	@Override
	public void setWhitelisted(boolean b)
	{
		player.setWhitelisted(b);
	}

	@Override
	public boolean isWhitelisted()
	{
		return player.isWhitelisted();
	}

	@Override
	public void setBanned(boolean b)
	{
		player.setBanned(b);
	}

	@Override
	public boolean isBanned()
	{
		return player.isBanned();
	}

	@Override
	public void setOp(boolean b)
	{
		player.setOp(b);
	}

	@Override
	public boolean isOp()
	{
		return player.isOp();
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
		return this;
	}

}
