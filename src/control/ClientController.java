package control;

import control.command.ChangeDataCommCommand;
import java.net.InetAddress;
import java.net.UnknownHostException;
import model.Client;

/**
 *
 * @author José Carlos
 */
public class ClientController {

    private static ClientController instance;
    private Client client;

    public static ClientController getInstance() {
        if (instance == null) {
            instance = new ClientController();
        }
        return instance;
    }

    private ClientController() {

    }

    public void setOnline(boolean online) {
        Client c = getClient();
        c.setOnline(online);
        try {
            c.setPath(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
        }
        c.setPort(56001);
        setClient(c);
        new ChangeDataCommCommand(getClient()).execute();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

}
