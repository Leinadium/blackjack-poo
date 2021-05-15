package controller.observer;

public interface ObservadoDealer {
    void registraObservador(ObservadorDealer o);
    void retiraObservador(ObservadorDealer o);
    NotificacoesDealer get();
}
