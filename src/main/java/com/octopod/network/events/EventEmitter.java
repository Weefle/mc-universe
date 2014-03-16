package com.octopod.network.events;

import com.octopod.network.NetworkPlus;
import org.bukkit.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
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

	public synchronized void triggerEvent(final Event event) {

		NetworkPlus.getLogger().verbose("Triggering event in thread: &a" + Thread.currentThread().getName());

        List<EventMethod> listeners = collectListenersOf(event);

        synchronized(listeners) {

            //Run all listeners
            for(final EventMethod eventMethod: listeners) {
                eventMethod.invoke(event);
            }

        }

	}

    private ArrayList<EventMethod> collectListenersOf(Event event) {

        List<Object> listeners = new ArrayList<>(EventManager.getManager().getListeners());

        synchronized(listeners) {
            ArrayList<EventMethod> methods = new ArrayList<>();
            for(Object listener: listeners) {
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
            Collections.sort(methods);
            return methods;
        }

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

        public Event invoke(final Event event) {

            try {
                Runnable invokeMethod = new Runnable() {
                    public void run() {
                        try {
                            method.invoke(object, event);
                        } catch (Exception e) {
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
        public int compareTo(EventMethod o) {
            return Integer.compare(annotation.priority().getPriority(), o.annotation.priority().getPriority());
        }
    }

}
