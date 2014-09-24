package com.octopod.networkplus;

import com.octopod.networkplus.event.Event;
import com.octopod.networkplus.event.EventHandler;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class TempListener<T extends Event>
{
	private final Object lock = new Object();
	private int executionsLeft;
	private TempListenerFilter<T> filter;
	private Class<T> type;

	public TempListener(Class<T> type, TempListenerFilter<T> filter)
	{
		this.type = type;
		this.filter = filter;
		this.executionsLeft = 1;
		register();
	}

	public TempListener(Class<T> type, TempListenerFilter<T> callback, int executions)
	{
		this.type = type;
		this.filter = callback;
		this.executionsLeft = executions;
		register();
	}

	@EventHandler
	public void processEvent(Event event)
	{
		if(type.isInstance(event))
		{
			try
			{
				if(filter.onEvent(this, type.cast(event)))
				{
					synchronized(lock)
					{
						executionsLeft--;
						lock.notify();
					}
				}
			}
			catch (Exception e) {e.printStackTrace();}
			if(executionsLeft == 0) unregister();
		}
	}

	private void register()
	{
		NetworkPlus.getLogger().i("A TempListener has been registered.");
		NetworkPlus.getEventManager().registerListener(this);
	}

	private void unregister()
	{
		NetworkPlus.getLogger().i("A TempListener has been unregistered.");
		NetworkPlus.getEventManager().unregisterListener(this);
	}

	/**
	 * Waits for the filter to get its executions.
	 * Use <code>timeout</code> (in ms) to timeout afterwards and return anyway.
	 * @param timeout the timeout, in ms
	 * @return whether the request timed out or not
	 */
	public boolean waitFor(long timeout)
	{
		long startTime = System.currentTimeMillis();
		try
		{
			//If the time elapsed is lower than the timeout and we still need more executionsLeft
			while((System.currentTimeMillis() - startTime) < timeout && executionsLeft > 0)
			{
				synchronized(lock) {lock.wait(timeout);}
			}
		}
		catch (InterruptedException e) {}

		return (System.currentTimeMillis() - startTime) < timeout;
	}

}
