# Duvida

+ Mesmo apos remover um peer (apos ter-se desconectado e termos tirado do mapa com timestamps) devemos continuar a te-lo como vizinho direto (para o caso de que ele se volte a reconectar temos hipotese de sincronizar)

![alt text](duvida.jpeg)

R: sim

# Observacoes
> Anti-entropy: Each replica regularly chooses another replica at random,
and exchanges state differences, leading to identical states at both
afterwards

> Ja fiz teste com 6 peer's e de momento nao ocorre nao acontecerem ```erros de Concorrencia``` explicitamente

>Quando inicializamos um Peer e respetivos vizinhos podemos colocar os vizinhos automaticamente no seu mapa de vizinho com respetivo timestamp atual (PROFESSOR DISSE QUE SIM)

# Todo

1. Testar nas maquinas do laboratorio (agora vao ser l8xx)

    + localmente parece estar a funcionar como esperado, mas falta testar na maquina do laboratorio

    + MOSTRAR AO PROFESSOR A FUNCIONAR LOCAMENTE E DEPOIS TESTAR NO LAB E MOSTRAR TAMBEM

```Nota```: ja testei localmente e o funcionamento parece estar correto (ja testei remover peer's e voltar a adicionar e a informacao esta a propagar como esperado)


# Comandos para testar localmente (sem script)

> Comandos abaixo simulam este rede

![alt text](peer_network.jpeg)


+ No diretorio (...)/Sistemas_Distribuidos, compilar

```bash
$ javac ds/assign/entropy/*.java
```

+ Executar os Peer's

```bash 

Peer_1 $ java ds.assign.entropy.Peer localhost 10000 localhost 20000 

Peer_2 $ java ds.assign.entropy.Peer localhost 20000 localhost 10000 localhost 30000 localhost 40000 

Peer_3 $ java ds.assign.entropy.Peer localhost 30000 localhost 20000

Peer_4 $ java ds.assign.entropy.Peer localhost 40000 localhost 20000 localhost 50000 localhost 60000

Peer_5 $ java ds.assign.entropy.Peer localhost 50000 localhost 40000

Peer_6 $ java ds.assign.entropy.Peer localhost 60000 localhost 40000

```


# Comandos para testar no laboratorio 


+ No diretorio (...)/Sistemas_Distribuidos, compilar

```bash
$ javac ds/assign/entropy/*.java
```

+ Executar os Peer's (Um por maquina)


```bash 

Peer_1 $ java ds.assign.entropy.Peer L820 10000 L821 20000 

Peer_2 $ java ds.assign.entropy.Peer L821 20000 L820 10000 L822 30000 L823 40000 

Peer_3 $ java ds.assign.entropy.Peer L822 30000 L821 20000

Peer_4 $ java ds.assign.entropy.Peer L823 40000 L821 20000 L824 50000 L826 60000

Peer_5 $ java ds.assign.entropy.Peer L824 50000 L823 40000

Peer_6 $ java ds.assign.entropy.Peer L826 60000 L823 40000

```
