package com.octopod.switchcore.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
* @author Octopod <octopodsquad@gmail.com>
*/
class EventHandler implements Comparable<EventHandler>
{
	Method method;
	Object instance;

	EventHandler(Method method, Object instance)
	{
		if(!method.isAnnotationPresent(EventSubscribe.class)) throw new IllegalArgumentException("This method does not have the EventSubscribe annotation");
		this.method = method;
		this.instance = instance;
	}

	public void invoke(Event event)
	{
		try
		{
			method.invoke(instance, event);
		}
		catch (IllegalAccessException | InvocationTargetException e) {}
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Event> getEventType()
	{
		return (Class<? extends Event>)method.getParameterTypes()[0];
	}

	@Override
	public int compareTo(EventHandler other)
	{
		EventSubscribe h1 = this.method.getAnnotation(EventSubscribe.class);
		EventSubscribe h2 = other.method.getAnnotation(EventSubscribe.class);
		return h1.priority().compareTo(h2.priority());
	}

	@Override
	public boolean equals(Object other)
	{
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof EventHandler)) return false;

		return ((EventHandler)other).method == method && ((EventHandler)other).instance == instance;
	}

	@SuppressWarnings("unchecked")
	public static boolean isEventHandler(Method method)
	{
		return
			method.getReturnType().equals(Void.TYPE) && //Method returns VOID
				method.getParameterTypes().length == 1 && //Method has one argument
				Event.class.isAssignableFrom(method.getParameterTypes()[0]) && //first argument is event
				method.isAnnotationPresent(EventSubscribe.class); //Method has annotation
	}
}
