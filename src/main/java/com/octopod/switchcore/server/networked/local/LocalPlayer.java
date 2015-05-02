package com.octopod.switchcore.server.networked.local;

import com.octopod.minecraft.Location;
import com.octopod.minecraft.MinecraftEffect;
import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.minecraft.MinecraftWorld;
import com.octopod.switchcore.PlayerSwitchResult;
import com.octopod.switchcore.event.AsyncEventHandler;
import com.octopod.switchcore.event.events.NetworkPacketInEvent;
import com.octopod.switchcore.packets.SwitchPacket;
import com.octopod.switchcore.packets.PacketInPlayerSwitch;
import com.octopod.switchcore.packets.PacketOutPlayerSwitch;
import com.octopod.switchcore.server.networked.NetworkedPlayer;

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
public class LocalPlayer implements NetworkedPlayer
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
		final SwitchPacket message = new PacketInPlayerSwitch(player);

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
	public int getHotbarSelection()
	{
		return player.getHotbarSelection();
	}

	@Override
	public void setHotbarSelection(int i)
	{
		player.setHotbarSelection(i);
	}

	@Override
	public void setExpBar(double v)
	{
		player.setExpBar(v);
	}

	@Override
	public void setExpLevel(int i)
	{
		player.setExpLevel(i);
	}

	@Override
	public void setMaxHealth(double v)
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
	public void setWalkSpeed(double v)
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

	@Override
	public <T> void playEffect(Location location, MinecraftEffect<T> effect, T t)
	{
		player.playEffect(location, effect, t);
	}

	@Override
	public void playHurtEffect()
	{
		player.playHurtEffect();
	}

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
	public MinecraftPlayer getPlayer()
	{
		return this;
	}

}
