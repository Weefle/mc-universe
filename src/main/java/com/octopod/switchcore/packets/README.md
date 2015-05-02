SwitchMC - Packets
======
Switch uses a packet-like system for sending information between servers.

Packets that are aimed from sender server A to reciever server B are prefixed with "PacketIn" aka requests.

Packets that are aimed from reciever server B back to sender server A are prefixed with "PacketOut" aka responses.

PacketInServerPing / PacketOutServerPing
------
This packet is used by SwitchMC to simply ping a server.
The ping is two-way; server A sends PacketInServerPing to server B, which will send PacketOutServerPing back to server A.
Example: getting the latency between your server and another server:
```java
SwitchPacket packet = new PacketInServerPing();

AsyncEventHandler<NetworkPacketInEvent> filter
