package controller;

import views.*;

public class Controller {
    FrameInicial frameInicial;
    FrameDealer frameDealer;

    public Controller() {
        this.frameInicial = new FrameInicial(this);
        this.frameInicial.abrir();
    }

    public void iniciarPartida(int quantidadeJogadores) {
        this.frameInicial.fechar();
        System.out.println("comecando a partida com " + quantidadeJogadores + " jogadores");
    }
}