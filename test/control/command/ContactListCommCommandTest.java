package control.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import control.Defs;
import org.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author José Carlos
 */
public class ContactListCommCommandTest extends TestCase {

    private boolean bodyTest(Client client, boolean avaliable) {
        try (Socket socket = new Socket(Defs.getInstance().getPath(), Defs.getInstance().getPort());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {

            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.CONTACTLIST.ordinal());
            json.put(CommandStatementName.CMM_CLIENT_LIST_AVALIABLE.getName(), avaliable);
            CommCommand.setClientOnJSON(json, client);

            out.println(json.toString());
            json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            assertEquals(CommandStatementValue.CONTACTLIST_STATUS.ordinal(),
                    json.getInt(CommandStatementName.CMM_TYPE.getName()));
            return json.getBoolean(CommandStatementName.CMM_STATUS.getName());

        } catch (IOException ex) {
            System.out.println(ex);
            return false;
        }
    }

    @Test
    public void testExecuteAvaliableSuccess() {
        Client client = CommCommand.getRandomClient();
        client.setId(1);
        assertTrue(bodyTest(client, true));
    }

    @Test
    public void testExecuteAvaliableFail() {
        Client client = new Client();
        assertTrue(bodyTest(client, true));
    }

    @Test
    public void testExecuteOwnSuccess() {
        Client client = CommCommand.getRandomClient();
        client.setId(1);
        assertTrue(bodyTest(client, false));
    }

    @Test
    public void testExecuteOwnFail() {
        Client client = new Client();
        assertTrue(bodyTest(client, false));
    }

}
