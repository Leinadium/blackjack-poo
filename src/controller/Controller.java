package controller;

import java.util.ArrayList;

import views.*;
import model.Blackjack;

public class Controller {
    FrameInicial frameInicial;
    FrameDealer frameDealer;
    ArrayList<FrameJogador> frameJogador;
    Blackjack api;
    Modo modo;

    public Controller() {
        // carrega as imagens
        Imagem.carregar();
        this.frameInicial = new FrameInicial(this);
        this.frameInicial.abrir();
    }
    
    public void passaVez() {
        api.passaVez();

        if (api.getJogadoresFinalizados()) {
            System.out.println("TODOS OS JOGADORES JOGARAM");
            this.modo = Modo.FINAL;
            this.api.fazerJogadaDealer();

        } else {
            this.frameJogador.get(api.getVez()).iniciarRodada();
            this.api.distribuiCartasJogador(false);
        }
    }
    
    private void acionaSplit() {
        int idJogador = api.getVez();
    	String numJogadorString = String.format("%d", idJogador+1);
    	
    	// cria nova janela
    	int mao = this.api.quantidadeMaosSplitJogador(idJogador) - 1; // nova mao
    	this.frameJogador.add(new FrameJogador(this, numJogadorString, idJogador, mao));

        // inicializa ultima janela
        int ultimo = this.frameJogador.size() - 1;
        FrameJogador novoFrame = this.frameJogador.get(ultimo);
        novoFrame.iniciarRodada();
        this.api.registraObservador(this.frameJogador.get(ultimo));
        this.api.distribuiCartasJogador(true);
    }
    
    public void fazerJogada(String acao, boolean mao_splitada) {
    	this.api.fazerJogada(acao);
    	if (acao.equals("SPLIT")) {
    		acionaSplit();
    	}
    	if (api.jogadorEhFinalizado()) {
    	    passaVez();
        }
    }

    public void aumentaAposta(int valor) {
        if (modo == Modo.APOSTA) {
            System.out.println("aumentando a aposta em " + valor);
            this.api.aumentaAposta(valor); }
    }

    public void diminuiAposta(int valor) {
        if (modo == Modo.APOSTA) {
            System.out.println("diminuindo a aposta em " + valor);
            this.api.diminuiAposta(valor);
        }
    }

    public void finalizarAposta() {
        this.api.finalizarAposta();
        this.api.passaVez();

        if (this.api.getJogadoresFinalizados()) {   // se todos apostaram
            this.api.resetVez();        // reseta a vez para comecar tudo
            this.frameJogador.get(api.getVez()).iniciarRodada();  // inicia o primeiro
            this.api.distribuiCartasJogador(false);
            this.api.distribuiCartasDealer();
            this.modo = Modo.JOGANDO;
        } else {
            this.frameJogador.get(this.api.getVez()).iniciarAposta();
        }
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
        this.api = Blackjack.getAPI();
        this.api.iniciarBlackjack(quantidadeJogadores);

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
        this.modo = Modo.INICIO;
    }

    public void iniciarAposta() {
        this.api.reiniciarJogadores();
        this.frameJogador.get(this.api.getVez()).iniciarAposta();
        this.modo = Modo.APOSTA;
    }

    public void iniciarRodada() {
    	int i;
    	int quantidadeJogadores = this.frameJogador.size();
    	
        this.frameJogador.get(0).iniciarRodada();
        this.modo = Modo.JOGANDO;
        
        this.api.iniciarRodada();
    }
    
    public void finalizarRodada() {
    	this.frameDealer.reiniciarDealer();
    }
}

enum Modo {
    INICIO,     // esperando ele clicar em inicar rodada
    APOSTA,     // os jogadores apostando
    JOGANDO,    // os jogadores jogando
    FINAL       // final da rodada, distribuindo o dinheiro
}