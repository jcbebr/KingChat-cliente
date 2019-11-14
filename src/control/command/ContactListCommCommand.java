package control.command;

import java.util.ArrayList;
import java.util.List;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONArray;
import org.json.JSONObject;

public class ContactListCommCommand extends CommCommand {

    private Client client;
    private boolean avaliable;
    private ArrayList<Client> clients;

    public ContactListCommCommand(Client client, boolean avaliable) {
        this.client = client;
        this.avaliable = avaliable;
        this.clients = new ArrayList<>();
    }

    @Override
    public void execute() {
        try {
            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.CONTACTLIST.ordinal());
            json.put(CommandStatementName.CMM_CLIENT_LIST_AVALIABLE.getName(), avaliable);
            CommCommand.setClientOnJSON(json, client);

            send(json.toString());
            json = new JSONObject(read());
            System.out.println(json.toString());
            this.status = CommandStatementValue.CONTACTLIST_STATUS.ordinal()
                    == json.getInt(CommandStatementName.CMM_TYPE.getName())
                    && json.getBoolean(CommandStatementName.CMM_STATUS.getName());

            if (this.status) {
                JSONArray jsonClients = json.getJSONArray(CommandStatementName.CMM_CLIENT_ARRAY.getName());
                for (Object jsonClient : jsonClients) {
                    clients.add(CommCommand.getClientFromJSON(new JSONObject(jsonClient.toString())));
                }
            }
        } finally {
            close();
        }
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

}
