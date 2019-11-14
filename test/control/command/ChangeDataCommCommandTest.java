package control.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ChangeDataCommCommandTest extends TestCase {

    private boolean bodyTest(Client client) {
        try (Socket socket = new Socket(Defs.getInstance().getPath(), Defs.getInstance().getPort());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {

            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.CHANGEDATA.ordinal());
            CommCommand.setClientOnJSON(json, client);

            out.println(json.toString());
            json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            assertEquals(CommandStatementValue.CHANGEDATA_STATUS.ordinal(),
                    json.getInt(CommandStatementName.CMM_TYPE.getName()));
            return json.getBoolean(CommandStatementName.CMM_STATUS.getName());

        } catch (IOException ex) {
            Logger.getLogger(LoginCommCommandTest.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Test
    public void testExecuteSuccess() {
        Client client = new Client("jose", "jose", "" + System.currentTimeMillis(), "jose", 1999, false, "", 0);
        client.setId(1);
        assertTrue(bodyTest(client));
    }

    @Test
    public void testExecuteFail() {
        Client client = new Client();
        assertFalse(bodyTest(client));
    }

}
