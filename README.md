NetworkPlus
======

Multi-server based commands and events.

Plugin Goals:
- [ ] Send relevant information dynamically between servers when they need it (as opposed to interval-based)
- [ ] Modular design for potential custom addons
- [ ] Abstraction layer to easily convert to other APIs

Temporary Listeners
------
In NetworkPlus, you can create listeners that will run an X amount of times before unregistering itself.
You can also wait for the listener to unregister, allowing you to wait for responses from other servers.
There are three parts in creating the TempListener, the type of event, the filter, and the required executions.

First, create the TempListenerFilter.
This allows you to process each event call and return which ones are "valid" executions.
```java
//Or any event
TempListenerFilter<NetworkMessageEvent> filter = new TempListenerFilter<>()
{
    public boolean onEvent(TempListener<NetworkMessageEvent> listener,
        NetworkMessageEvent event)
    {
        //Code here
        return true;
    }
}
```

Then, create the listener:
```java
//Defaults to 1 execution
TempListener<NetworkMessageEvent> listener = new TempListener<>(NetworkMessageEvent.class, filter);
```

Now, the next time the event is called, it will run this listener, causing it to unregister itself.
If you want, you can wait for the listener to unregister itself.

```java
//Wait for 1000 ms before timing out
boolean complete = listener.waitFor(1000);
```