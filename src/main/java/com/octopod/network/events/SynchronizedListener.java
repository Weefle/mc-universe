package com.octopod.network.events;

public abstract class SynchronizedListener <T extends Event> {

    private EventListener<T> listener;
	private final Object lock = new Object();
	private int executions = 0;

	public SynchronizedListener(EventListener<T> listener) {
		this.listener = listener;
	}

	abstract public void executionListener(T event);

	public void execute(T event) {
		try {
			if(listener.onEvent(event)) {
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

	public SynchronizedListener<T> waitFor(long timeout, int expectedExecutions) {
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

	public SynchronizedListener<T> waitFor(long timeout) {
		try {
			synchronized(lock) {
				lock.wait(timeout);
			}
		} catch (InterruptedException e) {}
		return this;
	}

}
