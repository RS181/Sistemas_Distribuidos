# TODO
+ Testar com maquinas do laboratorio

# Como correr o progama (exemplo)

+ No diretorio  (...)/Sistemas_Distribuidos, compilar

****

    $ javac ds/assign/ring/*.java

+ Executar o CalculatorMultiServer (porta default e 44444)

**** 

    $ java ds.assign.ring.CalculatorMultiServer localhost

+ Executar os Peer's (o utimo parametro representa o host calculator server)
                
****   

    Peer_1 $ java ds.assign.ring.Peer localhost 10000 localhost 20000 localhost

    Peer_2 $ java ds.assign.ring.Peer localhost 20000 localhost 30000 localhost

    Peer_3 $ java ds.assign.ring.Peer localhost 30000 localhost 40000 localhost

    Peer_4 $ java ds.assign.ring.Peer localhost 40000 localhost 50000 localhost

    Peer_5 $ java ds.assign.ring.Peer localhost 50000 localhost 10000 localhost

+ Injetar o Token num dos peers 

**** 

    $ java ds.assign.ring.Token localhost 10000 

## Versao com script (so funciona se  todos os peer estao a correr na mesma maquina)

    terminal_1$ pwd 
    (...)/Sistemas_Distribuidos

    terminal_1$ javac ds/assign/ring/*.java  

    terminal_1$ ./ds/assign/ring/scripts/run.sh 

    terminal_1$ java ds.assign.ring.Token localhost 10000


## Verificar logs de CalculatorMultiServer 

    Ver ficheiro /logs/CalculatorServer.txt

### Para obter logs de CalculatorMultiServer de um Peer especifico (dado a @Port do mesmo)

    terminal_1$ pwd 
    (...)/Sistemas_Distribuidos

    terminal_1$ ./ds/assign/ring/scripts/extract_messages.sh nr_porta


    ver ficheiro /out/nr_porta

### Para verificar se existem pedidos repetidos nos ficheiros /out/nr_porta

    terminal_1$ pwd 
    (...)/Sistemas_Distribuidos

    # A opção -c prefixa cada linha com o número de ocorrências na entrada:
    terminal_1$ uniq -c ds/assign/ring/out/nr_porta.txt 