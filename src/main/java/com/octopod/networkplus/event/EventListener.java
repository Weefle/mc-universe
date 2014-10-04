package com.octopod.networkplus.event;

import com.octopod.minecraft.MinecraftServerInterface;
import com.octopod.networkplus.NetworkPlus;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
class EventListener implements Comparable<EventListener> {
	private Object object;
	private Method method;
	private EventHandler annotation;

	public EventListener(Object object, Method method, EventHandler annotation) {
		this.object = object;
		this.method = method;
		this.annotation = annotation;
	}

	public int getPriorityLevel() {return annotation.priority().getPriority();}

	public Event callEvent(final Event event)
	{
		try {
			Runnable invokeMethod = new Runnable()
			{
				public void run()
				{
					try
					{
						method.invoke(object, event);
					}
					catch (Exception e)
					{
						MinecraftServerInterface server = NetworkPlus.getInterface();
						server.console("&cFailed to callEvent method " + method.getName() + "(" + Arrays.asList(method.getParameterTypes()) + ")");
						server.console("&cUsing arguments of types (" + event.getClass() + ")");
						e.printStackTrace();
					}
				}
			};

			if(annotation.async()) {
				new Thread(invokeMethod).start();
			} else {
				invokeMethod.run();
			}
		} catch (Exception e) {}
		return event;
	}

	public EventHandler handler() {return annotation;}

	@Override
	public int compareTo(EventListener other) {
		return Integer.compare(getPriorityLevel(), other.getPriorityLevel());
	}
}
