package control.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import control.Defs;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author José Carlos
 */
public class AddContactCommCommandTest extends TestCase {

    private boolean bodyTest(Client client1, Client client2) {
        try (Socket socket = new Socket(Defs.getInstance().getServer_path(), Defs.getInstance().getServer_port());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {

            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.ADDCONTACT.ordinal());

            JSONArray optJSONArray = new JSONArray(new ArrayList<Client>() {
                {
                    add(client2);
                    add(client1);
                }
            });

            json.put(CommandStatementName.CMM_CLIENT_ARRAY.getName(), optJSONArray);

            out.println(json.toString());
            json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            assertEquals(CommandStatementValue.ADDCONTACT_STATUS.ordinal(),
                    json.getInt(CommandStatementName.CMM_TYPE.getName()));
            return json.getBoolean(CommandStatementName.CMM_STATUS.getName());
            
        } catch (IOException ex) {
            Logger.getLogger(LoginCommCommandTest.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Test
    public void testExecuteSuccess() {
        Client client1 = CommCommand.getRandomClient();
        client1.setId(1);
        Client client2 = CommCommand.getRandomClient();
        client2.setId(5);
        assertTrue(bodyTest(client1, client2));
    }

    @Test
    public void testExecuteFail() {
        Client client1 = CommCommand.getRandomClient();
        client1.setId(null);
        Client client2 = CommCommand.getRandomClient();
        client2.setId(5);
        assertFalse(bodyTest(client1, client2));
    }

}
