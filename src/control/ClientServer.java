package control;

import control.command.client_client.receive.IdentifyCommandCommCommand;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author José Carlos
 */
public class ClientServer extends Thread {

    private boolean alive;
    private ServerSocket server;

    public ClientServer() {
        this.alive = true;
    }

    @Override
    public void run() {
        try {
            this.server = new ServerSocket(Defs.getInstance().getClient_port());
            server.setReuseAddress(true);
            while (alive) {
                System.out.println("Aguardando conexao de cliente...");
                new IdentifyCommandCommCommand(server.accept()).start();
            }
        } catch (IOException ex) {
            if (ex.getClass().equals(BindException.class)) {
                JOptionPane.showMessageDialog(null, "Confira sua configuração de rede.");
                System.exit(0);
            }
            if (alive) {
                Logger.getLogger(ClientServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void close() {
        this.alive = false;
        try {
            this.server.close();
            System.out.println("Fechou");
        } catch (IOException ex) {
//            System.out.println(ex);
        }
    }

}
