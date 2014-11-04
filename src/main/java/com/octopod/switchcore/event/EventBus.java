package com.octopod.switchcore.event;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class EventBus
{
	private final Map<Class<? extends Event>, SortedSet<EventHandler>> handlerMap = new HashMap<>();

	public void unregisterAll()
	{
		handlerMap.clear();
	}

	public void unregisterAll(Class<? extends Event> event)
	{
		if(handlerMap.containsKey(event)) handlerMap.get(event).clear();
	}

	public int register(Object object)
	{
		int count = 0;
		for(EventHandler handler: findEventHandlers(object))
		{
			Class<? extends Event> event = handler.getEventType();
			if(!handlerMap.containsKey(event)) handlerMap.put(event, new TreeSet<EventHandler>());
			handlerMap.get(event).add(handler);
			count++;
		}
		return count;
	}

	public int unregister(Object object)
	{
		int count = 0;
		for(EventHandler handler: findEventHandlers(object))
		{
			Class<? extends Event> event = handler.getEventType();
			if(handlerMap.containsKey(event)) handlerMap.get(event).remove(handler);
			count++;
		}
		return count;
	}

	public boolean registered(Object object)
	{
		for(EventHandler handler: findEventHandlers(object))
		{
			Class<? extends Event> event = handler.getEventType();
			if(handlerMap.containsKey(event)) if(handlerMap.get(event).contains(handler)) return true;
		}
		return false;
	}

    public synchronized void post(final Event event)
	{
		if(handlerMap.containsKey(event.getClass()) || handlerMap.containsKey(Event.class))
		{
			SortedSet<EventHandler> handlers;
			if(handlerMap.containsKey(event.getClass()))
				handlers = new TreeSet<>(handlerMap.get(event.getClass()));
			else
				handlers = new TreeSet<>();

			if(handlerMap.containsKey(Event.class))
				handlers.addAll(handlerMap.get(Event.class));

			for(EventHandler handler: handlers)
			{
				handler.invoke(event);
			}
		}
    }

	private static Set<EventHandler> findEventHandlers(Object object)
	{
		Set<EventHandler> handlers = new HashSet<>();
		for(Method method: object.getClass().getMethods())
		{
			if(EventHandler.isEventHandler(method)) handlers.add(new EventHandler(method, object));
		}
		return handlers;
	}
}
