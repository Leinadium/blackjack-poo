/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controller.observer.*;
import model.cartas.*;

public class Blackjack implements ObservadoAPI {

	// Implementacao o Observer
	ArrayList<ObservadorAPI> listaObservadores;
	NotificacaoAPI notificacaoAPI;

	// implementacao do singleton
	private static Blackjack bj = null;

	// Objetos essenciais
	Baralho baralho;
	Dealer dealer;
	List<Jogador> jogadores;
	int qtdJogadores;
	protected int vez;
	protected boolean jogadoresFinalizados;

    /**
     * Implementacao do padrao singleton.
     * Retorna uma instancia de blackjack para aquela quantidade de jogadores
     * Se havia uma instancia com uma quantidade diferente de jogadores, sera sobreescrita
     * @param quantidadeJogadores quantidade de Jogadores
     * @return instancia da api (objeto Blackjack)
     */
    public static Blackjack getAPI(int quantidadeJogadores) {
        if (bj == null || bj.qtdJogadores != quantidadeJogadores) {
            bj = new Blackjack(quantidadeJogadores);
        }
        return bj;
    }


    private Blackjack(int quantidadeJogadores){
    	int i;
    	this.baralho = new Baralho(4);
    	this.dealer = new Dealer();
    	this.jogadores = new ArrayList<>();
    	this.qtdJogadores = quantidadeJogadores;
    	for (i = 0; i < quantidadeJogadores; i++) {
    		jogadores.add(new Jogador(i));
    	}
    	vez = 0;
    	jogadoresFinalizados = false;
    }

	/**
	 * Reinicia os jogadores para mais uma rodada
	 * (o dinheiro nao eh alterado)
	 */
	public void reiniciarJogadores() {
    	for (Jogador jog: this.jogadores) {
    		jog.iniciaJogada();
		}
	}

	/**
	 * Pega de qual jogador eh a vez
	 * @return
	 */
	public int getVez() {
		return vez;
	}

	/**
	 * Retorna se todos os jogadores ja jogaram
	 * Para poder descobrir quem ganhou e perdeu
	 * @return
	 */
	public boolean getJogadoresFinalizados() {
    	return jogadoresFinalizados;
	}

	/**
	 * Passa a vez para o proximo jogador.
	 * Se todos jogaram, define jogadoresFinalizados como true
	 */
	public void passaVez() {
    	vez++;
    	if (vez == jogadores.size()) {
    		jogadoresFinalizados = true;
		}
	}

	/**
	 * Exibe o baralho inteiro no sdout (para testes da implementacao)
	 */
	public void exibirBaralho() {
        this.baralho.exibirTodos();
    }

	/**
	 * Aumenta o valor da aposta do jogador de acordo com o que clicou
	 * @param valor valor da ficha
	 */
	public void aumentaAposta(int valor) {
		Jogador jog = this.jogadores.get(vez);
		Ficha f = new Ficha(valor);
		jog.aumentarAposta(f);
		notificarTodos(NotificacaoAPI.JogadorAposta);
	}

	/**
	 * Diminui o valor da aposta do jogador de acordo com o que clicou
	 */
	public void diminuiAposta(int valor) {
		Jogador jog = this.jogadores.get(vez);
		Ficha f = new Ficha(valor);
		jog.diminuirAposta(f);
		notificarTodos(NotificacaoAPI.JogadorAposta);
	}

	/**
	 * Finaliza a aposta e entra no modo normal
	 */
	public void finalizarAposta() {
		this.jogadores.get(vez).finalizarAposta();
	}


	/**
	 * Distribui duas cartas para o Dealer
	 */
	public void distribuiCartasDealer() {
    	this.dealer.mao.ganharCarta(baralho.pop());
		this.dealer.mao.ganharCarta(baralho.pop());

		notificarTodos(NotificacaoAPI.DealerCartas);
	}

