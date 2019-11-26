package control.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import model.CommandStatementName;
import model.CommandStatementValue;
import control.Defs;
import org.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author José Carlos
 */
public class LoginCommCommandTest extends TestCase {

    private boolean bodyTest(String nick, String pass) {
        try (Socket socket = new Socket(Defs.getInstance().getServer_path(), Defs.getInstance().getServer_port());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {
            
            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.LOGIN.ordinal());
            json.put(CommandStatementName.CMM_NICK.getName(), nick);
            json.put(CommandStatementName.CMM_PASS.getName(), pass);
            out.println(json.toString());
            json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            assertEquals(CommandStatementValue.LOGIN_STATUS.ordinal(),
                    json.getInt(CommandStatementName.CMM_TYPE.getName()));
            return json.getBoolean(CommandStatementName.CMM_STATUS.getName());

        } catch (IOException ex) {
            Logger.getLogger(LoginCommCommandTest.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @Test
    public void testExecuteSuccess() {
        assertTrue(bodyTest("jose", "jose"));
    }

    @Test
    public void testExecuteFail() {
        assertFalse(bodyTest("asd", "jose"));
    }

}
