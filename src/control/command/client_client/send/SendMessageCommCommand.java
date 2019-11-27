package control.command.client_client.send;

import control.command.CommCommand;
import control.observer.ClientController;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONObject;

/**
 *
 * @author José Carlos
 */
public class SendMessageCommCommand extends CommCommand {

    private final Client client;
    private final String message;

    public SendMessageCommCommand(Client client, String message) {
        super(client.getPath(), client.getPort());
        this.client = client;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.MESSAGE.ordinal());
            json.put(CommandStatementName.CMM_ID.getName(), ClientController.getInstance().getClient().getId());
            json.put(CommandStatementName.CMM_NICK.getName(), ClientController.getInstance().getClient().getNick());
            json.put(CommandStatementName.CMM_MESSAGE.getName(), this.message);
            send(json.toString());

            ClientController.getInstance().persistSendChat(client.getId(), message);
        } finally {
            close();
            ClientController.getInstance().processChat(this.client.getId());
        }
    }

}
