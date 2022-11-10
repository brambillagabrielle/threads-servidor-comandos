package ServidorComandos;

import java.io.PrintStream;
import java.net.Socket;

public class Cliente {
    
    private String ip;
    private Socket socket;
    private PrintStream saida;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintStream getSaida() {
        return saida;
    }

    public void setSaida(PrintStream saida) {
        this.saida = saida;
    }
    
}
