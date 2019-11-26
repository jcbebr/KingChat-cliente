package control.observer;

public interface Observed {

    void addObservador(Observer obs);

    void removerObservador(Observer obs);
}
