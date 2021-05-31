package controller.observer;

import java.util.HashMap;
import java.util.ArrayList;

public interface ObservadoAPI {
    void registraObservador(ObservadorAPI o);   // registra um observador
    void removeObservador(ObservadorAPI o);     // remove um observador
    void notificarTodos(NotificacaoAPI n);      // seta a notificacao, e notifica todos
    NotificacaoAPI getNotificacao();            // metodo de acessoa a notificacao

    String[] getCartasDealer();                 // notificacao: DealerCartas
    int getValorDealer();                       // notificacao: DealerCartas
    boolean getFinalizadoDealer();              // notificacao: DealerCartas

    String[] getCartasJogador(int idJogador, int mao);     // notificacao: JogadorCartas
    int getValorJogador(int idJogador, int mao);           // notificacao: JogadorCartas

    ArrayList<Integer> getApostaJogador(int idJogador);    // notificacao: JogadorAposta
    int getDinheiroJogador(int idJogador);
    int getValorApostaJogador(int idJogador);
    boolean getPodeApostaJogador(int idJogador);
    
    boolean getPodeStand(boolean mao_splitada);
    boolean getPodeHit(boolean mao_splitada);
    boolean getPodeDouble(boolean mao_splitada);
    boolean getPodeSurrender(boolean mao_splitada);
    boolean getPodeSplit(boolean mao_splitada);
}
