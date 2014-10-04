package com.octopod.networkplus.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventManager {

	private final List<Object> listeners = Collections.synchronizedList(new ArrayList<>());
	
	public List<Object> getListeners() {
		return listeners;
	}
	
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
        List<EventListener> listeners = findListeners(event);

		for(final EventListener eventMethod: listeners) eventMethod.callEvent(event);
    }

    private ArrayList<EventListener> findListeners(Event event)
	{
        synchronized(listeners)
		{
            ArrayList<EventListener> methods = new ArrayList<>();
            for(Object listener: listeners)
			{
                for(Method method: listener.getClass().getMethods())
				{
                    EventHandler annotation = method.getAnnotation(EventHandler.class);
                    if(annotation != null)
                    {
                        Class<?>[] argTypes = method.getParameterTypes();

                        if(argTypes.length != 1)
                            continue;

                        if(argTypes[0] != Event.class && !event.getClass().equals(argTypes[0])) {
							continue;
						}


                        methods.add(new EventListener(listener, method, annotation));
                    }
                }

            }
            Collections.sort(methods);
            return methods;
        }

    }
}
