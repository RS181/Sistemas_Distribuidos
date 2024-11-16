#!/bin/bash

# Inicia os Peers em terminais separados (ver figura com esquema de conexao inicial entre Peer's)

# Peer 1 
gnome-terminal -- bash -c "java ds.examples.sockets.ex_9_extra.Peer localhost 2000 localhost 3000 localhost 4000; " &

# Peer 2 
gnome-terminal -- bash -c "java ds.examples.sockets.ex_9_extra.Peer localhost 3000 localhost 2000 ; " &

# Peer 3 
gnome-terminal -- bash -c "java ds.examples.sockets.ex_9_extra.Peer localhost 4000 localhost 2000 localhost 5000 ; " &


# Peer 4
gnome-terminal -- bash -c "java ds.examples.sockets.ex_9_extra.Peer localhost 5000 localhost 4000 ; " &

