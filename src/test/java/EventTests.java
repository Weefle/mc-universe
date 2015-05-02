import com.octopod.switchcore.event.AsyncEventHandler;
import com.octopod.switchcore.event.Event;
import com.octopod.switchcore.event.EventBus;
import org.junit.Test;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class EventTests
{
	public static class TestEvent extends Event {}

	public static class TestEventHandler extends AsyncEventHandler<TestEvent>
	{
		public TestEventHandler(EventBus eventBus)
		{
			super(eventBus);
		}

		@Override
		public boolean handle(TestEvent event)
		{
			System.out.println("TestEvent found.");
			return true;
		}

		@Override
		public void finish(boolean success)
		{
			System.out.println("Success: " + success);
		}
	}

	@Test
	public void testAbstractEventHandler()
	{
		final EventBus eventBus = new EventBus();

		TestEventHandler handler = new TestEventHandler(eventBus);

		eventBus.post(new TestEvent());
	}
}
