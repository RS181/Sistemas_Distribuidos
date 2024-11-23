#!/bin/bash

#Funciona se for todos o CalculatorMultiServer e Peer's forem incializados na mesma maquina


# Inicia o CalculatorMultiServer
gnome-terminal -- bash -c "java ds.assign.ring.CalculatorMultiServer localhost > ds/assign/ring/logs/CalculatorServer.txt;"

# Inicia os Peers em terminais separados
gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 10000 localhost 20000 localhost; " &
gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 20000 localhost 30000 localhost; " &
gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 30000 localhost 40000 localhost; " &
gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 40000 localhost 50000 localhost; " &
gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 50000 localhost 10000 localhost; " &


# Two Peer version 
# gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 10000 localhost 20000; " &
# gnome-terminal -- bash -c "java ds.assign.ring.Peer localhost 20000 localhost 10000; " &

