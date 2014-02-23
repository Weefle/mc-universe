package com.octopod.network.events;

import java.lang.reflect.Method;
import java.util.List;

import com.octopod.network.Debug;

public class EventEmitter {
	
	private static EventEmitter emitter = null;
	
	public static EventEmitter getEmitter() {
		if(emitter != null) {
			return emitter;
		} else {
			emitter = new EventEmitter();
			return emitter;
		}
	}
	
	public long totalTriggers = 0;

	public void triggerEvent(final Event event) {
		new Thread(event.getClass().getSimpleName() + "-" + totalTriggers) {
			@Override
			public void run() {
				trigger(event);
			}
		}.start();
	}
	
	private synchronized void trigger(final Event event) {
		
		Debug.verbose("Triggering event: &a" + event.getClass().getSimpleName());

		List<Object> listeners = EventManager.getManager().getListeners();
		
		synchronized(listeners) 
		{
			for(final Object listener: listeners) 
			{
				Method[] methods = listener.getClass().getMethods();
				for(final Method method: methods) 
				{
					EventHandler annotation = method.getAnnotation(EventHandler.class);
					if(annotation != null) 
					{
						Class<?>[] argTypes = method.getParameterTypes();
						if(argTypes.length != 1)
							continue;
						if(!event.getClass().getSimpleName().equals(argTypes[0].getSimpleName()))
							continue;
						
						Runnable invoke = new Runnable() {
							public void run() {
								try {
									method.invoke(listener, event);
								} catch (Exception e) {
									e.printStackTrace();
								}								
							}
						};
						
						if(annotation.runAsync()) {
							new Thread(invoke).start();					
						} else {
							invoke.run();
						}
						
					}
				}
			}
		}
		
		totalTriggers++;
		
	}

}
