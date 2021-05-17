package controller.observer;

public interface ObservadoAPI {
    void registraObservador(ObservadorAPI o);   // registra um observador
    void removeObservador(ObservadorAPI o);     // remove um observador
    void notificarTodos(NotificacaoAPI n);      // seta a notificacao, e notifica todos
    NotificacaoAPI getNotificacao();            // metodo de acessoa a notificacao

    String[] getCartasDealer();                 // notificacao: DealerCartas
    int getValorDealer();                       // notificacao: DealerCartas
}
