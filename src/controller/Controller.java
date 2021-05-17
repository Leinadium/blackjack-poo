package controller;

import java.util.ArrayList;

import views.*;
import model.Blackjack;

public class Controller {
    FrameInicial frameInicial;
    FrameDealer frameDealer;
    ArrayList<FrameJogador> frameJogador;
    Blackjack api;

    public Controller() {
        // carrega as imagens
        Imagem.carregar();
        this.frameInicial = new FrameInicial(this);
        this.frameInicial.abrir();
    }
    
    public void fazerJogada(String numJogador, String acao){
    	switch(acao) {
            case "STAND" -> System.out.println("Ainda nao implementado");
            case "HIT" -> System.out.println("Ainda nao implementado");
            case "DOUBLE" -> System.out.println("Ainda nao implementado");
            case "SURRENDER" -> System.out.println("Ainda nao implementado");
            case "SPLIT" -> acionaSplit(0); //por enquanto é só do jogador 1, depois vamos expandir para os demais
    	}
    }

    void acionaSplit(int numJogador) {
    	String numJogadorString = String.format("%d", numJogador+1);
    	this.frameJogador.add(new FrameJogador(this, numJogadorString)); //cria nova janela para a mao splitada
    	int ultimo = this.frameJogador.size() - 1;
    	this.frameJogador.get(ultimo).iniciarRodada(); //ultima janela precisa ser inicializada
    	String[] cartasJogador = api.distribuiCartasJogador(numJogador, true); //distribui as cartas para o jogador
    	this.frameJogador.get(ultimo).adicionarCarta(cartasJogador[2]);
    	this.frameJogador.get(ultimo).adicionarCarta(cartasJogador[3]);
    	this.frameJogador.get(ultimo).iniciarRodada();
    	this.frameJogador.get(numJogador).substituirCarta(0,  cartasJogador[0]);
    	this.frameJogador.get(numJogador).substituirCarta(1, cartasJogador[1]);
    }
    
    public void fecharPartida() {
    	int i;
    	int quantidadeJogadores = this.frameJogador.size();
    	for (i=0; i<quantidadeJogadores;i++) {
    		this.frameJogador.get(i).fechar();
    	}
        this.frameDealer.fechar();
        this.frameDealer = null;
        
        //abre novamente o menu para caso queira iniciar uma nova partida
        this.frameInicial.abrir();
    }

    public void salvarPartida() {
        System.out.println("Salvando partida (ainda nao implementado)!");
        // TODO
    }

    public void carregarPartida() {
        System.out.println("Carregar partida! (ainda nao implementado)");
        // TODO
    }

    public void iniciarPartida(int quantidadeJogadores) {
        // fecha o menu
        this.frameInicial.fechar();

        // inicia o jogo
        api = new Blackjack(quantidadeJogadores);
        this.frameJogador = new ArrayList<>();
        for (int i = 0; i < quantidadeJogadores; i++) {
        	String numJogador = String.format("%d", i+1);
        	this.frameJogador.add(new FrameJogador(this, numJogador));
        }
        this.frameDealer = new FrameDealer(this);
    }

    public void iniciarRodada() {
    	int i;
    	int quantidadeJogadores = this.frameJogador.size();
        // distribui as cartas para o dealer
        String[] cartasDealer = api.distribuiCartasDealer();
        frameDealer.adicionarCarta(cartasDealer[0]);
        frameDealer.adicionarCarta(cartasDealer[1]);
        
        // distribui as cartas para o jogador
        for(i = 0; i < quantidadeJogadores; i++) {
        	String[] cartasJogador = api.distribuiCartasJogador(i, false);
        	this.frameJogador.get(i).adicionarCarta(cartasJogador[0]);
        	this.frameJogador.get(i).adicionarCarta(cartasJogador[1]);
        	this.frameJogador.get(i).iniciarRodada();
        }
    }
}