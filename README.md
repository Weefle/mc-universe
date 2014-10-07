NetworkPlus
======

Multi-server based commands and events.

Plugin Goals:
- [ ] Send relevant information dynamically between servers when they need it (as opposed to interval-based)
- [ ] Modular design for potential custom addons
- [ ] Abstraction layer to easily convert to other APIs

Dynamic Information Synchronizing
------
In contrast to interval-based server synchronization, NetworkPlus aims to send relevant information only when it needs to.

Network Packets
------
Sending messages cross-server uses a packet-like system. For example, asking a server to bring a player into it is:
```java
new PacketOutPlayerSend(player).send(server);
```

The arguments of the packets are an array of strings, which are serialized and used as the message to send between servers.
Because of this, the NetworkMessageInEvent automatically tries to deserialize the message as an array of strings for easy access.
If the message cannot be deserialized as an array of strings, then then an array with the message being the only element will be made.

Some messages, such as PacketOutPlayerSend, expect a message to be returned on a different channel via the NetworkMessageInEvent.
At this time, the only way to tell apart what return messages are for which packet is by the channel.

You can create a Temporary Listener (next section) to listen and/or wait for this return message,
then match the channel of the event to the "in channel" of the packet:
```java
final NetworkPacket message = new MessageOutPlayerSend(player).send(server);
TempListenerFilter<NetworkMessageInEvent> filter = new TempListenerFilter<>()
{
    public boolean onEvent(TempListener<NetworkMessageInEvent> listener,
        NetworkMessageInEvent event)
    {
        if(event.getChannel().equals(message.getChannelIn())
        {
            //Code here
        }
    }
}
```

Feel free to add your own NetworkPackets; just have your class extend NetworkPacket.

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

Also, if you want to wait for the listener on a different thread, you can do that as well. In that case,
you'd want to make a TempListenerFinish:

```java
TempListenerFinish finish = new TempListenerFinish()
{
    public void finish(boolean success)
    {
        //Listener finished!
    }
}
```

Then use this when asynchronously waiting for the listener:

```java
listener.waitForAsync(1000, finish);
```