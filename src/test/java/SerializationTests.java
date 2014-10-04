import com.octopod.networkplus.serializer.GsonSerializer;
import com.octopod.networkplus.serializer.JavaSerializer;
import com.octopod.networkplus.serializer.NetworkPlusSerializer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class SerializationTests
{
	private enum TestEnum
	{
		RED, BLUE, YELLOW, GREEN
	}

	@Test
	public void testGsonSerializer()
	{
		testEnumSerializationFor(new GsonSerializer());
	}

	@Test
	public void testJavaSerializer()
	{
		testEnumSerializationFor(new JavaSerializer());
	}

	@SuppressWarnings("unchecked")
	public void testEnumSerializationFor(NetworkPlusSerializer serializer)
	{
		System.out.println("TESTING SERIALIZATION FOR: \"" + serializer.getName() + "\"");

		Map<TestEnum, Object> map = new HashMap<>();
		map.put(TestEnum.RED, "red");
		map.put(TestEnum.BLUE, 1);
		map.put(TestEnum.YELLOW, new String[]{"A", "B", "C"});
		map.put(TestEnum.GREEN, true);

		String encoded = serializer.serialize(map);
		System.out.println("Encoded TestEnum Map: " + encoded);
		map = serializer.deserializeMap(encoded, TestEnum.class, Object.class);
		System.out.println("Decoded TestEnum Map: " + map);
		for(TestEnum type: map.keySet()) //ClassCastException here for GsonSerializer
		{
			System.out.println(type + ": " + map.get(type));
		}
	}
}
