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
    
    public void fazerJogada(int idJogador, String acao){
    	switch (acao) {
            case "STAND": System.out.println("Ainda nao implementado");break;
            case "HIT": {
                this.api.fazerHitJogador(idJogador, 0); // por enquanto so na primeira mao
                break;
            }
            case "DOUBLE": System.out.println("Ainda nao implementado");break;
            case "SURRENDER": System.out.println("Ainda nao implementado");break;
            case "SPLIT": {
                acionaSplit(idJogador);    //por enquanto é só do jogador 1
                break;
            }
    	}
    }

    void acionaSplit(int idJogador) {
    	String numJogadorString = String.format("%d", idJogador+1);
    	int mao = api.quantidadeMaosSplitJogador(idJogador) + 1; // nova mao
    	this.frameJogador.add(new FrameJogador(this, numJogadorString, idJogador, mao)); //cria nova janela para a mao splitada
        int ultimo = this.frameJogador.size() - 1;
        FrameJogador novoFrame = this.frameJogador.get(ultimo);

        novoFrame.iniciarRodada();          //ultima janela precisa ser inicializada
    	api.distribuiCartasJogador(idJogador, true); //distribui as cartas para o jogador

        // this.frameJogador.get(ultimo).adicionarCarta(cartasJogador[2]);
    	// this.frameJogador.get(ultimo).adicionarCarta(cartasJogador[3]);
    	// this.frameJogador.get(ultimo).iniciarRodada();
    	// this.frameJogador.get(numJogador).substituirCarta(0,  cartasJogador[0]);
    	// this.frameJogador.get(numJogador).substituirCarta(1, cartasJogador[1]);
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

        // inicia os frames dos jogadores
        this.frameJogador = new ArrayList<>();
        for (int i = 0; i < quantidadeJogadores; i++) {
        	String numJogador = String.format("%d", i+1);
            this.frameJogador.add(new FrameJogador(this, numJogador, i, 0));
        }
        // inicia o frame do dealer
        this.frameDealer = new FrameDealer(this);

        // liga o dealer ao model (padrao observer)
        this.api.registraObservador(this.frameDealer);

        // liga os jogadores ao model (padrao observer)
        for (int i = 0; i < quantidadeJogadores; i ++) {
            this.api.registraObservador(this.frameJogador.get(i));
        }
    }

    public void iniciarRodada() {
    	int i;
    	int quantidadeJogadores = this.frameJogador.size();
        // distribui as cartas para o dealer
        api.distribuiCartasDealer();
        
        // distribui as cartas para o jogador
        for(i = 0; i < quantidadeJogadores; i++) {
        	api.distribuiCartasJogador(i, false);
        	// this.frameJogador.get(i).adicionarCarta(cartasJogador[0]);
        	// this.frameJogador.get(i).adicionarCarta(cartasJogador[1]);
        	this.frameJogador.get(i).iniciarRodada();
        }
    }
}