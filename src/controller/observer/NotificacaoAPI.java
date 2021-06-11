/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */

package controller.observer;

/**
 * Enumerador para as notificacoes dos observadores
 */
public enum NotificacaoAPI {
    DealerCartas,       // O dealer recebeu alguma carta
    JogadorCartas,      // Algum jogador recebeu alguma carta
    DealerAposta,       // A aposta padrao foi modificada
    JogadorAposta,      // A aposta de algum jogador foi modificada
    JogadorAcao,        // A condicao de alguma acao do jogador foi modificada
    JogadorResultado,   // O resultado do jogador (se ganhou, perdeu, empatou)
    JogadorFalido,      // Avisa que alguem faliu
}
