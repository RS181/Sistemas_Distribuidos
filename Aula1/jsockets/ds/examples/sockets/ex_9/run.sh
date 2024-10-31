#!/bin/bash

# Inicia os Peers em terminais separados

gnome-terminal -- bash -c "java ds.examples.sockets.ex_9.Peer localhost 22222 33333; " &
gnome-terminal -- bash -c "java ds.examples.sockets.ex_9.Peer localhost 33333 22222; " &