package control.command.client_server;

import control.Defs;
import control.command.CommCommand;
import control.observer.ClientController;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONObject;

public class ChangeDataCommCommand extends CommCommand {

    private Client client;

    public ChangeDataCommCommand(Client client) {
        super(Defs.getInstance().getServer_path(), Defs.getInstance().getServer_port());
        this.client = client;
    }

    @Override
    public void run() {
        try {
            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.CHANGEDATA.ordinal());
            CommCommand.setClientOnJSON(json, client);

            send(json.toString());
            json = new JSONObject(read());
            System.out.println(json.toString());
            this.status = CommandStatementValue.CHANGEDATA_STATUS.ordinal() 
                    == json.getInt(CommandStatementName.CMM_TYPE.getName()) 
                    && json.getBoolean(CommandStatementName.CMM_STATUS.getName());
        } finally {
            close();
            ClientController.getInstance().processChangeData(status);
        }
    }

}
