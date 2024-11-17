# Duvidas

+ No trabalho 2, quando um peer atualiza o seu mapa (faze um pedido de sincronizacao), com outro peer, no final temos o mesmo mapa em ambos os peers??? 

+ Long chega para representar o timestamp certo?

+ Os peer's que nao estao diretamente conectados conhecem-se apenas pela 'partilha' de mapas

# Observacoes
> Anti-entropy: Each replica regularly chooses another replica at random,
and exchanges state differences, leading to identical states at both
afterwards

![alt text](peer_network.jpeg)

> Ja fiz teste com 6 peer's e de momento nao ocorre nao acontecerem ```erros de Concorrencia``` explicitamente


# Todo

1. Fazer ajustes na classe Server , para atualizar corretamente o mapa (nao esquecer que na sincronizacao de mapas, queremos o timestamp maior (mais recente) quando fizermos o merge) 

2. Fazer testes e verificar que tudo funcuona bem 