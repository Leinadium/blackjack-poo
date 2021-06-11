/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */

package controller.observer;

import java.util.ArrayList;

/**
 * Interface para a classe sendo observada do padrão Observer.
 */
public interface ObservadoAPI {
    void registraObservador(ObservadorAPI o);   // registra um observador
    void removeObservador(ObservadorAPI o);     // remove um observador
    void notificarTodos(NotificacaoAPI n);      // seta a notificacao, e notifica todos
    NotificacaoAPI getNotificacao();            // metodo de acessoa a notificacao

    /* NOTIFICACAO [DealerCartas] */
    String[] getCartasDealer();                 // retorna uma lista contendo as cartas do dealer
    int getValorDealer();                       // retorna a soma das cartas do dealer
    boolean getFinalizadoDealer();              // retorna se o dealer finalizou ou nao

    /* NOTIFICACAO [JogadorCartas] */
    String[] getCartasJogador(int idJogador, int mao);      // retorna uma lista com as cartas do jogador
    int getValorJogador(int idJogador, int mao);            // retorna a soma das cartas do jogador

    /* NOTIFICACAO [JogadorAposta] */
    ArrayList<Integer> getApostaJogador(int idJogador, int mao);    // retorna a lista de fichas da aposta
    int getDinheiroJogador(int idJogador);                          // retorna o dinheiro do jogador
    int getValorApostaJogador(int idJogador, int mao);              // retorna o valor total da aposta atual
    boolean getPodeApostaJogador(int idJogador);                    // retorna se o jogador ainda pode apostar

    /* NOTIFICACAO [JogadorAcao] */
    boolean getPodeStand(int idJogador, int mao);          // retorna se o jogador pode fazer Stand
    boolean getPodeHit(int idJogador, int mao);            // retorna se o jogador pode fazer Hit
    boolean getPodeDouble(int idJogador, int mao);         // retorna se o jogador pode fazer Double
    boolean getPodeSurrender(int idJogador, int mao);      // retorna se o jogador pode fazer Surrender
    boolean getPodeSplit(int idJogador, int mao);          // retorna se o jogador pode fazer Split

    /* NOTIFICACAO [JogadorResultado] */
    String getResultado(int idJogador, int idMao);           // retorna quem ganhou (Dealer, Jogador ou Push)
}
