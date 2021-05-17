package controller.observer;

public interface ObservadoJogador {
    void registraObservador(ObservadorJogador o);
    void retiraObservador(ObservadorJogador o);
    NotificacoesJogador get();
}