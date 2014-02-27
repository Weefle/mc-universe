package com.octopod.network.events;

import com.octopod.network.NetworkDebug;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		new Thread(event.getClass().getSimpleName() + "-" + totalTriggers++) {
			@Override
			public void run() {
				trigger(event);
			}
		}.start();
	}

	private synchronized void trigger(final Event event) {

		NetworkDebug.verbose("Triggering event in thread: &a" + Thread.currentThread().getName());

		List<Object> listeners = new ArrayList<>(EventManager.getManager().getListeners());

        Iterator<Object> listener_it = listeners.iterator();

        synchronized(listeners) {
            while(listener_it.hasNext()) {
                final Object listener = listener_it.next();
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

	}

}
