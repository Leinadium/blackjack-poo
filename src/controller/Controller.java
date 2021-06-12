/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */

package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import views.*;
import model.Blackjack;

/**
 * Classe que controla o jogo. Manipula os frames e faz as chamadas para API do model.
 * Ao instanciar a classe, a tela inicial é aberta automaticamente.
 */
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
        this.frameDealer.alteraEstadoBotaoSalvar(true);
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
        String novoFrameTitulo;
        String nome = this.frameJogador.get(idJogador).nomeJogador;
    	
    	// cria nova janela
        int idNovaMao = this.api.nivelSplitJogador();
        novoFrameTitulo = nome + " - " + "SPLIT " + String.valueOf(idNovaMao);
        FrameJogador novoFrame = new FrameJogador(this, novoFrameTitulo, idJogador, idNovaMao);
    	this.frameJogador.put((idJogador + 1) * 100 + idNovaMao, novoFrame);
        this.api.registraObservador(novoFrame);
        this.api.distribuiCartasJogador(true);      // chamada somente para atualizar as cartas

        // novoFrame.iniciarRodada();   // so inicia depois que a outra mao finalizar
    }
    
    public void fazerJogada(String acao, int mao) {
    	this.api.fazerJogada(acao, mao);
    	this.frameDealer.alteraEstadoBotaoSalvar(false);
    	// se tiver que criar uma nova janela
        if (acao.equals("SPLIT")) {
            acionaSplit();
        }

        // se o jogador finalizou sua jogada
    	if (api.jogadorEhFinalizado()) {
    		System.out.println("finalizou sua jogada " + String.valueOf(api.jogadorEhFinalizado()));
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
    	int idJogador, idMao, idCarta, qtdCartas, qtdJogadores, qtdMaos;
    	String[] listaCartas;
        ArrayList<String> linhas = new ArrayList<String>();
        qtdJogadores = this.api.qtdJogadores;
        
        // salva a quantidade de jogadores
        linhas.add(String.valueOf(qtdJogadores));
        
        // salva de quem eh a vez quando o jogo foi salvo
        linhas.add(String.valueOf(this.api.getVez()));

        for (idJogador = 0; idJogador < qtdJogadores; idJogador++) {
        	String linha;
        	qtdMaos = this.api.getQuantidadeMaos(idJogador);
        	for (idMao = 0; idMao < qtdMaos; idMao++) {
        		// salva a aposta de uma mao
        		linha = "Aposta";
        		linha += ";" + String.valueOf(idJogador);
        		//linha += ";" + String.valueOf(idMao);
        		linha += ";" + String.valueOf(this.api.getApostaMao(idJogador, idMao));
        		linhas.add(linha);
        		listaCartas = this.api.getCartasJogador(idJogador, idMao);
        		for (idCarta = 0; idCarta < listaCartas.length; idCarta++) {
        			// salva cada carta que uma mao tem
            		linha = "Carta";
            		linha += ";" + String.valueOf(idJogador);
            		linha += ";" + String.valueOf(idMao);
            		linha += ";" + String.valueOf(listaCartas[idCarta]);
            		linhas.add(linha);
        		}
        	}
        	
        	// salva quantas maos aquele jogador tem
        	linha = "Split";
        	linha += ";" + String.valueOf(idJogador);
        	linha += ";" + String.valueOf(qtdMaos);
        	linhas.add(linha);
        	
        	// salva qual o saldo de dinheiro daquele jogador
        	linha = "Dinheiro";
        	linha += ";" + String.valueOf(idJogador);
        	linha += ";" + String.valueOf(this.api.getDinheiroJogador(idJogador));
        	linhas.add(linha);
        	
        	// salva o nome do jogador
        	linha = "Nome";
        	linha += ";" + String.valueOf(idJogador);
        	linha += ";" + this.frameJogador.get(idJogador).nomeJogador;
        	linhas.add(linha);
        }
        
        
        try {
			controller.Save.main(linhas);
		} catch (IOException e) {
			System.out.println("Deu erro em carregar partida! (ainda nao implementado)");
		}
    }

    public void carregarPartida() {
    	int i, j, k, vez;
    	int qtdJogadores, qtdMaos;
    	int idJogador, idMao;
    	int dinheiro, aposta;
    	String naipeCarta, nomeCarta;
    	String novoFrameTitulo;
    	String[] listaNomes, elementosCarta;
    	try {
			ArrayList<String> partida_salva = controller.Load.main(null);
			qtdJogadores = Integer.parseInt(partida_salva.get(0));
			vez = Integer.parseInt(partida_salva.get(1));
			this.frameNomes = new FrameNomes(this, qtdJogadores);
			listaNomes = new String[qtdJogadores];
			for (j = 2; j < partida_salva.size(); j++) {
				String[] listaElementos = partida_salva.get(j).split(";");
				if ((listaElementos[0]).equals("Nome")) {
					idJogador = Integer.parseInt(listaElementos[1]);
					listaNomes[idJogador] = listaElementos[2];
				}
			}
			this.iniciarPartida(qtdJogadores, listaNomes);
			this.api.defineVez(vez); // pode mexer direto mas achei melhor nao
			this.frameJogador.get(this.api.getVez()).iniciarAposta();
			this.modo = Modo.APOSTA;
			
	        for (j = 2; j < partida_salva.size(); j++) {
	        	String[] listaElementos = partida_salva.get(j).split(";");
	        	if ((listaElementos[0]).equals("Aposta")) {
	        		idJogador = Integer.parseInt(listaElementos[1]);
	        		aposta = Integer.parseInt(listaElementos[2]);
	        		this.api.defineAposta(idJogador, aposta);
	        	}
	        }
	        
			//Inicia a rodada
	        this.frameDealer.alteraEstadoBotaoSalvar(true);
	        this.frameJogador.get(api.getVez()).iniciarRodada();  // inicia o primeiro
	        this.modo = Modo.JOGANDO;
	        
	        
			for (j = 2; j < partida_salva.size(); j++) {
				String[] listaElementos = partida_salva.get(j).split(";");
				if ((listaElementos[0]).equals("Split")) {
					idJogador = Integer.parseInt(listaElementos[1]);
					qtdMaos = Integer.parseInt(listaElementos[2]);
					for (k = 0; k < qtdMaos-1; k++) {
						// adiciona nova mao na views
						novoFrameTitulo = listaNomes[idJogador] + " - " + "SPLIT"; 
						if (k > 0) {
							novoFrameTitulo += " " + String.valueOf(k+1);
						}
						FrameJogador novoFrame = new FrameJogador(this, novoFrameTitulo, idJogador, k+1);
						this.api.registraObservador(novoFrame);
						novoFrame.insereBotoes();
						// adiciona nova mao no models
						this.api.adicionaMaoJogador(idJogador);
					}
				}
			}
	        
	        
	        
	        for (i = 0; i < qtdJogadores; i++) {
	        	this.frameJogador.get(i).finalizarAposta();
	        }
	        
	        for (j = 2; j < partida_salva.size(); j++) {
	        	String[] listaElementos = partida_salva.get(j).split(";");
	        	  	
	        	if ((listaElementos[0]).equals("Dinheiro")) {
	        		idJogador = Integer.parseInt(listaElementos[1]);
	        		dinheiro = Integer.parseInt(listaElementos[2]);
	        		this.api.defineDinheiro(idJogador, dinheiro);
	        	}
	        	
				if ((listaElementos[0]).equals("Carta")) {
					idJogador = Integer.parseInt(listaElementos[1]);
					idMao = Integer.parseInt(listaElementos[2]);
					elementosCarta = listaElementos[3].split("-");
					nomeCarta = elementosCarta[0];
					naipeCarta = elementosCarta[1];
					if (idJogador > 3) { // entao eh o dealer
						this.api.distribuiCartaDealer(nomeCarta, naipeCarta);
					} else {
						this.api.distribuiCartaJogador(idJogador, idMao, nomeCarta, naipeCarta);
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
        
        this.frameDealer.alteraEstadoBotaoSalvar(true);

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
        this.frameDealer.alteraEstadoBotaoSalvar(false);
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

/**
 * Enumerador para os diferentes modo/estágios de uma rodada.
 */
enum Modo {
    INICIO,     // esperando ele clicar em inicar rodada
    APOSTA,     // os jogadores apostando
    JOGANDO,    // os jogadores jogando
    FINAL       // final da rodada, distribuindo o dinheiro
}