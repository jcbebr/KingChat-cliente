package control.command.client_client.receive;

import control.command.CommCommand;
import control.command.client_client.send.SendMessageCommCommand;
import control.observer.ClientController;
import java.net.Socket;
import model.CommandStatementName;
import org.json.JSONObject;

/**
 *
 * @author José Carlos
 */
public class ReceiveMessageCommCommand extends CommCommand {

    private JSONObject json;
    private String message;
    private int id;

    public ReceiveMessageCommCommand(Socket socket, JSONObject json) {
        super(socket);
        this.json = json;
    }

    @Override
    public void run() {
        try {
            this.id = json.getInt(CommandStatementName.CMM_ID.getName());
            this.message = json.getString(CommandStatementName.CMM_MESSAGE.getName());

            SendMessageCommCommand.persistMessage(this.id, message);
        } finally {
            close();
            ClientController.getInstance().persistChat(this.id);
        }
    }

}
