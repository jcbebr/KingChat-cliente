package control.command.client_server;

import control.Defs;
import control.command.CommCommand;
import control.observer.ClientController;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONObject;

public class SignInCommCommand extends CommCommand {

    private Client client;

    public SignInCommCommand(Client client) {
        super(Defs.getInstance().getServer_path(), Defs.getInstance().getServer_port());
        this.client = client;
    }

    @Override
    public void run() {
        try {
            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.SIGNIN.ordinal());
            CommCommand.setClientOnJSON(json, client);

            send(json.toString());
            json = new JSONObject(read());
            System.out.println(json.toString());
            this.status = CommandStatementValue.SIGNIN_STATUS.ordinal()
                    == json.getInt(CommandStatementName.CMM_TYPE.getName())
                    && json.getBoolean(CommandStatementName.CMM_STATUS.getName());

        } finally {
            close();
            ClientController.getInstance().processSignin(this.status);
        }
    }

}
