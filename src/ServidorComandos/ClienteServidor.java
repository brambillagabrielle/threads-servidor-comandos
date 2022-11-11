package ServidorComandos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClienteServidor extends Thread {
    
    private static boolean terminado = false;
    private final Socket conexao;

    public ClienteServidor(Socket conexao) {
        this.conexao = conexao;
    }
    
    @Override
    public void run() {
        
        try {
        
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(
                          conexao.getInputStream()
                    )
            );
                
            String linha;
            while (true) {
                
                linha = entrada.readLine();
                
                if (linha == null) {
                    System.out.println("Fim da conexão");
                    break;
                }
                
                System.out.println(linha);
                
            }
            
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        
        terminado = true;
        
    }
    
    public static void main(String args[]) {
        
        try {
            
            Socket conexao = new Socket("127.0.0.1", 1234);

            PrintStream saida = new PrintStream(
                    conexao.getOutputStream()
            );
            BufferedReader teclado = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            
            System.out.println("Entre com os comandos para serem executados: ");
            System.out.println("# ");
            
            Thread thread = new ClienteServidor(conexao);
            thread.start();
            
            String linha;
            while(true) {
                
                linha = teclado.readLine();
                
                if(terminado) {
                    break;
                }
                
                saida.println(linha);
                
            }
            
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        
    }
    
}
