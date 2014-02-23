package com.octopod.network.events;

public abstract class SynchronizedListener <T extends Event> {

	public interface EventCallback <T extends Event> {
		public boolean onEvent(T event);
	}
	
	private EventCallback<T> callback;
	private final Object lock = new Object();
	private int executions = 0;
	
	public SynchronizedListener(EventCallback<T> callback) {
		this.callback = callback;
	}

	abstract public void executionListener(T event);
	
	public void execute(T event) {
		try {
			if(callback.onEvent(event)) {
				synchronized(lock) {
					executions++;
					lock.notify();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public SynchronizedListener<T> register() {
		EventManager.getManager().registerListener(this);
		return this;
	}
	
	public SynchronizedListener<T> unregister() {
		EventManager.getManager().registerListener(this);
		return this;
	}
	
	public SynchronizedListener<T> waitFor(int expectedExecutions, long timeout) 
	{
		long startTime = System.currentTimeMillis();
		try {
			while(executions < expectedExecutions) {
				if((System.currentTimeMillis() - startTime) > timeout)
					break;
				synchronized(lock) {
					lock.wait(timeout);
				}
			}
		} catch (InterruptedException e) {}
		return this;
	}
	
	public SynchronizedListener<T> waitFor(long timeout) 
	{
		try {
			synchronized(lock) {
				lock.wait(timeout);
			}
		} catch (InterruptedException e) {}
		return this;
	}
	
}
