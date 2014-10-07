package com.octopod.switchcore.event;

import com.octopod.minecraft.MinecraftServerInterface;
import com.octopod.switchcore.SwitchCore;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
class EventMethod implements Comparable<EventMethod>
{
	private Object instance;
	private Method method;
	private EventHandler handler;

	public EventMethod(Object object, Method method, EventHandler annotation) {
		this.instance = object;
		this.method = method;
		this.handler = annotation;
	}

	public Object getInstance() {return instance;}

	public int getPriorityLevel() {return handler.priority().getPriority();}

	public Event callEvent(final Event event)
	{
		try {
			Runnable invokeMethod = new Runnable()
			{
				public void run()
				{
					try
					{
						method.invoke(instance, event);
					}
					catch (Exception e)
					{
						MinecraftServerInterface server = SwitchCore.getInterface();
						server.console("&cFailed to callEvent method " + method.getName() + "(" + Arrays.asList(method.getParameterTypes()) + ")");
						server.console("&cUsing arguments of types (" + event.getClass() + ")");
						e.printStackTrace();
					}
				}
			};

			if(handler.async()) {
				new Thread(invokeMethod).start();
			} else {
				invokeMethod.run();
			}
		}
		catch (Exception e) {}
		return event;
	}

	public EventHandler handler() {return handler;}

	@Override
	public int compareTo(EventMethod other) {
		return Integer.compare(getPriorityLevel(), other.getPriorityLevel());
	}
}
