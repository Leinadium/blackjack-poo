package controller.observer;

import java.io.File;

public interface ObservadoMenu {
    void registraObservador(ObservadorMenu o);
    void retiraObservador(ObservadorMenu o);
    NotificacoesMenu get();
    int getQuantidadeJogadores();
    File getArquivo();
}
