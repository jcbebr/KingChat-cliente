package control;

import control.observer.ClientController;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Carlos
 */
public class RefreshContactList extends Thread {

    private boolean alive;

    public RefreshContactList() {
        this.alive = true;
    }

    @Override
    public void run() {
        try {
            while (alive) {
                this.sleep(Defs.getInstance().getRf_interval() * 1000);
                ClientController.getInstance().contactList(true);
                ClientController.getInstance().contactList(false);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(RefreshContactList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void kil() {
        this.alive = false;
    }

}
