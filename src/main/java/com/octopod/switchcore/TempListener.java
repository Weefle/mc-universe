package com.octopod.switchcore;

import com.octopod.switchcore.event.Event;
import com.octopod.switchcore.event.EventHandler;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class TempListener<T extends Event>
{
	private final Object lock = new Object();
	private final Object last = new Object();
	private int executionsLeft;
	private TempListenerFilter<T> filter;
	private Class<T> type;

	@SuppressWarnings("unchecked")
	public TempListener(Class<T> type, TempListenerFilter<T> filter)
	{
		this.type = type;
		this.filter = filter;
		this.executionsLeft = 1;
		register();
	}

	@SuppressWarnings("unchecked")
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
		if(executionsLeft > 0 && type.isInstance(event))
		{
			try
			{
				if(filter.onEvent(this, type.cast(event)))
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
		SwitchCore.getEventManager().registerListener(this);
	}

	private void unregister()
	{
		synchronized(last) {last.notify();}
		SwitchCore.getEventManager().unregisterListener(this);
	}

	/**
	 * Waits for the filter to get its executions.
	 * Use <code>timeout</code> (in ms) to timeout afterwards and return anyway.
	 *
	 * @param timeout the timeout, in ms
	 * @param unregister unregister on timeout?
	 * @return whether the listener succesfully did its executions within the timeout
	 */
	public boolean waitFor(long timeout, boolean unregister)
	{
		long start = System.currentTimeMillis();
		try
		{
			synchronized(last) {last.wait(timeout);}
		}
		catch (InterruptedException e) {}
		boolean success = (System.currentTimeMillis() - start) < timeout;
		if(!success && unregister) unregister();
		return success;
	}

	public boolean waitFor(long timeout)
	{
		return waitFor(timeout, true);
	}

	public void waitForAsync(final long timeout, final boolean unregister, final TempListenerFinish finish)
	{
		new Thread()
		{
			public void run()
			{
				finish.finish(waitFor(timeout, unregister));
			}
		}.start();
	}

	public void waitForAsync(long timeout, TempListenerFinish finish)
	{
		waitForAsync(timeout, true, finish);
	}

}
