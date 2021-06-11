package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import views.*;
import model.Blackjack;

public class Controller {
    FrameInicial frameInicial;
    FrameDealer frameDealer;
    FrameNomes frameNomes;
    // ArrayList<FrameJogador> frameJogador;
    HashMap<Integer, FrameJogador> frameJogador;
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
        int idNovaMao = this.api.nivelSplitJogador();
        FrameJogador novoFrame = new FrameJogador(this, numJogadorString, idJogador, idNovaMao);
    	this.frameJogador.put((idJogador + 1) * 100 + idNovaMao, novoFrame);
        this.api.registraObservador(novoFrame);
        this.api.distribuiCartasJogador(true);      // chamada somente para atualizar as cartas

        // novoFrame.iniciarRodada();   // so inicia depois que a outra mao finalizar
    }
    
    public void fazerJogada(String acao, int mao) {
    	this.api.fazerJogada(acao, mao);

    	// se tiver que criar uma nova janela
        if (acao.equals("SPLIT")) {
            acionaSplit();
        }

        // se o jogador finalizou sua jogada
    	if (api.jogadorEhFinalizado()) {
    	    passaVez();
        }
    	// se ele acabou uma mao e tem outra mao para jogar
    	else if (!api.jogadorEhFinalizado() && api.maoEhFinalizado(mao)) {
    	    // procurando a outra mao
            int vez = this.api.getVez();
            int proximaMao = mao + 1;
            for (FrameJogador fj: this.frameJogador.values()) {
                // procura por um framejogador com mao diferente de 0
                if (fj.idMao == proximaMao && fj.idJogador == vez) {
                    fj.iniciarRodada();     // libera os botoes para jogada
                    return;
                }
            }
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
    	for (FrameJogador fj: frameJogador.values()) {
    	    this.api.removeObservador(fj);
    	    fj.fechar();
        }
    	this.frameJogador = null;

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
    	String[] nomes;
    	try {
			ArrayList<String> partida_salva = controller.Load.main(null);
			quantidadeJogadores = Integer.parseInt(partida_salva.get(0));
			vez = Integer.parseInt(partida_salva.get(1));
			this.frameNomes = new FrameNomes(this, quantidadeJogadores);
			nomes = new String[quantidadeJogadores];
			for (j = 2; j < partida_salva.size(); j++) {
				String[] lista_elementos = partida_salva.get(j).split(";");
				if ((lista_elementos[0]).equals("Nome")) {
					idJogador = Integer.parseInt(lista_elementos[1]);
					nomes[idJogador] = lista_elementos[2];
				}
			}
			this.iniciarPartida(quantidadeJogadores, nomes);
			this.api.defineVez(vez); // pode mexer direto mas achei melhor nao
			this.frameJogador.get(this.api.getVez()).iniciarAposta();
			this.modo = Modo.APOSTA;
	        for (j = 2; j < partida_salva.size(); j++) {
	        	// nao consegui pensar em um nome melhor do que lista_elementos
	        	String[] lista_elementos = partida_salva.get(j).split(";");
	        	if ((lista_elementos[0]).equals("Aposta")) {
	        		idJogador = Integer.parseInt(lista_elementos[1]);
	        		aposta = Integer.parseInt(lista_elementos[2]);
	        		this.api.defineAposta(idJogador, aposta);
	        	}
	        }
	        
			// ainda tem que passar a aposta para o lado
	        this.frameJogador.get(api.getVez()).iniciarRodada();  // inicia o primeiro
	        
	        this.modo = Modo.JOGANDO;
	        // o botao ainda ta aparecendo como disabled pros outros jogadores
	        for (i = 0; i < quantidadeJogadores; i++) {
	        	this.frameJogador.get(i).finalizarAposta();
	        }
	        
	        for (j = 2; j < partida_salva.size(); j++) {
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

    /**
     * Inicia uma partida:
     *
     * Fecha os frames do menu e começa a api.
     * Cria os frames dos jogadores e do dealer.
     * Registra os frames na api como observadores.
     * Coloca o modo do jogo como INICIO
     * @param quantidadeJogadores Quantidade de jogadores para a partida
     * @param nomes Array contendo o nome dos jogadores. null para nomes genericos.
     */
    public void iniciarPartida(int quantidadeJogadores, String[] nomes) {
        // fecha o menu
        this.frameNomes.fechar();

        // inicia o jogo
        this.api = Blackjack.getAPI();
        this.api.iniciarBlackjack(quantidadeJogadores);

        // inicia os frames dos jogadores
        this.frameJogador = new HashMap<>();
        for (int i = 0; i < quantidadeJogadores; i++) {
            String nomeJogador;
            if (nomes != null) {
                nomeJogador = nomes[i];
            } else {
                nomeJogador = String.format("Jogador %d", i + 1);
            }
            this.frameJogador.put(i, new FrameJogador(this, nomeJogador, i, 0));
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

    /**
     * Overload de iniciarPartida para quando os nomes nao estao definidos
     * @param quantidadeJogadores quantidade de jogadores para a partida
     */
    public void iniciarPartida(int quantidadeJogadores) {
        iniciarPartida(quantidadeJogadores, null);
    }

    public void iniciarAposta() {
        if (this.modo == Modo.FINAL) {
            this.reiniciarRodada();
        }

        this.api.reiniciarJogadores();
        if (this.frameJogador != null) {    // caso tudo tenha sido fechado, pois os jogadores faliram
            this.frameJogador.get(this.api.getVez()).iniciarAposta();
        }
        this.modo = Modo.APOSTA;
    }

    /**
     * Remove o frame daquele jogador
     */
    private void removerJogador(int idJogador) {
        FrameJogador f = this.frameJogador.get(idJogador);
        this.api.removeObservador(f);
        f.fechar();
        this.frameJogador.remove(idJogador);
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
        HashMap<Integer, FrameJogador> copia = new HashMap<>(frameJogador);

        // desliga as maos de split, e nao copia as maos de split para a copia
        for (int id: this.frameJogador.keySet()) {
            FrameJogador fj = this.frameJogador.get(id);
            if (fj.idMao != 0) {
                fj.fechar();    // fecha a janela
                this.api.removeObservador(fj);
            } else {
                fj.reiniciarJogador();  // reinicia as propriedades
                copia.put(id, fj);
            }
        }
        // reescrevendo a lista de frameJogadores, agora as maos split sumiram
        this.frameJogador = copia;

        // removendo jogadores que faliram
        ArrayList<Integer> temp = this.api.removerJogadoresFalidos();
        if (temp != null) {
            for (int id: temp) { removerJogador(id); }
        }
        // verificando se sobrou alguem
        if (!this.api.temJogadores()) {
            this.fecharPartida();
            return;
        }

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

    public void iniciarEscolhaNomes(int quantidadeJogadores) {
        this.frameInicial.fechar();
        this.frameNomes = new FrameNomes(this, quantidadeJogadores);
        this.frameNomes.abrir();
    }
}

enum Modo {
    INICIO,     // esperando ele clicar em inicar rodada
    APOSTA,     // os jogadores apostando
    JOGANDO,    // os jogadores jogando
    FINAL       // final da rodada, distribuindo o dinheiro
}