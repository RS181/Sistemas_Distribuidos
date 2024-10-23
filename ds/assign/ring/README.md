# Como correr o progama (exemplo)

+ No diretorio  (...)/Sistemas_Distribuidos, compilar

****

    $ javac ds/assign/ring/*.java

+ Executar o CalculatorMultiServer (porta default e 44444)

**** 

    $ java ds.assign.ring.CalculatorMultiServer localhost

+ Executar os Peer's 
                
****   

    Peer_1 $ java ds.assign.ring.Peer localhost 10000 localhost 20000

    Peer_2 $ java ds.assign.ring.Peer localhost 20000 localhost 30000

    Peer_3 $ java ds.assign.ring.Peer localhost 30000 localhost 40000

    Peer_4 $ java ds.assign.ring.Peer localhost 40000 localhost 50000

    Peer_5 $ java ds.assign.ring.Peer localhost 50000 localhost 10000

+ Injetar o Token num dos peers 

**** 

    $ java ds.assign.ring.Token localhost 10000 

## Versao com script (so funciona se  todos os peer estao a correr na mesma maquina)
    terminal_1$ pwd 
    (...)/Sistemas_Distribuidos

    terminal_1$ javac ds/assign/ring/*.java  

    terminal_1$ ./ds/assign/ring/run.sh 

    terminal_1$ java ds.assign.ring.Token localhost 10000
