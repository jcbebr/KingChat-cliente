package control.observer;

import control.ClientServer;
import control.Defs;
import control.RefreshContactList;
import control.command.client_client.send.SendFileCommCommand;
import control.command.client_client.send.SendMessageCommCommand;
import control.command.client_server.AddContactCommCommand;
import control.command.client_server.ChangeDataCommCommand;
import control.command.client_server.ContactListCommCommand;
import control.command.client_server.LoginCommCommand;
import control.command.client_server.RemoveContactCommCommand;
import control.command.client_server.SignInCommCommand;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Client;
import view.ChangeDataFrame;
import view.InitFrame;
import view.MainFrame;

/**
 *
 * @author José Carlos
 */
public class ClientController implements Observed {

    private static ClientController instance;
    private Client client;

    public static ClientController getInstance() {
        if (instance == null) {
            instance = new ClientController();
        }
        return instance;
    }

    private ClientController() {
        this.mObss = new ArrayList<>();
        this.iObss = new ArrayList<>();
        this.cObss = new ArrayList<>();
    }

    //----------------------------INIT_FRAME------------------------------------------//
    private final List<InitFrameObserver> iObss;

    public void doSingin(String nick, String email, String pass, String birth) {
        try {
            Client obj_client = new Client();
            obj_client.setNick(nick);
            obj_client.setEmail(email);
            obj_client.setPass(pass);
            int iBirth = Integer.parseInt(birth);
            if (iBirth > 2019 || iBirth < 1900) {
                throw new NumberFormatException();
            }
            obj_client.setBirth(iBirth);
            SignInCommCommand signInCommCommand = new SignInCommCommand(obj_client);
            signInCommCommand.start();
        } catch (NumberFormatException ex) {
            notifyIOperationFailed("Valor inválido no cammpo 'ano de nascimento'");
        }
    }

    public void processSignin(boolean status) {
        if (status) {
            notifyIOperationSuccess("Sucesso ao cadastrar, faça o Login");
            notifyICleanFields();
        } else {
            notifyIOperationFailed("Não foi possível cadastrar este Usuário!");
        }
    }

    public void doLogin(String nick, String pass) {
        Client client = new Client();
        client.setNick(nick);
        client.setPass(pass);
        LoginCommCommand loginCommCommand = new LoginCommCommand(client);
        loginCommCommand.start();
    }

    public void processLogin(boolean status) {
        if (status) {
//            notifyIOperationSuccess("Sucesso ao logar");
            this.setOnline(true);
            notifyINextFrame();
            notifyMChangedTitle(this.client.getNick());
            new ClientServer().start();
            new RefreshContactList().start();
        } else {
            notifyIOperationFailed("Não foi fazer o login, confira seus dados!");
        }
    }

    private void notifyIOperationFailed(String message) {
        for (InitFrameObserver obs : iObss) {
            obs.notifyOperationFailed(message);
        }
    }

    private void notifyIOperationSuccess(String message) {
        for (InitFrameObserver obs : iObss) {
            obs.notifyOperationSuccess(message);
        }
    }

    private void notifyINextFrame() {
        for (InitFrameObserver obs : iObss) {
            obs.notifyNextFrame();
        }
    }

    private void notifyICleanFields() {
        for (InitFrameObserver obs : iObss) {
            obs.notifyCleanFields();
        }
    }

    //----------------------------CHANGE_DATA-----------------------------------------//
    private final List<ChangeDataObserver> cObss;

    public void refreshClient(String nick, String email, String pass, String birth) {
        try {
            this.client.setNick(nick);
            this.client.setEmail(email);
            this.client.setPass(pass);
            this.client.setBirth(Integer.parseInt(birth));

            ChangeDataCommCommand changeData = new ChangeDataCommCommand(this.client);
            changeData.start();
        } catch (NumberFormatException ex) {
            notifyCOperationFailed("Valor inválido no cammpo 'ano de nascimento'");
        }
    }

    public void processChangeData(boolean status) {
        if (status) {
            notifyCOperationSuccess("Sucesso ao alterar dados");
            notifyMChangedTitle(this.client.getNick());
            cObss.clear();
        } else {
            notifyCOperationFailed("Não foi possível alterar os dados!");
        }
    }

    private void notifyCOperationFailed(String message) {
        for (ChangeDataObserver obs : cObss) {
            obs.notifyOperationFailed(message);
        }
    }

    private void notifyCOperationSuccess(String message) {
        for (ChangeDataObserver obs : cObss) {
            obs.notifyOperationSuccess(message);
        }
    }

    //----------------------------MAIN_FRAME-----------------------------------------//
    private final List<MainFrameObserver> mObss;

    public void addContact(Object obj_Client) {
        Client send_client = (Client) obj_Client;
        AddContactCommCommand addContact = new AddContactCommCommand(this.client, send_client);
        addContact.start();
    }

