package controller;

import views.*;

public class Controller {
    FrameInicial frameInicial;
    FrameDealer frameDealer;

    public Controller() {
        // carrega as imagens
        Imagem.carregar();

        this.frameInicial = new FrameInicial(this);
        this.frameInicial.abrir();
    }

    public void iniciarPartida(int quantidadeJogadores) {
        this.frameInicial.fechar();
        this.frameDealer = new FrameDealer();
    }
}