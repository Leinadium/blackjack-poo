package controller;

import java.util.ArrayList;

import views.*;
import model.Blackjack;

public class Controller {
    FrameInicial frameInicial;
    FrameDealer frameDealer;
    ArrayList<FrameJogador> frameJogador;
    Blackjack api;
    int modo;   // 0 para aposta, 1 para partida, 2 para esperando

    public Controller() {
        // carrega as imagens
        Imagem.carregar();
        this.frameInicial = new FrameInicial(this);
        this.frameInicial.abrir();
    }
    
    public void fazerJogada(String acao) {
    	switch (acao) {
            case "STAND": {
                // por enquanto so passa a vez
                passaVez();
                break;
            }
            case "HIT": {
                this.api.fazerHitJogador(0); // por enquanto so na primeira mao
                break;
            }
            case "DOUBLE": System.out.println("Ainda nao implementado");break;
            case "SURRENDER": System.out.println("Ainda nao implementado");break;
            case "SPLIT": {
                acionaSplit();
                break;
            }
    	}
    }

    public void passaVez() {
        api.passaVez();

        if (api.getJogadoresFinalizados()) {
            System.out.println("TODOS OS JOGADORES JOGARAM");
            // TODO
        } else {
            api.distribuiCartasJogador(false);
            this.frameJogador.get(api.getVez()).iniciarRodada();
        }
    }

    void acionaSplit() {
        int idJogador = api.getVez();
    	String numJogadorString = String.format("%d", idJogador+1);
    	int mao = api.quantidadeMaosSplitJogador(idJogador) + 1; // nova mao
    	this.frameJogador.add(new FrameJogador(this, numJogadorString, idJogador, mao)); //cria nova janela para a mao splitada
        int ultimo = this.frameJogador.size() - 1;
        FrameJogador novoFrame = this.frameJogador.get(ultimo);

        novoFrame.iniciarRodada();          //ultima janela precisa ser inicializada
    	api.distribuiCartasJogador(true); //distribui as cartas para o jogador

        // this.frameJogador.get(ultimo).adicionarCarta(cartasJogador[2]);
    	// this.frameJogador.get(ultimo).adicionarCarta(cartasJogador[3]);
    	// this.frameJogador.get(ultimo).iniciarRodada();
    	// this.frameJogador.get(numJogador).substituirCarta(0,  cartasJogador[0]);
    	// this.frameJogador.get(numJogador).substituirCarta(1, cartasJogador[1]);
    }

    public void aumentaAposta(int valor) {
        if (modo == 0) {
            System.out.println("aumentando a aposta em " + valor);
            this.api.aumentaAposta(valor); }
    }

    public void diminuiAposta(int valor) {
        if (modo == 0) {
            System.out.println("diminuindo a aposta em " + valor);
            this.api.diminuiAposta(valor);
        }
    }

    public void finalizarAposta() {
        this.api.finalizarAposta();
        this.frameJogador.get(api.getVez()).iniciarRodada();
        this.api.distribuiCartasJogador(false);
        this.api.distribuiCartasDealer();
        modo = 1;
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

        this.modo = 2;
    }

    public void iniciarAposta() {
        this.api.reiniciarJogadores();
        this.frameJogador.get(this.api.getVez()).iniciarAposta();
        this.modo = 0;
    }

    public void iniciarRodada() {
    	int i;
    	int quantidadeJogadores = this.frameJogador.size();
        // distribui as cartas para o dealer
        // api.distribuiCartasDealer();

        // api.distribuiCartasJogador(false);
        this.frameJogador.get(0).iniciarRodada();
        this.modo = 1;
    }
}