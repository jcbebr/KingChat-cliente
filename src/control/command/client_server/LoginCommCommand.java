package control.command.client_server;

import control.Defs;
import control.command.CommCommand;
import control.observer.ClientController;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONObject;

public class LoginCommCommand extends CommCommand {

    private Client client;

    public LoginCommCommand(Client client) {
        super(Defs.getInstance().getServer_path(), Defs.getInstance().getServer_port());
        this.client = client;
    }

    @Override
    public void run() {
        try {
            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.LOGIN.ordinal());
            json.put(CommandStatementName.CMM_NICK.getName(), client.getNick());
            json.put(CommandStatementName.CMM_PASS.getName(), client.getPass());
            System.out.println(json.toString());
            send(json.toString());
            json = new JSONObject(read());

            this.status = (CommandStatementValue.LOGIN_STATUS.ordinal() == json.getInt(CommandStatementName.CMM_TYPE.getName())
                    && json.getBoolean(CommandStatementName.CMM_STATUS.getName()));
            if (this.status) {
                ClientController.getInstance().setClient(CommCommand.getClientFromJSON(json));
            }
        } finally {
            close();
            ClientController.getInstance().processLogin(this.status);
        }
    }

}
