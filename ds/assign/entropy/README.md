# Duvidas

+ No trabalho 2, quando um peer atualiza o seu mapa (faze um pedido de sincronizacao), com outro peer, no final temos o mesmo mapa em ambos os peers??? 

>R: Sim

+ Long chega para representar o timestamp certo?

> R:Sim

+ Os peer's que nao estao diretamente conectados conhecem-se apenas pela 'partilha' de mapas

> R:sim

+ Quando inicializamos um Peer e respetivos vizinhos  podemos colocar os vizinhos automaticamente no seu mapa de vizinho
com respetivo timestamp atual? ( mesmo nao ter  feito qualquer conexao previa a esse vizinho)

> R: SIm 

+ Quando um peer recebe outro peer que nao e seu vizinho direto, ele pode enviar pedidos de sincronizacao, certo?

> R: Nao, apenas pode fazer pedidos de sincronizacao a vizinhos diretos 

+ Qual seria um threshold valido para remover um peer? (tem de ser um alto o suficiente para nao remover um peer por engano)

> R: (5 min , ou seja em media 10 tentativas)

+ Apos a remocao de um peer da rede, e suposto que o mesmo se possa conectar novamente a rede certo? (precisso verificar se isto acontece, mas penso que da maneira como tenho a aplicao a fazer as coisas isto ja deve acontecer sem ser precisso fazer nada, mas e necessario testa isto!!)

> R: Sim

# Observacoes
> Anti-entropy: Each replica regularly chooses another replica at random,
and exchanges state differences, leading to identical states at both
afterwards

![alt text](peer_network.jpeg)

> Ja fiz teste com 6 peer's e de momento nao ocorre nao acontecerem ```erros de Concorrencia``` explicitamente



# Todo

1. Modificar PeerConnection para apenas enviar pedidos de Sincronizacao para vizinhos diretos 

> FEITO

2. Corrigir localizacao de logs (colocar a pasta de logs no git, nao sei porque mas nao esta a por no git)

3. Tentar descobrir com debug com prints, o pq de estar a fazer dois merge's (quando teste nas maquina de laboratorio)

2. Fazer alteracoes ao codigo e verificar se o funcionamento e o esperado (localmente e nas maquinas do lab) (ver se vale a penas fazer isto)

    + remover parte de updateSenderPeer e sendData(data) (para dentro do if (!resultMap.equals(data)))

    + Verificar se o data= neighbourInfo.updateTimestampMap(data, port, host) esta no sitio certo (eu acho que tenho de colocar dentro do if (!resultMap.equals(data)))

2. Adicionar a remocao de um peer apos um certo threshold ser verificado (ver tempo que professor sugeriu)


3. Testar nas maquinas do laboratorio

    + Eu testei com exemplo de 3 peer's e parece estar perto do funcionamento pretendido, 
    mas as vezes apos uma sincronizacao o peer que envia o pedido de sincronizacao e depois recebe o do respetivo vizinho (esta a receber duplicado, mas no final fica com o mesmo mapa aparentemente)

    + Testar novamente e ver a acontecer (penso que nao e um erro por assim dizer, mas e necessario fazer umas alteracoes ao coidigo apenas para garantir que funciona em condicoes)

4. Perguntar se o terceiro trabalho se tem algum " exercicio exemplo"  para o terceiro trabalho, ou se algum dos exercicios que fizemos se assemelha.

> Professor vai disponiblizar no moodle 

5. Restrutar o README para indicar como e que se corre o programa