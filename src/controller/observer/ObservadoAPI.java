package controller.observer;

import java.util.ArrayList;

public interface ObservadoAPI {
    void registraObservador(ObservadorAPI o);   // registra um observador
    void removeObservador(ObservadorAPI o);     // remove um observador
    void notificarTodos(NotificacaoAPI n);      // seta a notificacao, e notifica todos
    NotificacaoAPI getNotificacao();            // metodo de acessoa a notificacao

    String[] getCartasDealer();                 // notificacao: DealerCartas
    int getValorDealer();                       // notificacao: DealerCartas
    boolean getFinalizadoDealer();              // notificacao: DealerCartas

    String[] getCartasJogador(int idJogador, int mao);      // notificacao: JogadorCartas
    int getValorJogador(int idJogador, int mao);            // notificacao: JogadorCartas

    ArrayList<Integer> getApostaJogador(int idJogador);     // notificacao: JogadorAposta
    int getDinheiroJogador(int idJogador);                  // notificacao: JogadorAposta
    int getValorApostaJogador(int idJogador);               // notificacao: JogadorAposta
    boolean getPodeApostaJogador(int idJogador);            // notificacao: JogadorAposta
    
    boolean getPodeStand(int idJogador, boolean mao_splitada);          // notificacao: JogadorAcao
    boolean getPodeHit(int idJogador, boolean mao_splitada);            // notificacao: JogadorAcao
    boolean getPodeDouble(int idJogador, boolean mao_splitada);         // notificacao: JogadorAcao
    boolean getPodeSurrender(int idJogador, boolean mao_splitada);      // notificacao: JogadorAcao
    boolean getPodeSplit(int idJogador, boolean mao_splitada);          // notificacao: JogadorAcao

    String getResultado(int idJogador);           // notificacao: JogadorResultado
}
