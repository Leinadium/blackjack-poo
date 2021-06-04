/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import java.util.ArrayList;
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
     * @return instancia da api (objeto Blackjack)
     */
    public static Blackjack getAPI() {
        if (bj == null) {
            bj = new Blackjack();
        }
        return bj;
    }


    private Blackjack(){
    }

	/**
	 * Inicia a API com todas as configuracoes
	 * @param quantidadeJogadores quantidade de jogadores para a partida
	 */
	public void iniciarBlackjack(int quantidadeJogadores) {
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
	 * @return o indice do jogador
	 */
	public int getVez() {
		return vez;
	}

	/**
	 * Retorna se todos os jogadores ja jogaram
	 * Para poder descobrir quem ganhou e perdeu
	 * @return a quantidade de jogadores finalizados
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
     * Reseta a vez e os jogadoresFinalizados
     */
	public void resetVez() {
	    this.vez = 0;
	    this.jogadoresFinalizados = false;
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

	public void fazerJogadaDealer() {
		// AI do dealer
		while (this.dealer.podeHit()) {
			this.dealer.fazerHit(this.baralho);
		}
		this.dealer.fazerStand();

		notificarTodos(NotificacaoAPI.DealerCartas);
		notificarTodos(NotificacaoAPI.JogadorResultado);
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
		if (!split) {
			Jogador jog = this.jogadores.get(numJogador);
	    	// jog.mao.ganharCarta(baralho.pop());
			// jog.mao.ganharCarta(baralho.pop());

			// para forcar alguma carta, de teste
			jog.mao.ganharCarta(new Carta(Cor.VERMELHO, Nome.REI, Naipe.COPAS));
			jog.mao.ganharCarta(new Carta(Cor.VERMELHO, Nome.REI, Naipe.OUROS));

			// vejo se fez um blackjack
			jog.finalizado = jog.mao.finalizado;
		}
		else {
			// Apos as cartas serem distribuidas no split, as condicoes de acao do jogador que fez split sao alteradas
			// Isso ja foi feito para o frame do jogador que fez a acao, porem nao pro frame da mao splitada, ja que ela nao havia sido registrada ainda
		}
		notificarTodos(NotificacaoAPI.JogadorAcao);
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
		} else {
			return String.format("%d", this.jogadores.get(numJogador).maoSplit.soma);
		}
	}
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
    
    /* ==== FUNCOES DE ACESSO PARA A VIEWS SOBRE FUNCIONALIDADE DO JOGADOR ==== */
    
    
    public void iniciarRodada() {
    	notificarTodos(NotificacaoAPI.JogadorAcao);
    }
    
    /**
     * Retorna um booleano que diz se o jogador pode fazer um stand
     * @param mao_splitada (booleano que verifica se essa eh a mao splitada do jogador ou nao)
     * @return booleano que diz se aquela mao do jogador pode fazer um stand
     */
    public boolean getPodeStand(int idJogador, boolean mao_splitada) {
    	boolean resultado;
    	Jogador jog = this.jogadores.get(idJogador);
    	if (mao_splitada) {
    		return jog.podeStand(jog.maoSplit);
    	}
    	else {
    		return jog.podeStand();
    	}
    }
    
    /**
     * Retorna um booleano que diz se o jogador pode fazer um hit
     * @param mao_splitada (booleano que verifica se essa eh a mao splitada do jogador ou nao)
     * @return booleano que diz se aquela mao do jogador pode fazer um hit
     */
    public boolean getPodeHit(int idJogador, boolean mao_splitada) {
    	boolean resultado;
    	Jogador jog = this.jogadores.get(idJogador);
    	if (mao_splitada) {
    		resultado = jog.podeHit(jog.maoSplit);
    	}
    	else {
    		resultado = jog.podeHit();
    	}
    	return (resultado);
    }
    
    /**
     * Retorna um booleano que diz se o jogador pode fazer um double
     * @param mao_splitada (booleano que verifica se essa eh a mao splitada do jogador ou nao)
     * @return booleano que diz se aquela mao do jogador pode fazer um double
     */
    public boolean getPodeDouble(int idJogador, boolean mao_splitada) {
    	boolean resultado;
    	Jogador jog = this.jogadores.get(idJogador);
    	if (mao_splitada) {
    		resultado = false;
    	}
    	else {
    		resultado = jog.podeDouble();
    	}
    	return (resultado);
    }
    
    /**
     * Retorna um booleano que diz se o jogador pode fazer um surrender
     * @param mao_splitada (booleano que verifica se essa eh a mao splitada do jogador ou nao)
     * @return booleano que diz se aquela mao do jogador pode fazer um surrender
     */
    public boolean getPodeSurrender(int idJogador, boolean mao_splitada) {
    	boolean resultado;
    	Jogador jog = this.jogadores.get(idJogador);
    	if (mao_splitada) {
    		resultado = jog.podeSurrender(jog.maoSplit);
    	}
    	else {
    		resultado = jog.podeSurrender();
    	}
    	return (resultado);
    }
    
    /**
     * Retorna um booleano que diz se o jogador pode fazer um split
     * @param mao_splitada (booleano que verifica se essa eh a mao splitada do jogador ou nao)
     * @return booleano que diz se aquela mao do jogador pode fazer um split
     */
    
    public boolean getPodeSplit(int idJogador, boolean mao_splitada) {
    	boolean resultado;
    	Jogador jog = this.jogadores.get(idJogador);
    	if (mao_splitada) {
    		resultado = false;
    	}
    	else {
    		resultado = jog.podeSplit();
    	}
    	return (resultado);
    }
    
    /* ==== FUNCOES QUE IMPLEMENTAM FUNCIONALIDADE DO JOGADOR ==== */
    
    private void fazerStandJogador(Jogador jog, int mao) {
    	if (mao == 0) {
    		jog.fazerStand();
    	}
    	else {
    		jog.fazerStand(jog.maoSplit);
    	}
    }
    
	private void fazerHitJogador(Jogador jog, int mao) {
		if (mao == 0) {
			jog.fazerHit(baralho);
		} else {
			jog.fazerHit(baralho, jog.maoSplit);
		}
		notificarTodos(NotificacaoAPI.JogadorCartas);
	}
	
	private void fazerDoubleJogador(Jogador jog) {
		try {
			jog.fazerDouble(baralho);
		} catch (Exception e) {
			e.printStackTrace();
		}
		notificarTodos(NotificacaoAPI.JogadorCartas); // recebe mais uma carta por causa do hit
		notificarTodos(NotificacaoAPI.JogadorAposta);
	}
	
	private void fazerSurrenderJogador(Jogador jog) {
    	jog.fazerSurrender(jog.mao, !jog.validaSurrender(this.dealer));
		notificarTodos(NotificacaoAPI.JogadorAposta);
	}
	
	private void fazerSplitJogador(Jogador jog) {
		try {
			jog.fazerSplit(baralho);
		} catch (Exception e) {
			// se der excecao nao faz o split
		}
	}

    public void fazerJogada(String acao, boolean mao_splitada) {
    	int numJogador = vez;
    	Jogador jog = this.jogadores.get(numJogador);
    	int mao = 0;
    	if (mao_splitada) {mao = 1;}

    	// notificarTodos(NotificacaoAPI.JogadorAcao);
    	if (acao.equals("STAND")) {
			fazerStandJogador(jog, mao);
    	}
    	else if (acao.equals("HIT")) {
    		fazerHitJogador(jog, mao);
    	}
    	else if (acao.equals("DOUBLE")) {
    		fazerDoubleJogador(jog);
    	}
    	else if (acao.equals("SURRENDER")) {
    		fazerSurrenderJogador(jog);
    	}
    	else if (acao.equals("SPLIT")) {
    		fazerSplitJogador(jog);
    	}
    	else {
    		System.out.println("Houve algum problema"); //vou mudar depois
    	}
    	notificarTodos(NotificacaoAPI.JogadorAcao);
    }

    public boolean jogadorEhFinalizado() {
    	return this.jogadores.get(this.vez).finalizado;
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
	public boolean getFinalizadoDealer() { return this.dealer.verificaFinalizadoGeral(); }

	public String[] getCartasJogador(int idJogador, int mao) {
    	if (mao==0) {
    		return this.jogadores.get(idJogador).mao.toArray();
		} else {
    		return this.jogadores.get(idJogador).maoSplit.toArray();
		}
	}
	public int getValorJogador(int idJogador, int mao) {
    	if (mao==0) {
    		return this.jogadores.get(idJogador).mao.soma;
		} else {
    		return this.jogadores.get(idJogador).maoSplit.soma;
		}
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
	public boolean getPodeApostaJogador(int idJogador) { return this.jogadores.get(idJogador).apostaValida(); }

	public String getResultado(int idJogador) {
    	Jogador jog = this.jogadores.get(idJogador);
    	return verificaGanhador(jog, this.dealer).toString();
	}
}
