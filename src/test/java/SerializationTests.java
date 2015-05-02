import com.hyperfresh.mcuniverse.Server;
import com.hyperfresh.mcuniverse.ServerValue;
import com.hyperfresh.mcuniverse.packets.PacketInPlayerSwitch;
import com.hyperfresh.mcuniverse.packets.PacketOutServerDiscover;
import com.hyperfresh.mcuniverse.packets.PacketOutServerPing;
import com.hyperfresh.mcuniverse.packets.Packet;
import com.hyperfresh.mcuniverse.serializer.GsonSerializer;
import com.hyperfresh.mcuniverse.serializer.JavaSerializer;
import com.hyperfresh.mcuniverse.serializer.SwitchCorePacketSerializer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class SerializationTests
{
	@Test
	public void testGsonSerializer()
	{
		GsonSerializer serializer = new GsonSerializer();
		long time = System.currentTimeMillis();

		testPingPacketSerialization(serializer);
		testServerPacketSerialization(serializer);
		testSwitchPacketSerialization(serializer);

		System.out.println(serializer.getName() + " Serializer finished in " + (System.currentTimeMillis() - time) + " ms");
	}

	@Test
	public void testJavaSerializer()
	{
		JavaSerializer serializer = new JavaSerializer();
		long time = System.currentTimeMillis();

		testPingPacketSerialization(serializer);
		testServerPacketSerialization(serializer);
		testSwitchPacketSerialization(serializer);

		System.out.println(serializer.getName() + " Serializer finished in " + (System.currentTimeMillis() - time) + " ms");
	}

	public void testPingPacketSerialization(SwitchCorePacketSerializer serializer)
	{
		Packet packet;
		String encoded;

		encoded = serializer.serialize(new PacketOutServerPing());
		System.out.println("Encoded PacketOutServerPing: " + encoded);
		packet = serializer.deserialize(encoded);
		System.out.println("Decoded SwitchPacket: " + packet.getClass().getName());
	}

	public void testServerPacketSerialization(SwitchCorePacketSerializer serializer)
	{
		Packet packet;
		String encoded;

		encoded = serializer.serialize(new PacketOutServerDiscover(new FakeServer()));
		System.out.println("Encoded PacketOutServerDiscover: " + encoded);
		packet = serializer.deserialize(encoded);
		System.out.println("Decoded SwitchPacket: " + ((PacketOutServerDiscover)packet).getServer());
	}

	public void testSwitchPacketSerialization(SwitchCorePacketSerializer serializer)
	{
		Packet packet;
		String encoded;

		encoded = serializer.serialize(new PacketInPlayerSwitch());
		System.out.println("Encoded PacketInPlayerSwitch: " + encoded);
		packet = serializer.deserialize(encoded);
		System.out.println("Decoded PacketInPlayerSwitch: " + ((PacketInPlayerSwitch)packet).getResult().name());
	}

	private static class FakeServer implements Server
	{
		@Override
		public void setValue(ServerValue type, Object object)
		{

		}

		@Override
		public Object getValue(ServerValue type)
		{
			return null;
		}

		@Override
		public int totalValues()
		{
			return 0;
		}

		@Override
		public Map<ServerValue, Object> toValueMap()
		{
			return new HashMap<>();
		}

		@Override
		public boolean isExternal()
		{
			return true;
		}

		@Override
		public boolean isOnline()
		{
			return false;
		}

		@Override
		public String getServerIdentifier()
		{
			return "test";
		}

		@Override
		public String getServerName()
		{
			return "test";
		}

		@Override
		public String getServerVersion()
		{
			return null;
		}

		@Override
		public int getMaxPlayers()
		{
			return 0;
		}

		@Override
		public boolean getWhitelistEnabled()
		{
			return false;
		}

		@Override
		public String[] getWhitelistedPlayers()
		{
			return new String[0];
		}

		@Override
		public int getHubPriority()
		{
			return 0;
		}

		@Override
		public String[] getOnlinePlayers()
		{
			return new String[0];
		}

		@Override
		public String[] getQueuedPlayers()
		{
			return new String[0];
		}

		@Override
		public long getLastOnline()
		{
			return 0;
		}

		@Override
		public String toString()
		{
			return "[server: " + getServerIdentifier() + ", values: " + totalValues() + ", external: " + isExternal() + "]";
		}
	}
}
