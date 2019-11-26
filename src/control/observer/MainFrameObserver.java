package control.observer;

import java.util.List;
import model.Client;

public interface MainFrameObserver extends Observer{

    void notifyChangedTitle(String title);
    
    void notifyChangedAvaliableList(List<Client> list);
    
    void notifyChangedOwnList(List<Client> list);
    
    void notifyRefreshChat(byte[] messages, String nick);
}
