package controller.observer;

public enum NotificacaoAPI {
    DealerCartas,       // O dealer recebeu alguma carta
    JogadorCartas,      // Algum jogador recebeu alguma carta
    DealerAposta,       // A aposta padrao foi modificada
    JogadorAposta,      // A aposta de algum jogador foi modificada
    JogadorAcao, // A condicao de alguma acao do jogador foi modificada
}
