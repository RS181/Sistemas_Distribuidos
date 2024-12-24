# Distributed Systems - Practical Assignment 2024/25

This Directory contains the implementation of application prototypes for various scenarios in Distributed Systems.

## Projects Overview

### 1. Mutual Exclusion with the Token Ring Algorithm (`ds.assign.ring`)
- Implements mutual exclusion in a distributed environment using the Token Ring Algorithm.
- A ring network with 5 peers, each having exclusive access to a calculator server via a token.
- Peers generate server requests following a Poisson distribution and process them when holding the token.

### 2. Counting the Number of Nodes in a P2P Network (`ds.assign.entropy`)
- Uses the Anti-Entropy Algorithm to count and disseminate the number of nodes in a peer-to-peer network.
- Each peer maintains a map of connected peers, periodically updated and synchronized across the network.
- Handles node removals gracefully by using timestamps to manage stale entries.

### 3. A Basic Chat Application Using Totally-Ordered Multicast (`ds.assign.tom`)
- Implements a chat system ensuring a consistent order of messages using the Totally-Ordered Multicast (TOM) Algorithm.
- Leverages Lamport Clocks for message timestamping and synchronization across peers.
- All peers agree on the order of messages, achieving a unified view of the chat history.



## How to Run 

For more detailed information of how to run please checK the **README.md** present in each package (Note that the **README.md** present in every package is in **Portuguese**)
