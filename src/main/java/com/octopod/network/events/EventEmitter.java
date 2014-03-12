package com.octopod.network.events;

import com.octopod.network.NetworkDebug;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
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

		for(final EventMethod eventMethod: collectListenersOf(event)) {

            Runnable invoke = new Runnable() {
                public void run() {
                    try {
                        eventMethod.invoke(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            if(eventMethod.handler().runAsync()) {
                new Thread(invoke).start();
            } else {
                invoke.run();
            }

        }

	}

    private List<EventMethod> collectListenersOf(Event event) {

        ArrayList<EventMethod> methods = new ArrayList<>();
        List<Object> listeners = new ArrayList<>(EventManager.getManager().getListeners());
        Iterator<Object> listener_it = listeners.iterator();

        synchronized(listeners) {
            while(listener_it.hasNext()) {
                Object listener = listener_it.next();
                for(Method method: listener.getClass().getMethods()) {
                    EventHandler annotation = method.getAnnotation(EventHandler.class);
                    if(annotation != null)
                    {
                        Class<?>[] argTypes = method.getParameterTypes();
                        if(argTypes.length != 1)
                            continue;
                        if(!event.getClass().getSimpleName().equals(argTypes[0].getSimpleName()))
                            continue;
                        methods.add(new EventMethod(listener, method, annotation));
                    }
                }
            }
        }

        Collections.sort(methods);
        return methods;

    }

    private static class EventMethod implements Comparable<EventMethod>
    {
        private Object object;
        private Method method;
        private EventHandler annotation;

        protected EventMethod(Object object, Method method, EventHandler annotation) {
            this.object = object;
            this.method = method;
            this.annotation = annotation;
        }

        public void invoke(Object... args) throws IllegalAccessException, InvocationTargetException {
            method.invoke(object, args);
        }

        public EventHandler handler() {return annotation;}

        @Override
        public int compareTo(EventMethod o) {
            return Integer.compare(annotation.priority().getPriority(), o.annotation.priority().getPriority());
        }
    }

}
