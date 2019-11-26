package control.command.client_client.receive;

import control.command.CommCommand;
import model.CommandStatementName;
import model.CommandStatementValue;
import java.net.Socket;
import org.json.JSONObject;

/**
 *
 * @author José Carlos
 */
public class IdentifyCommandCommCommand extends CommCommand {

    public IdentifyCommandCommCommand(Socket socket) {
        super(socket);
        System.out.println("Conectou");
    }

    @Override
    public void run() {
        JSONObject json = new JSONObject(this.read());
        System.out.println(json.toString());
        switch (CommandStatementValue.values()[json.getInt(CommandStatementName.CMM_TYPE.getName())]) {
            case MESSAGE:
                new ReceiveMessageCommCommand(socket, json).start();
                break;
            case FILE:
                new ReceiveFileCommCommand(socket, json).start();
                break;
            default:
                send("");
                break;
        }

    }

}