	/**
	 * Distribui duas cartas para um jogador.
	 */
	public void distribuiCartasJogador(boolean split) {
		int numJogador = vez;
		List<Carta> cartas = this.jogadores.get(numJogador).mao.cartas;
		if (split) {
			try {
				this.jogadores.get(numJogador).fazerSplit(this.jogadores.get(numJogador).mao, baralho);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		else {
	    	this.jogadores.get(numJogador).mao.ganharCarta(baralho.pop());
			this.jogadores.get(numJogador).mao.ganharCarta(baralho.pop());

		}
		notificarTodos(NotificacaoAPI.JogadorCartas);
	}

	public void fazerHitJogador(int mao) {
		int numJogador = vez;
		Jogador jog = this.jogadores.get(numJogador);
		if (mao == 0) {
			jog.fazerHit(baralho);
		} else {
			jog.fazerHit(baralho, jog.maosSplit.get(mao-1));
		}
		notificarTodos(NotificacaoAPI.JogadorCartas);
	}

	/**
	 * Retorna o valor de pontos de uma mão do jogador
	 * @param numMao = 0 se mao normal, 1 se primeira mao do split, 2 se segunda mao do split
	 * @return String = com o total de pontos da mao
	 */
	public String retornaValorPontosJogador(int numMao) {
		int numJogador = vez;
		if (numMao == 0) {
			return String.format("%d", this.jogadores.get(numJogador).mao.soma);
		}
		else if (numMao == 1){
			return String.format("%d", this.jogadores.get(numJogador).maosSplit.get(0).soma);
		}
		else {
			return String.format("%d", this.jogadores.get(numJogador).maosSplit.get(1).soma);
		}
	}

	public int quantidadeMaosSplitJogador(int numJogador) { return this.jogadores.get(numJogador).maosSplit.size();}


    /**
     * Retorna o resultado de uma partida entre um jogador e um dealer
     * @param jog - Jogador.
     * @param d - Dealer.
     * @return Resultado entre os dois (DEALER, JOGADOR ou PUSH)
     */
    public Resultado verificaGanhador(Jogador jog, Dealer d) {
    	if (d.mao.blackjack) {
    		return Resultado.DEALER;
    	}
    	else if (jog.mao.blackjack) {
    		jog.recebePagamentoBlackjack();
    		return Resultado.JOGADOR;
    	}
    	else if (jog.mao.quebrado) {
    		return Resultado.DEALER;
    	}
    	else if (d.mao.quebrado) {
    		jog.dinheiro += jog.aposta;
    		return Resultado.JOGADOR;
    	}
    	else if ((21-jog.calculaMelhorValor()) < (21-d.mao.soma)) {
    		jog.dinheiro += jog.aposta;
    		return Resultado.JOGADOR;
    	}
    	else if ((21-jog.calculaMelhorValor()) > (21-d.mao.soma)) {
    		return Resultado.DEALER;
    	}
    	return Resultado.PUSH;
    }


    /* ==== FUNCOES DO OBSERVADOR ==== */

    public void registraObservador(ObservadorAPI o) {
    	if (listaObservadores == null) { listaObservadores = new ArrayList<>(); }
		listaObservadores.add(o);
    	// assim que eh registrado, ele precisa enviar as fichas no inicio
		notificarTodos(NotificacaoAPI.JogadorAposta);
	}
    public void removeObservador(ObservadorAPI o) {
    	listaObservadores.remove(o);
	}
	public NotificacaoAPI getNotificacao() {
		return notificacaoAPI;
	}
	public void notificarTodos(NotificacaoAPI n) {
    	this.notificacaoAPI = n;
    	for (ObservadorAPI o: listaObservadores) { o.notificar(this); }
    }

	public String[] getCartasDealer() {
		return this.dealer.mao.toArray();
	}
	public int getValorDealer() {
    	return this.dealer.mao.soma;
	}

	public String[] getCartasJogador(int idJogador, int mao) {
    	if (mao==0) {
    		return this.jogadores.get(idJogador).mao.toArray();
		} else {
    		return this.jogadores.get(idJogador).maosSplit.get(mao - 1).toArray();
		}
	}
	public int getValorJogador(int idJogador, int mao) {
    	if (mao==0) {
    		return this.jogadores.get(idJogador).mao.soma;
		} else {
    		return this.jogadores.get(idJogador).maosSplit.get(mao -1).soma;
		}
	}

	public HashMap<Integer, Integer> getFichasJogador(int idJogador) {
    	Jogador jog = this.jogadores.get(idJogador);
		HashMap<Integer, Integer> ret = new HashMap<>();

		for (Ficha f: jog.fichas) {
			if (ret.containsKey(f.valor)) {
				ret.put(f.valor, ret.get(f.valor) + 1);
			} else {
				ret.put(f.valor, 1);
			}
		}
		return ret;
	}
	public ArrayList<Integer> getApostaJogador(int idJogador) {
		ArrayList<Integer> ret = new ArrayList<>();
		if (this.jogadores.get(idJogador).fichasAposta == null) {
			return ret;
		}
		for (Ficha f: this.jogadores.get(idJogador).fichasAposta) {
			ret.add(f.valor);
		}
		return ret;
	}
	public int getDinheiroJogador(int idJogador) {
    	return this.jogadores.get(idJogador).dinheiro;
	}
	public int getValorApostaJogador(int idJogador) {
    	return this.jogadores.get(idJogador).aposta;
	}
	public boolean getPodeApostaJogador(int idJogador) {
    	int aposta = this.jogadores.get(idJogador).aposta;
    	return (aposta >= 20 && aposta <= 100);
	}
}
