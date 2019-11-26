package control.command.client_client.send;

import control.command.CommCommand;
import control.observer.ClientController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONObject;

/**
 *
 * @author José Carlos
 */
public class SendFileCommCommand extends CommCommand {

    private final Client client;
    private final File file;

    public SendFileCommCommand(Client client, File file) {
        super(client.getPath(), client.getPort());
        this.client = client;
        this.file = file;
    }

    @Override
    public void run() {
        InputStream fIn = null;
        OutputStream fOut = null;
        try {
            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.FILE.ordinal());
            json.put(CommandStatementName.CMM_ID.getName(), ClientController.getInstance().getClient().getId());
            json.put(CommandStatementName.CMM_NICK.getName(), ClientController.getInstance().getClient().getNick());
            json.put(CommandStatementName.CMM_FILE.getName(), file.getName());
            send(json.toString());

            SendMessageCommCommand.persistMessage(client.getId(), "Enviando arquivo.");
            
            byte[] bytes = new byte[16 * 1024];
            fIn = new FileInputStream(file);
            fOut = socket.getOutputStream();

            int count;
            while ((count = fIn.read(bytes)) > 0) {
                fOut.write(bytes, 0, count);
            }

            SendMessageCommCommand.persistMessage(client.getId(), "Arquivo enviado com sucesso.");

        } catch (IOException ex) {
            SendMessageCommCommand.persistMessage(client.getId(), "Falha ao enviar arquivo.");
            Logger.getLogger(SendFileCommCommand.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fOut.close();
                fIn.close();
                close();
            } catch (IOException ex) {
                Logger.getLogger(SendFileCommCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
            ClientController.getInstance().persistChat(this.client.getId());
        }
    }

   

}
