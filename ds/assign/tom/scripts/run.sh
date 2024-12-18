#!/bin/bash


# Peer 1
gnome-terminal -- bash -c "java ds.assign.tom.Peer localhost 10000 localhost 20000 localhost 30000 localhost 40000 localhost 50000 localhost 60000 ; " &  

# Peer 2
gnome-terminal -- bash -c "java ds.assign.tom.Peer localhost 20000 localhost 10000 localhost 30000 localhost 40000 localhost 50000 localhost 60000 ; " &  

# Peer 3
gnome-terminal -- bash -c "java ds.assign.tom.Peer localhost 30000 localhost 10000 localhost 20000 localhost 40000 localhost 50000 localhost 60000 ; " &  

# Peer 4
gnome-terminal -- bash -c "java ds.assign.tom.Peer localhost 40000 localhost 10000 localhost 20000 localhost 30000 localhost 50000 localhost 60000 ; " &  

# Peer 5
gnome-terminal -- bash -c "java ds.assign.tom.Peer localhost 50000 localhost 10000 localhost 20000 localhost 30000 localhost 40000 localhost 60000 ; " &

# Peer 6
gnome-terminal -- bash -c "java ds.assign.tom.Peer localhost 60000 localhost 10000 localhost 20000 localhost 30000 localhost 40000 localhost 50000 ; " &

