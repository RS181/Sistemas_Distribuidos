# Notas
1. cada vez que enviamos  incrementamos o relogio de lampart (na cabeca temos a mensagem mais antiga e na cauda temos a mensagem mais recente)

2. No ack temos de enviar um relogio lampart


3. professor nao esta disponivel no dia 23/24/25 mas ele disse para retirar duvidas basta envia email e deixar fazer zoom 

> PARA MAIS DETALHES SOBRE TOTALY ORDERED MULTICAST COM RELOGIOS DE LAMPART VER A SECCAO Totaly Ordered Multicast no pdf LogicalClocks.pdf

# Comandos para testar no laboritorio (TODO)

+ No diretorio (...)/Sistemas_Distribuidos, compilar (IMPORTANTE)

```bash
    $ javac ds/assign/tom/*.java
```

+ Executar os Peer's

```bash
    # Peer 1
    java ds.assign.tom.Peer L820 10000 L821 20000 L822 30000 L823 40000 L824 50000 L826 60000   

    # Peer 2
    java ds.assign.tom.Peer L821 20000 L820 10000 L822 30000 L823 40000 L824 50000 L826 60000   

    # Peer 3
    java ds.assign.tom.Peer L822 30000 L820 10000 L821 20000 L823 40000 L824 50000 L826 60000   

    # Peer 4
    java ds.assign.tom.Peer L823 40000 L820 10000 L821 20000 L822 30000 L824 50000 L826 60000   

    # Peer 5
    java ds.assign.tom.Peer L824 50000 L820 10000 L821 20000 L822 30000 L823 40000 L826 60000 

    # Peer 6
    java ds.assign.tom.Peer L826 60000 L820 10000 L821 20000 L822 30000 L823 40000 L824 50000 ```
```

# Comandos para testar localmente

+ No diretorio (...)/Sistemas_Distribuidos, compilar (IMPORTANTE)

```bash
    $ javac ds/assign/tom/*.java
```

+ Executar os Peer's 

```bash
    # Peer 1
    java ds.assign.tom.Peer localhost 10000 localhost 20000 localhost 30000 localhost 40000 localhost 50000 localhost 60000   

    # Peer 2
    java ds.assign.tom.Peer localhost 20000 localhost 10000 localhost 30000 localhost 40000 localhost 50000 localhost 60000   

    # Peer 3
    java ds.assign.tom.Peer localhost 30000 localhost 10000 localhost 20000 localhost 40000 localhost 50000 localhost 60000   

    # Peer 4
    java ds.assign.tom.Peer localhost 40000 localhost 10000 localhost 20000 localhost 30000 localhost 50000 localhost 60000   

    # Peer 5
    java ds.assign.tom.Peer localhost 50000 localhost 10000 localhost 20000 localhost 30000 localhost 40000 localhost 60000 

    # Peer 6
    java ds.assign.tom.Peer localhost 60000 localhost 10000 localhost 20000 localhost 30000 localhost 40000 localhost 50000 
```

# Verificar diferencas entre ficheiros output

+ **Verificar se conteudo de ficheiros e diferente**

```bash
    # Se os arquivos tiverem o mesmo conteúdo, o comando diff não retornará nada (ou seja, a saída será vazia). Caso contrário, ele exibirá as diferenças entre os arquivos.

    diff ds/assign/tom/out/localhost_X0000_dic.txt ds/assign/tom/out/localhost_Y0000_dic.txt
```


+ **Ver ficheiro lado a lado**
```bash
    # Mostra os arquivos lado a lado para facilitar a visualização das diferenças.
    diff --side-by-side ds/assign/tom/out/localhost_X0000_dic.txt ds/assign/tom/out/localhost_Y0000_dic.txt

```
