package controller;

import java.awt.*;
import java.io.IOException;
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
            finalizarRodada();

        } else {
            this.frameJogador.get(api.getVez()).iniciarRodada();
            this.api.distribuiCartasJogador(false);
            // caso o jogador faça um blackjack,
            if (api.jogadorEhFinalizado()) {
                passaVez();
            }

        }
    }
    
    private void acionaSplit() {
        int idJogador = api.getVez();
    	String numJogadorString = String.format("%d", idJogador+1);
    	
    	// cria nova janela
    	this.frameJogador.add(new FrameJogador(this, numJogadorString, idJogador, 1));

        // inicializa ultima janela
        int ultimo = this.frameJogador.size() - 1;
        FrameJogador novoFrame = this.frameJogador.get(ultimo);
        novoFrame.iniciarRodada();
        this.api.registraObservador(this.frameJogador.get(ultimo));
        this.api.distribuiCartasJogador(true);
    }
    
    public void fazerJogada(String acao, boolean mao_splitada) {
    	this.api.fazerJogada(acao, mao_splitada);
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
            this.iniciarRodada();

        } else {
            this.frameJogador.get(this.api.getVez()).iniciarAposta();
        }
    }

    public void fecharPartida() {
    	int i;
    	int quantidadeJogadores = this.frameJogador.size();
    	for (i=0; i<quantidadeJogadores;i++) {
    	    this.api.removeObservador(this.frameJogador.get(i));
    		this.frameJogador.get(i).fechar();
    	}
    	this.api.removeObservador(this.frameDealer);
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
    	int i, j, quantidadeJogadores, vez;
    	int idJogador, dinheiro;
    	int aposta;
    	String naipe, nome;
    	try {
			ArrayList<String> partida_salva = controller.Load.main(null);
			quantidadeJogadores = Integer.parseInt(partida_salva.get(0));
			vez = Integer.parseInt(partida_salva.get(1));
			aposta = Integer.parseInt(partida_salva.get(2));
			this.iniciarPartida(quantidadeJogadores);
			this.api.defineVez(vez); // pode mexer direto mas achei melhor nao
			this.frameJogador.get(this.api.getVez()).iniciarAposta();
			this.modo = Modo.APOSTA;
			this.api.defineAposta(aposta);
			this.frameJogador.get(this.api.getVez()).finalizarAposta();
			// ainda tem que passar a aposta para o lado
	        this.frameJogador.get(api.getVez()).iniciarRodada();  // inicia o primeiro
	        this.modo = Modo.JOGANDO;
	        // o botao ainda ta aparecendo como disabled pros outros jogadores
	        for (i = 0; i < quantidadeJogadores; i++) {
	        	this.frameJogador.get(i).finalizarAposta();
	        }
	        for (j = 3; j < partida_salva.size(); j++) {
	        	// nao consegui pensar em um nome melhor do que lista_elementos
	        	String[] lista_elementos = partida_salva.get(j).split(";");
	        	if ((lista_elementos[0]).equals("Dinheiro")) {
	        		idJogador = Integer.parseInt(lista_elementos[1]);
	        		dinheiro = Integer.parseInt(lista_elementos[2]);
	        		this.api.defineDinheiro(idJogador, dinheiro);
	        	}
	        	if ((lista_elementos[0]).equals("Carta")) {
	        		idJogador = Integer.parseInt(lista_elementos[2]);
	        		nome = (lista_elementos[3].split("-"))[0];
	        		naipe = (lista_elementos[3].split("-"))[1];
	        		if (idJogador > 3) { // entao eh o dealer
	        			this.api.distribuiCartaDealer(nome, naipe);
	        		}
	        		else {
	        			this.api.distribuiCartaJogador(idJogador, nome, naipe);
	        		}
	        	}
	        }
			
		} catch (IOException e) {
			System.out.println("Deu erro em carregar partida! (ainda nao implementado)");
			
		}
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
        if (this.modo == Modo.FINAL) {
            this.reiniciarRodada();
        }

        this.api.reiniciarJogadores();
        this.frameJogador.get(this.api.getVez()).iniciarAposta();
        this.modo = Modo.APOSTA;
    }

    public void iniciarRodada() {
    	int i;
    	// int quantidadeJogadores = this.frameJogador.size();

        this.api.resetVez();        // reseta a vez para comecar tudo
        this.frameJogador.get(api.getVez()).iniciarRodada();  // inicia o primeiro
        this.modo = Modo.JOGANDO;
        
        this.api.iniciarRodada();
        this.api.distribuiCartasDealer();
        this.api.distribuiCartasJogador(false);

        if (this.api.jogadorEhFinalizado()) {   // caso ele tenha um blackjack
            passaVez();
        }
    }

    public void reiniciarRodada() {
        ArrayList<FrameJogador> copia = new ArrayList<>(frameJogador);

        // desliga as maos de split, e nao copia as maos de split para a copia
        for (FrameJogador fj: this.frameJogador) {
            if (fj.idMao != 0) {
                fj.fechar();    // fecha a janela
                this.api.removeObservador(fj);
            } else {
                fj.reiniciarJogador();  // reinicia as propriedades
                copia.add(fj);
            }
        }

        // reescrevendo a lista de frameJogadores, agora as maos split sumiram
        this.frameJogador = copia;

        this.api.reiniciarDealer();
        this.frameDealer.reiniciarDealer();
        this.api.resetVez();
    }
    
    public void finalizarRodada() {
        System.out.println("TODOS OS JOGADORES JOGARAM");
        this.modo = Modo.FINAL;
        this.api.fazerJogadaDealer();   // faz a jogada e notifica o(s) resultado(s)

        // distribui o dinheiro
        this.api.distribuiDinheiroJogadores();
    }
}

enum Modo {
    INICIO,     // esperando ele clicar em inicar rodada
    APOSTA,     // os jogadores apostando
    JOGANDO,    // os jogadores jogando
    FINAL       // final da rodada, distribuindo o dinheiro
}