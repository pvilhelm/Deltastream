# Deltastream
Deltastream is a distributed streaming protocol atop on UDP to decrease the load
on the source node. The protocol is mainly intended for data where the "freshness"
of if is of importance, e.g. live video. 

It functions as a application layer to implement a transparent transport layer between 
the source node and multiple receiver nodes. The Deltastream protocol is distributed,
i.e. each receiving node is also transmitting to other receiving nodes, lowering 
the load on the source node. 

The protocol is specially design for a uncontrolled and unkind environment, which
is, the source doesn't control the protocol implementation the other nodes uses
or can trust the fairness or ability of the nodes. 

Each node have to "buy" access to the stream from other nodes by promising future
data of the same stream. 

Which this set-up, destructive nodes and nodes with bad throughput will eventually
get starved out as they don't contribute to the stream.  