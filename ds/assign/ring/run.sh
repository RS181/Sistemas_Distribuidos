#!/bin/bash

# Inicia o CalculatorMultiServer
gnome-terminal -- bash -c "java ds.assign.ring.CalculatorMultiServer localhost; exec bash"

# Inicia os Peers em terminais separados
gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 10000 localhost 20000; " &
gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 20000 localhost 30000; " &
gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 30000 localhost 40000; " &
gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 40000 localhost 50000; " &
gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 50000 localhost 10000; " &
