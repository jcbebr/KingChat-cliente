package control.observer;

public interface InitFrameObserver extends Observer{

    void notifyNextFrame();
    
    void notifyCleanFields();
}
