package com.octopod.networkplus.event;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class Listener<T extends Event>
{
	private final Object lock = new Object();
	private int executions = 0;

	@EventHandler
	public void processEvent(T event)
	{
		try
		{
			onEvent(event);
			if(event.isUnlocked())
			{
				event.setUnlocked(false);
				synchronized(lock)
				{
					executions++;
					lock.notify();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public abstract void onEvent(T event);

	/**
	 * Waits for the above listener to be fired X times, then returns.
	 * Use timeout to timeout after X ms and return anyway.
	 * @param timeout
	 * @param expectedExecutions
	 * @return
	 */
	public ListenerResult waitFor(long timeout, int expectedExecutions)
	{
		long startTime = System.currentTimeMillis();
		executions = 0;
		try {
			while(executions < expectedExecutions)
			{
				if((System.currentTimeMillis() - startTime) > timeout) break;
				synchronized(lock)
				{
					lock.wait(timeout);
				}
			}
		} catch (InterruptedException e) {}
		if((System.currentTimeMillis() - startTime) >= timeout)
		{
			//Event timed out, return FAIL
			return ListenerResult.FAIL;
		}
		else
		{
			return ListenerResult.PASS;
		}
	}

	public ListenerResult waitFor(long timeout)
	{
		return waitFor(timeout, 1);
	}
}
