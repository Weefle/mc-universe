package com.hyperfresh.mcuniverse.event;

import java.lang.reflect.Method;

/**
 *
 *
 * @author Octopod <octopodsquad@gmail.com>
 */
public abstract class AsyncEventHandler<T extends Event>
{
	private final Object lock = new Object();
	private final Object last = new Object();
	private int executionsLeft;
	private EventBus bus;

	public AsyncEventHandler()
	{
		this(com.hyperfresh.mcuniverse.UniverseAPI.getEventBus(), 1);
	}

	public AsyncEventHandler(int executions)
	{
		this(com.hyperfresh.mcuniverse.UniverseAPI.getEventBus(), executions);
	}

	public AsyncEventHandler(EventBus bus)
	{
		this(bus, 1);
	}

	public AsyncEventHandler(EventBus bus, int executions)
	{
		this.bus = bus;
		this.executionsLeft = executions;
		register();
	}

	public abstract boolean handle(T event);

	public void finish(boolean success) {}

	@SuppressWarnings("unchecked")
	public final Class<T> type()
	{
		for(Method method: this.getClass().getDeclaredMethods())
		{
			if(method.getName().equals("handle")) return (Class<T>)method.getParameterTypes()[0];
		}
		return null;
	}

	@EventSubscribe
	public final void process(Event event)
	{
		Class<T> type = type();
		if(executionsLeft > 0 && type.isInstance(event))
		{
			try
			{
				if(handle(type.cast(event)))
				{
					synchronized(lock)
					{
						if(--executionsLeft == 0) unregister();
						lock.notify();
					}
				}
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}

	private void register()
	{
		bus.register(this);
	}

	private void unregister()
	{
		synchronized(last) {last.notify();}
		bus.unregister(this);
	}

	/**
	 * Waits for the filter to get its executions.
	 * Use <code>timeout</code> (in ms) to timeout afterwards and return anyway.
	 *
	 * @param timeout the timeout, in ms
	 * @param unregister unregister on timeout?
	 * @return whether the listener succesfully did its executions within the timeout
	 */
	public final boolean waitFor(long timeout, boolean unregister)
	{
		long start = System.currentTimeMillis();
		try
		{
			synchronized(lock) {lock.wait(timeout);}
		}
		catch (InterruptedException e) {}
		System.out.println("timeout= " + timeout + "\ntime elapsed= " + (System.currentTimeMillis() - start));
		boolean success = (System.currentTimeMillis() - start) < timeout;
		if(!success && unregister) unregister();
		AsyncEventHandler.this.finish(success);
		return success;
	}

	public final boolean waitFor(long timeout)
	{
		return waitFor(timeout, true);
	}

	public final void awaitFor(final long timeout, final boolean unregister)
	{
		new Thread()
		{
			public void run()
			{
				waitFor(timeout, unregister);
			}
		}.start();
	}

	public final void awaitFor(long timeout)
	{
		awaitFor(timeout, true);
	}

	public interface Finish
	{
		/**
		 * An implementation for awaitFor() that will run after the wait.
		 * @param successful false if awaitFor() timed out
		 */
		public void finish(boolean successful);
	}

}
