package control.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Client;
import model.CommandStatementName;
import org.json.JSONObject;

/**
 * Classe para definição da comunicação entre todos atores do sistema
 *
 * @author José Carlos
 */
public abstract class CommCommand {

    protected Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    protected boolean status;

    public CommCommand() {
        try {
            this.socket = new Socket("192.168.1.8", 56000);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(CommCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract void execute();

    public boolean getStatus() {
        return status;
    }

    protected void send(String command) {
        out.println(command);
    }

    protected String read() {
        try {
            return in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(CommCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    protected void close() {
        out.close();
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(CommCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(CommCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setClientOnJSON(JSONObject json, Client client) {
        json.put(CommandStatementName.CMM_NICK.getName(), client.getNick());
        json.put(CommandStatementName.CMM_BIRTH.getName(), client.getBirth());
        json.put(CommandStatementName.CMM_EMAIL.getName(), client.getEmail());
        json.put(CommandStatementName.CMM_ID.getName(), client.getId());
        json.put(CommandStatementName.CMM_OLDEMAIL.getName(), client.getOldemail());
        json.put(CommandStatementName.CMM_ONLINE.getName(), client.isOnline());
        json.put(CommandStatementName.CMM_PASS.getName(), client.getPass());
        json.put(CommandStatementName.CMM_PATH.getName(), client.getPath());
        json.put(CommandStatementName.CMM_PORT.getName(), client.getPort());
    }

    public static Client getClientFromJSON(JSONObject json) {
        Client client = new Client();
        if (json.has(CommandStatementName.CMM_ID.getName())) {
            client.setId(json.getInt(CommandStatementName.CMM_ID.getName()));
        }
        if (json.has(CommandStatementName.CMM_NICK.getName())) {
            client.setNick(json.getString(CommandStatementName.CMM_NICK.getName()));
        }
        if (json.has(CommandStatementName.CMM_PASS.getName())) {
            client.setPass(json.getString(CommandStatementName.CMM_PASS.getName()));
        }
        if (json.has(CommandStatementName.CMM_BIRTH.getName())) {
            client.setBirth(json.getInt(CommandStatementName.CMM_BIRTH.getName()));
        }
        if (json.has(CommandStatementName.CMM_EMAIL.getName())) {
            client.setEmail(json.getString(CommandStatementName.CMM_EMAIL.getName()));
        }
        if (json.has(CommandStatementName.CMM_ONLINE.getName())) {
            client.setOnline(json.getBoolean(CommandStatementName.CMM_ONLINE.getName()));
        }
        if (json.has(CommandStatementName.CMM_PATH.getName())) {
            client.setPath(json.getString(CommandStatementName.CMM_PATH.getName()));
        }
        if (json.has(CommandStatementName.CMM_PORT.getName())) {
            client.setPort(json.getInt(CommandStatementName.CMM_PORT.getName()));
        }
        return client;
    }
    
    public static Client getRandomClient() {
        int nc = new Random().nextInt(9999);
        return new Client("Cliente teste_" + nc, "email@" + nc,
                "old_email@" + nc, "pass_" + nc, nc * 2, true, nc + "." + nc, nc);
    }
    
}
