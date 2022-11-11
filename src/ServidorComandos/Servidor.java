package ServidorComandos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor extends Thread {
    
    private final Cliente cliente;
    private Socket conexao;
    private static List<Cliente> clientes;

    public Servidor(Cliente cliente) {
        this.cliente = cliente;
    }
    
    @Override
    public void run() {
        
        try {
            
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(
                            cliente.getSocket().getInputStream()
            ));
            PrintStream saida = new PrintStream(
                    cliente.getSocket().getOutputStream()
            );
            
            String linha = entrada.readLine();
            while (linha != null && !(linha.trim().equals(""))) {
                
                executaComando(linha, saida);
                saida.println("# ");
                linha = entrada.readLine();
                
            }
            
            clientes.remove(saida);
            conexao.close();
            
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        
    }
    
    public void executaComando(String comando, PrintStream saida) {
        
        try {
            
            // WINDOWS:
            Process processo = Runtime.getRuntime().exec("cmd /c " + comando);
            
            // LINUX:
            // Process processo = Runtime.getRuntime().exec(comando);
            StringBuilder saidaComando = new StringBuilder(); 

            BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(processo.getInputStream())
            );

            String linha;
            while((linha = leitor.readLine()) != null) {
                saidaComando.append(linha).append("\n");
            }
            
            int exitVal = processo.waitFor();
            if (exitVal == 0) {
                System.out.println("Comando executado com sucesso");
                saida.println(saidaComando);
            } else {
                saida.println("Erro\n");
            }
            
        } catch(InterruptedException | IOException e) {
            System.out.println(e);
        }
        
    }
    
    public static void main(String args[]) {
        
        System.out.println("* Servidor Terminal *");
        
        clientes = new ArrayList<>();
        
        try {
            
            ServerSocket serverSocket = new ServerSocket(1234);
            
            while(true) {
                
                Socket conexao = serverSocket.accept();
                
                Cliente cliente = new Cliente();
                cliente.setIp(conexao.getRemoteSocketAddress().toString());
                cliente.setSocket(conexao);
                clientes.add(cliente);
                
                System.out.println("\nNovo usuário conectado: " + cliente.getIp());
                System.out.println("Total de usuários atuais: " + clientes.size() + "\n");
                
                Thread thread = new Servidor(cliente);
                thread.start();
                
            }
            
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        
    }
    
}
