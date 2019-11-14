package control.command;

import java.util.ArrayList;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONArray;
import org.json.JSONObject;

public class AddContactCommCommand extends CommCommand {

    private final Client client1;
    private final Client client2;

    public AddContactCommCommand(Client client1, Client client2) {
        this.client1 = client1;
        this.client2 = client2;
    }

    @Override
    public void execute() {
        try {
            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.ADDCONTACT.ordinal());

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
            this.status = CommandStatementValue.ADDCONTACT_STATUS.ordinal()
                    == json.getInt(CommandStatementName.CMM_TYPE.getName())
                    && json.getBoolean(CommandStatementName.CMM_STATUS.getName());
        } finally {
            close();
        }
    }

}
