#!/bin/bash

# Inicia os Peers em terminais separados (ver figura com esquema de conexao inicial entre Peer's)

# Peer 1 (Initial neighbour = Peer 2 )
gnome-terminal -- bash -c "java ds.assing.entropy.Peer localhost 10000 localhost 20000 ; " &

# Peer 2 (Initial neighbour's = Peer 1, Peer 3, Peer 4 )
gnome-terminal -- bash -c "java ds.assing.entropy.Peer localhost 20000 localhost 10000 localhost 30000 localhost 40000 ; " &

# Peer 3 (Initial neighbour = Peer 2 )
gnome-terminal -- bash -c "java ds.assing.entropy.Peer localhost 30000 localhost 20000; " &


# Peer 4 (Initial neighbour's = Peer 2, Peer 5, Peer 6)
gnome-terminal -- bash -c "java ds.assing.entropy.Peer localhost 40000 localhost 20000 localhost 50000 localhost 60000; " &

# Peer 5 (Initial neighbour = Peer 4)
gnome-terminal -- bash -c "java ds.assing.entropy.Peer localhost 50000 localhost 40000" &

# Peer 6 (Initial neighbour = Peer 4)
gnome-terminal -- bash -c "java ds.assing.entropy.Peer localhost 60000 localhost 40000" &