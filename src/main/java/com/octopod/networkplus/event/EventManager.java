package com.octopod.networkplus.event;

import java.lang.reflect.Method;
import java.util.*;

public class EventManager {

	private final List<Object> listeners = Collections.synchronizedList(new ArrayList<>());

	public synchronized void registerListener(Object listener)
	{
		listeners.add(listener);
	}
	
	public synchronized void unregisterListener(Object listener)
	{
		listeners.remove(listener);
	}
	
	public void unregisterAll()
	{
		listeners.clear();
	}

    public synchronized void callEvent(final Event event)
	{
		for(final EventMethod eventMethod: findListeners(event))
		{
			eventMethod.callEvent(event);
		}
    }

	private ArrayList<EventMethod> findListeners(Event event)
	{
		synchronized(listeners)
		{
			ArrayList<EventMethod> methods = new ArrayList<>();
			for(Object listener: listeners)
			{
				for(Method method: listener.getClass().getMethods())
				{
					if(method.isAnnotationPresent(EventHandler.class))
					{
						Class<?>[] argTypes = method.getParameterTypes();

						if(argTypes.length != 1)
							continue;

						if(argTypes[0] != Event.class && !event.getClass().equals(argTypes[0]))
							continue;

						methods.add(new EventMethod(listener, method, method.getAnnotation(EventHandler.class)));
					}
				}
			}
			Collections.sort(methods);
			return methods;
		}
	}
}
