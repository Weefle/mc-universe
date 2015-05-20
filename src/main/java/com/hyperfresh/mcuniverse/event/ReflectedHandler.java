package com.hyperfresh.mcuniverse.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author octopod
 */
class ReflectedHandler<E extends Event> implements Handler<E>
{
	final Class<E> type;
	final Method method;
	final Object instance;

	public ReflectedHandler(Class<E> type, Object instance, Method method)
	{
		this.type = type;
		this.method = method;
		this.instance = instance;
		if(!method.isAnnotationPresent(EventHandler.class)) throw new IllegalArgumentException("This method does not have the EventSubscribe annotation");
		if(getEventType() != type) throw new IllegalArgumentException("The provided event type does not match the first argument of the provided method.");
	}

	@Override
	public void handle(E event)
	{
		try
		{
			method.invoke(instance, event);
		}
		catch (IllegalAccessException | InvocationTargetException e) {}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<E> getEventType()
	{
		return type;
	}

//	@Override
//	public int compareTo(ReflectedHandler other)
//	{
//		EventSubscribe h1 = this.method.getAnnotation(EventSubscribe.class);
//		EventSubscribe h2 = other.method.getAnnotation(EventSubscribe.class);
//		return h1.priority().compareTo(h2.priority());
//	}

	@Override
	public boolean equals(Object other)
	{
		return other != null && (other == this || other instanceof ReflectedHandler && ((ReflectedHandler)other).method == method && ((ReflectedHandler)other).instance == instance);
	}

}
