package control.command.client_client.receive;

import control.command.CommCommand;
import control.command.client_client.send.SendMessageCommCommand;
import control.observer.ClientController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CommandStatementName;
import org.json.JSONObject;

/**
 *
 * @author José Carlos
 */
public class ReceiveFileCommCommand extends CommCommand {

    private JSONObject json;
    private int id;
    private String nick;
    private String file_name;

    public ReceiveFileCommCommand(Socket socket, JSONObject json) {
        super(socket);
        this.json = json;
    }

    @Override
    public void run() {
        InputStream fIn = null;
        OutputStream fOut = null;
        try {
            this.id = json.getInt(CommandStatementName.CMM_ID.getName());
            this.nick = json.getString(CommandStatementName.CMM_NICK.getName());
            this.file_name = json.getString(CommandStatementName.CMM_FILE.getName());

            ClientController.getInstance().persistReceiveChat(this.id, this.nick, "Recebendo arquivo");
            
            File file = new File(this.file_name);
            fIn = socket.getInputStream();
            fOut = new FileOutputStream(file);

            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            byte[] bytes = new byte[16 * 1024];
            int count;
            while ((count = fIn.read(bytes)) > 0) {
                fOut.write(bytes, 0, count);
            }

            ClientController.getInstance().persistReceiveChat(this.id, this.nick, "Aqruivo: '" + this.file_name + "' recebido.");

        } catch (IOException ex) {
            ClientController.getInstance().persistReceiveChat(this.id, this.nick, "Falha ao receber arquivo.");
            Logger.getLogger(ReceiveFileCommCommand.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fOut.close();
                fIn.close();
                close();
            } catch (IOException ex) {
                Logger.getLogger(ReceiveFileCommCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
            ClientController.getInstance().processChat(this.id);
        }
    }

}
