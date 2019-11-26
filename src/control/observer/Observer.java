package control.observer;

/**
 *
 * @author José Carlos
 */
public interface Observer {
    
    void notifyOperationFailed(String message);

    void notifyOperationSuccess(String message);
    
}
