package controller;

import controller.observer.*;
import views.*;
import model.Blackjack;

public class Controller implements ObservadorDealer, ObservadorMenu {
    FrameInicial frameInicial;
    FrameDealer frameDealer;
    Blackjack api;

    public Controller() {
        // carrega as imagens
        Imagem.carregar();
        this.frameInicial = new FrameInicial();
        this.frameInicial.registraObservador(this);
        this.frameInicial.abrir();
    }


    public void notificar(ObservadoMenu o) {
        switch (o.get()) {
            case Carregar -> System.out.println("Ainda nao implementado");
            case Iniciar -> iniciarPartida(o.getQuantidadeJogadores());
        }
    }

    public void notificar(ObservadoDealer o) {
        switch (o.get()) {
            case PartidaEncerrada -> fecharPartida();
            case PartidaSalva -> System.out.println("Ainda nao implementado");
            case RodadaNova -> iniciarRodada();
        }
    }

    public void fecharPartida() {
        this.frameDealer.fechar();
        this.frameDealer = null;
        this.frameInicial.abrir();
    }

    public void iniciarPartida(int quantidadeJogadores) {
        // fecha o menu
        this.frameInicial.fechar();

        // inicia o jogo
        api = new Blackjack(quantidadeJogadores, "");
        this.frameDealer = new FrameDealer();
        this.frameDealer.registraObservador(this);

        // TODO
    }

    public void iniciarRodada() {
        // distribui as cartas para o dealer
        String[] cartasDealer = api.distribuiCartasDealer();
        frameDealer.adicionarCarta(cartasDealer[0]);
        frameDealer.adicionarCarta(cartasDealer[1]);

        // TODO
    }
}