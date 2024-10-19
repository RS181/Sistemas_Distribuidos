package ds.examples.sockets.ex_8;


/**
 * Class that starts two Peers and inject's one of them 
 * with a token
 */
public class Start {

    //Campos de incializacao Peer1
    private static  String hostPeer1 = "localhost";
    private static  String portPeer1 = "22222";
    
    //Campos de incializacao Peer2
    private static String  hostPeer2 = "localhost";
    private static String  portPeer2 = "33333";
    

    public static void main(String[] args) {
        //Tenho que colocar um valor incial em cada token
        //(devida a a necessidade de Peer ter esse argumento
        //diferente de "")
        String tokenPeer1="empty";
        String tokenPeer2="empty";

        if(coinToss() == 0){ //Peer 1 comeca com o token
            tokenPeer1 = "Token";
            System.out.println("Peer 1 starts with token");
        }
        else{   //Peer 2 comeca com o token
            tokenPeer2 = "Token";
            System.out.println("Peer 2 starts with token");
        }
        
        // Iniciar o Peer 1
        startPeer(hostPeer1,portPeer1,hostPeer2,portPeer2,tokenPeer1);

        //Iniciar o Peer 2
        startPeer(hostPeer2, portPeer2,hostPeer1, portPeer1, tokenPeer2);



        /**
         * Para fazer com n peers apenas tinha de configurar o esta classe
         * para fazer algo deste genero
         * 
         * java Peer host_Peer host_Peer_Port token? next_Peer next_Peer_Port
         * 
         * terminal_1$ java Peer localhost 5000 true localhost 6000
         * terminal_2$ java Peer localhost 6000 false localhost 7000
         * ......
         * terminal_n$ java Peer localhost .... false localhost 5000 
         * 
         * (simulando anel logico)
         */
    }

    /**
     * @param host      -> representa o host do Peer que vamos criar
     * @param port      -> Porta que o nosso Peer vai estar a funcionar
     * @param otherHost -> representa o host do Peer que vamos conectar
     * @param otherPort -> Porta do Peer que vamos conectar-nos
     * @param token     -> indica se o Peer vai comecar como token
     */
    private static void startPeer(String host,String port,String otherHost,String otherPort,String token) {
        // Cria um ProcessBuilder para executar o comando
        ProcessBuilder processBuilder = new ProcessBuilder();
        
        // Usamos gnome-terminal para executar o comando
        processBuilder.command("gnome-terminal", "--", "bash", "-c", 
        "cd /home/rui/Desktop/Sistemas_Distribuidos/Aula1/jsockets && java ds.examples.sockets.ex_8.Peer "
        + host + " " + port + " " + token.equals("Token") + " " + otherHost + " " + otherPort);

        try {
            // Inicia o processo
            Process process = processBuilder.start();

            // Aguarda o término do processo e obtém o código de saída
            int exitCode = process.waitFor();
            System.out.println("Processo finalizado com código: " + exitCode);
        } catch (Exception e) {
            System.out.println("Problema no Start.java ao criar Peer com porta @" + port);
        }
    }
    
    /**
     * 
     * @return 0 or 1 (simulating a coin toss)
     */
    static int coinToss(){
        return (int)Math.round(Math.random());
    }
}
