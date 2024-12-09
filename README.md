# Sistemas_Distribuidos
Aulas praticas referentes a UC Sistemas Distribuídos

> [!NOTE]
> Para remover todos os ficheiros ".class" presentes neste repositorio fazer 
> find diretorio_do_repositorio -name "*.class" -exec rm -f {} \;

## Nota Trabalho 2

Para verificar se a troca de informacao esta a ser bem feita, cada 
vez que e feita sincronizacao podemos fazer print em ambos os peers

## Dicas para trabalho 3

(ver esquema trabalho 3, as mensagens ack devem se repetir em cada peer, ou seja no esquema abaixo deviamos ter um processo ack para cada respetivo peer ,exceto aquele que enviou)

![alt text](<WhatsApp Image 2024-12-09 at 11.23.25.jpeg>)

1. ter 6 peer a trocar mensagens em multicast 

    + Garantir que faz a distribuicao das mensagens esta ser feita como esta demonstrado e explicado acima 

        + Com exemplo acima, sempre que enviamos uma mensagem:

            + enviamos a mensagem x para todos os peer's (incluindo o proprio peer que envia a mensagem).

            + cada peer responde com ack para todos os peer's.

    + nesta primeira fase podemos so enviar o timestamp invez de ter relogios de lampart

2. Depois de implementar priorityqeue sobre relogios de lampart 

    + So tiramos uma mensagem da priorityqueue se tiver uma mensagem para todos os outros peers (pode ser ack ou nao).No caso tiramos o ack de P2 porque verifamos que P1, P3, P4, P5 e P6 tem uma mensagem (pode ser mensagem ou ack)

        + Se for um ack (localmente esse peer descarta)

        + Senao, imprime o conteudo

    + O objetivo e que quando faz o processo de imprimir cada peer imprime na mesma ordem (pode ate ter mais elementos 
    mas a ordem tem de ser igual, essa ordem e dado por relogios de lampart)

    + O ack envia apenas o timestamp

    + As mensagens enviadas consistem numa lista de numeros (gerada nesse peer)




## Criterios AVALIACAO de todos os trabalhos

1. Se funciona como suposto

2. Se a maneira como resolvemos e "correta", ou seja que funciona e a aplicao dos algoritmos que ele pede 

3. legibilidade de codigo 

4. Nossa explicacao