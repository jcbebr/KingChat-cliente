package control.command.client_server;

import control.Defs;
import control.command.CommCommand;
import java.util.ArrayList;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONArray;
import org.json.JSONObject;

public class RemoveContactCommCommand extends CommCommand {

    private final Client client1;
    private final Client client2;

    public RemoveContactCommCommand(Client client1, Client client2) {
        super(Defs.getInstance().getServer_path(), Defs.getInstance().getServer_port());
        this.client1 = client1;
        this.client2 = client2;
    }

    @Override
    public void run() {
        try {
            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.REMOVECONTACT.ordinal());

            JSONArray optJSONArray = new JSONArray(new ArrayList<Client>() {
                {
                    add(client1);
                    add(client2);
                }
            });
            json.put(CommandStatementName.CMM_CLIENT_ARRAY.getName(), optJSONArray);

            send(json.toString());
            json = new JSONObject(read());
            System.out.println(json.toString());
            this.status = CommandStatementValue.REMOVECONTACT_STATUS.ordinal() 
                    == json.getInt(CommandStatementName.CMM_TYPE.getName()) 
                    && json.getBoolean(CommandStatementName.CMM_STATUS.getName());
        } finally {
            close();
        }
    }

}