    public void removeContact(Object obj_Client) {
        Client send_client = (Client) obj_Client;
        RemoveContactCommCommand removeContact = new RemoveContactCommCommand(this.client, send_client);
        removeContact.start();
    }

    public void contactList(boolean avaliable) {
        ContactListCommCommand contactList = new ContactListCommCommand(ClientController.getInstance().getClient(), avaliable);
        contactList.start();
    }

    public void processContactList(boolean avaliable, boolean status, List<Client> list) {
        if (status) {
            if (avaliable) {
                notifyMChangedAvaliableList(list);
            } else {
                notifyMChangedOwnList(list);
            }
        } else {
            notifyMOperationFailed("Não foi possível atualizar");
        }
    }

    public void sendMessage(String message) {
        if (lastSelected != null) {
            SendMessageCommCommand sendMessage = new SendMessageCommCommand(lastSelected, message);
            sendMessage.start();
        }
    }

    public void sendFile(File file) {
        if (lastSelected != null) {
            SendFileCommCommand sendFile = new SendFileCommCommand(lastSelected, file);
            sendFile.start();
        }
    }

    public void persistChat(Client obj_client) {
        if (obj_client.getClass().equals(Client.class)) {
            processChat(((Client) obj_client).getId());
        }
    }

    public byte[] processChat(int clientId) {
        try {
            String fileName = ClientController.getInstance().getClient().getId() + "_" + clientId + ".txt";
            if (new File(fileName).exists()) {
                Path path = Paths.get(fileName);
                byte[] bytes = Files.readAllBytes(path);
                if (this.lastSelected != null && this.lastSelected.getId() == clientId) {
                    notifyMRefreshChat(bytes, "");
                }
                return bytes;
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void persistSendChat(int clientId, String message) {
        FileWriter fr = null;
        try {
            File file = new File(ClientController.getInstance().getClient().getId() + "_" + clientId + ".txt");
            fr = new FileWriter(file, true);
            fr.write(ClientController.getInstance().getClient().getNick() + ": " + message + "\n");
        } catch (IOException ex) {
            Logger.getLogger(SendFileCommCommand.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(SendFileCommCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void persistReceiveChat(int clientId, String clientNick, String message) {
        FileWriter fr = null;
        try {
            File file = new File(ClientController.getInstance().getClient().getId() + "_" + clientId + ".txt");
            fr = new FileWriter(file, true);
            fr.write(clientNick + ": " + message + "\n");
        } catch (IOException ex) {
            Logger.getLogger(SendFileCommCommand.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(SendFileCommCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static Client lastSelected;

    public void refreshChat(Object obj_client) {
        if (obj_client.getClass().equals(Client.class)) {
            Client c = (Client) obj_client;
            if (c.isOnline()) {
                this.lastSelected = c;
                notifyMRefreshChat(processChat(c.getId()), c.getNick() + ", " + (2019 - c.getBirth()));
            } else {
                notifyMOperationFailed("Você não pode iniciar um chat com um contato offline.");
            }
        }
    }

    private void notifyMRefreshChat(byte[] messages, String nick) {
        for (MainFrameObserver obs : mObss) {
            obs.notifyRefreshChat(messages, nick);
        }
    }

    private void notifyMOperationFailed(String message) {
        for (MainFrameObserver obs : mObss) {
            obs.notifyOperationFailed(message);
        }
    }

    private void notifyMChangedTitle(String title) {
        for (MainFrameObserver obs : mObss) {
            obs.notifyChangedTitle("KingChat - " + title);
        }
    }

    private void notifyMChangedAvaliableList(List<Client> list) {
        for (MainFrameObserver obs : mObss) {
            obs.notifyChangedAvaliableList(list);
        }
    }

    private void notifyMChangedOwnList(List<Client> list) {
        for (MainFrameObserver obs : mObss) {
            obs.notifyChangedOwnList(list);
        }
    }

    //----------------------------COMMON-----------------------------------------//
    public void setOnline(boolean online) {
        Client c = getClient();
        try {
            c.setPath(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
        }
        c.setPort(Defs.getInstance().getClient_port());
        setClient(c);
        new ChangeDataCommCommand(getClient()).start();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void addObservador(Observer obs) {
        if (obs.getClass().equals(MainFrame.class)) {
            this.mObss.add((MainFrame) obs);
        } else if (obs.getClass().equals(InitFrame.class)) {
            this.iObss.add((InitFrame) obs);
        } else if (obs.getClass().equals(ChangeDataFrame.class)) {
            this.cObss.add((ChangeDataFrame) obs);
        }
    }

    @Override
    public void removerObservador(Observer obs) {
        if (obs.getClass().equals(MainFrame.class)) {
            this.mObss.remove((MainFrameObserver) obs);
        } else if (obs.getClass().equals(InitFrame.class)) {
            this.iObss.remove((InitFrameObserver) obs);
        } else if (obs.getClass().equals(ChangeDataFrame.class)) {
            this.cObss.remove((ChangeDataObserver) obs);
        }
    }

}
