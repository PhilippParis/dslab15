Summary:

* Genral Architecture:
The application uses a layered architecture with service layers which provide the functionality
and data access objects which retrieve data (users). For each service and dao there exists a interface to ease possible replacements in the future.

* Connection handling:
The server starts, after creating the necessary server socket, a dispatcher thread (Dispatcher), which listens
for incoming connections and creates a channel for each one of them (TCPChannel). A UDPChannel is created once at startup
of the server. These channels run in seperate threads, listen for incoming messages and provide methods for sending messages.

* Message sending:
the IChannel interface provides two ways of sending messages: an asynchronous ('send(IMessage)') and a synchronous way ('sendAndWait(IMessage)'). 
The executing thread of 'sendAndWait' will be blocked until a message with the same messageID is received on the same channel or after a timeout period (1sec),
while the executing thread of 'send(IMessage)' is not blocked.

* Message execution:
Messages which are direct responses to Messages that are sent with 'sendAndWait(...)', unblock the waiting thread and will be returned as result of the method 'sendAndWait(...)'. 
These messages are execute by the caller of 'sendAndWait(...)'.
All other messages will be executed by the provided MessageExecutor (IMessageExecutor).
