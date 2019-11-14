package control.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONObject;

public class ChangeDataCommCommand extends CommCommand {

    private Client client;

    public ChangeDataCommCommand(Client client) {
        this.client = client;
    }

    @Override
    public void execute() {
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
        }
    }

}
