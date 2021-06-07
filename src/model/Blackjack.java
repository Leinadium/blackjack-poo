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
	 * Também embaralha o baralho se precisar
	 */
	public void reiniciarJogadores() {
    	for (Jogador jog: this.jogadores) {
    		jog.iniciaJogada();
		}
    	notificarTodos(NotificacaoAPI.JogadorAposta);
    	if (baralho.precisaEmbaralhar()) { baralho.embaralhar(); }
	}

	/**
	 * Reinicia o dealer para mais uma rodada
	 */
	public void reiniciarDealer() {
		this.dealer.iniciaDealer();
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
     * Define a vez de um jogador
     * @param vez a ser definida
     */
	public void defineVez(int vez) {
	    this.vez = vez;
    }

	public void defineAposta(int valor) {
		int i;
		List<Ficha> lista_fichas = Ficha.calculaFicha(valor);
		for (i = 0; i < lista_fichas.size(); i++) {
			this.aumentaAposta(lista_fichas.get(i).valor);
		}
	}
	
	public void defineDinheiro(int numJogador, int dinheiro) {
		Jogador jog = this.jogadores.get(numJogador);
		jog.dinheiro = dinheiro;
		notificarTodos(NotificacaoAPI.JogadorAposta);
	}
	
	/**
	 * Aumenta o valor da aposta do jogador atual de acordo com o que clicou.
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
	 * Distribui uma cartas específica para o dealer.
	 */
	public void distribuiCartaDealer(String nome_string, String naipe_string) {
		Naipe naipe_carta = Carta.toNaipe(naipe_string);
		Nome nome_carta = Carta.toNome(nome_string);
		this.dealer.mao.ganharCarta(new Carta(Carta.retornaCor(naipe_carta), nome_carta, naipe_carta));
		notificarTodos(NotificacaoAPI.DealerCartas);
	}

	/**
	 * Distribui duas cartas para um jogador.
	 * Caso "split" seja true, como as cartas ja foram distribuidas,
	 * chama uma notificacao para a parte grafica atualizar as cartas
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
			notificarTodos(NotificacaoAPI.JogadorAposta);
		}
		notificarTodos(NotificacaoAPI.JogadorAcao);
		notificarTodos(NotificacaoAPI.JogadorCartas);
	}
	
	/**
	 * Distribui uma cartas específica para um jogador.
	 */
	public void distribuiCartaJogador(int idJogador, String nome_string, String naipe_string) {
		Jogador jog = this.jogadores.get(idJogador);
		Naipe naipe_carta = Carta.toNaipe(naipe_string);
		Nome nome_carta = Carta.toNome(nome_string);
		Carta carta = new Carta(Carta.retornaCor(naipe_carta), nome_carta, naipe_carta);
		jog.mao.ganharCarta(carta);
		
		// ve se fez um blackjack
		jog.finalizado = jog.mao.finalizado;
		
		notificarTodos(NotificacaoAPI.JogadorAcao); // as cartas acabam influenciando em quais acoes estao disponiveis
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
    public Resultado verificaGanhador(Jogador jog, int idMao, Dealer d) {
    	Mao m;
    	if (idMao == 0) { m = jog.mao; }
    	else { m = jog.maoSplit; }

    	if (d.mao.blackjack) {
    		return Resultado.DEALER;
    	}
    	else if (m.blackjack) {
    		// jog.recebePagamentoBlackjack();
    		return Resultado.JOGADOR;
    	}
    	else if (m.quebrado) {
    		return Resultado.DEALER;
    	}
    	else if (d.mao.quebrado) {
    		return Resultado.JOGADOR;
    	}
    	else if ((21-m.soma) < (21-d.mao.soma)) {
    		return Resultado.JOGADOR;
    	}
    	else if ((21-m.soma) > (21-d.mao.soma)) {
    		return Resultado.DEALER;
    	}
    	return Resultado.PUSH;
    }
    /** overload para quando a mao nao for especificada */
    public Resultado verificaGanhador(Jogador jog, Dealer d) { return verificaGanhador(jog, 0, d); }

	/**
	 * De acordo com o resultado (se ganhou, perdeu, empate), distribui o dinheiro
	 * @param jog Jogador a receber o dinheiro
	 * @param res Resultado do jogador
	 * @param idMao Id da mao do jogador
	 */
	private void distribuiDinheiro(Jogador jog, Resultado res, int idMao) {
    	Mao m = jog.getMaoFromId(idMao);
		switch (res) {
			case JOGADOR: {
				if (m.blackjack) {
					// recebe o pagamento do blackjack
					jog.recebeDinheiro(m.aposta);		// ganha o dinheiro de volta
					jog.recebePagamentoBlackjack();		// ganha o bonus
				} else if (jog.retornaUltimaJogada() == Jogada.DOUBLE) {
					// pega seu dinheiro de volta + a aposta com 200%
					jog.recebeDinheiro(m.aposta + m.aposta * 2);
				} else {
					// pega seu dinheiro de volta + a aposta
					jog.recebeDinheiro(m.aposta + m.aposta);
				}
				break;
			}
			case DEALER: {
				if (jog.retornaUltimaJogada() == Jogada.SURRENDER) {
					// recebe metade da aposta de volta
					jog.recebeDinheiro(m.aposta / 2);
				}
				// qualquer outra coisa, ele nao ganha mais nada
				break;
			}
			case PUSH: {
				// ganha seu dinheiro de volta
				jog.recebeDinheiro(m.aposta);
				break;
			}
		}
	}
	/** overload para quando a mao nao for especificada */
	private void distribuiDinheiro(Jogador jog, Resultado res) { distribuiDinheiro(jog, res, 0); }

	public void distribuiDinheiroJogadores() {
		for (Jogador jog: this.jogadores) {
			// distribui para o resultado dele
			distribuiDinheiro(jog, verificaGanhador(jog, this.dealer));
			// distribui para a segunda mao, caso tenha
			if (jog.maoSplit != null) {
				distribuiDinheiro(jog, verificaGanhador(jog, 1, this.dealer), 1);
			}
			if (jog.maoSplit2 != null) {
			    distribuiDinheiro(jog, verificaGanhador(jog, 2, this.dealer), 2);
            }
		}
		notificarTodos(NotificacaoAPI.JogadorAposta);
	}

    /* ==== FUNCOES DE ACESSO PARA A VIEWS SOBRE FUNCIONALIDADE DO JOGADOR ==== */
    
    
    public void iniciarRodada() {
    	notificarTodos(NotificacaoAPI.JogadorAcao);
    }
    
    /**
     * Retorna um booleano que diz se o jogador pode fazer um stand
     * @param mao id da mao
     * @return booleano que diz se aquela mao do jogador pode fazer um stand
     */
    public boolean getPodeStand(int idJogador, int mao) {
    	Jogador jog = this.jogadores.get(idJogador);
		Mao m = jog.getMaoFromId(mao);
		return jog.podeStand(m);
    }
    
    /**
     * Retorna um booleano que diz se o jogador pode fazer um hit
     * @param mao id da mao
     * @return booleano que diz se aquela mao do jogador pode fazer um hit
     */
    public boolean getPodeHit(int idJogador, int mao) {
    	Jogador jog = this.jogadores.get(idJogador);
		Mao m = jog.getMaoFromId(mao);
		return jog.podeHit(m);
    }
    
    /**
     * Retorna um booleano que diz se o jogador pode fazer um double
     * @param mao id da mao
     * @return booleano que diz se aquela mao do jogador pode fazer um double
     */
    public boolean getPodeDouble(int idJogador, int mao) {
    	Jogador jog = this.jogadores.get(idJogador);
    	Mao m = jog.getMaoFromId(mao);

    	return jog.podeDouble(m);
    }
    
    /**
     * Retorna um booleano que diz se o jogador pode fazer um surrender
     * @param mao id da mao
     * @return booleano que diz se aquela mao do jogador pode fazer um surrender
     */
    public boolean getPodeSurrender(int idJogador, int mao) {
    	Jogador jog = this.jogadores.get(idJogador);
		Mao m = jog.getMaoFromId(mao);
		return jog.podeSurrender(m);
    }
    
    /**
     * Retorna um booleano que diz se o jogador pode fazer um split
     * @param mao id da mao
     * @return booleano que diz se aquela mao do jogador pode fazer um split
     */
    
    public boolean getPodeSplit(int idJogador, int mao) {
    	Jogador jog = this.jogadores.get(idJogador);
		Mao m = jog.getMaoFromId(mao);
		return jog.podeSplit(m);
    }
    
    /* ==== FUNCOES QUE IMPLEMENTAM FUNCIONALIDADE DO JOGADOR ==== */
    
    private void fazerStandJogador(Jogador jog, int mao) {
    	Mao m = jog.getMaoFromId(mao);
		jog.fazerStand(m);
    }
    
	private void fazerHitJogador(Jogador jog, int mao) {
    	Mao m = jog.getMaoFromId(mao);
		jog.fazerHit(baralho, m);
		notificarTodos(NotificacaoAPI.JogadorCartas);
	}
	
	private void fazerDoubleJogador(Jogador jog, int mao) {
		Mao m = jog.getMaoFromId(mao);
    	try {
			jog.fazerDouble(m, baralho);
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
	
	private void fazerSplitJogador(Jogador jog, int mao) {
		Mao m = jog.getMaoFromId(mao);
		try {
			jog.fazerSplit(m, baralho);
		} catch (Exception e) {
			// se der excecao nao faz o split
		}
	}

    public void fazerJogada(String acao, int idMao) {
    	int numJogador = vez;
    	Jogador jog = this.jogadores.get(numJogador);

    	// notificarTodos(NotificacaoAPI.JogadorAcao);
    	if (acao.equals("STAND")) {
			fazerStandJogador(jog, idMao);
    	}
    	else if (acao.equals("HIT")) {
    		fazerHitJogador(jog, idMao);
    	}
    	else if (acao.equals("DOUBLE")) {
    		fazerDoubleJogador(jog, idMao);
    	}
    	else if (acao.equals("SURRENDER")) {
    		fazerSurrenderJogador(jog);
    	}
    	else if (acao.equals("SPLIT")) {
    		fazerSplitJogador(jog, idMao);
    	}
    	else {
    		System.out.println("Houve algum problema"); //vou mudar depois
    	}
    	notificarTodos(NotificacaoAPI.JogadorAcao);
    }

    public boolean jogadorEhFinalizado() {
    	return this.jogadores.get(this.vez).finalizado;
	}

	public boolean maoEhFinalizado(int idMao) { return this.jogadores.get(this.vez).getMaoFromId(idMao).finalizado; }

	public int nivelSplitJogador() { return this.jogadores.get(this.vez).nivelSplit(); }

    /* ==== FUNCOES DO OBSERVADOR ==== */

    public void registraObservador(ObservadorAPI o) {
    	if (listaObservadores == null) { listaObservadores = new ArrayList<>(); }
		listaObservadores.add(o);
    	// assim que eh registrado, ele precisa enviar as fichas no inicio
		// notificarTodos(NotificacaoAPI.JogadorAposta);
	}
    public void removeObservador(ObservadorAPI o) {
    	listaObservadores.remove(o);
	}
	public NotificacaoAPI getNotificacao() {
		return notificacaoAPI;
	}
	public void notificarTodos(NotificacaoAPI n) {
    	if (listaObservadores == null) { return; }
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
    	return this.jogadores.get(idJogador).getMaoFromId(mao).toArray();
	}
	public int getValorJogador(int idJogador, int mao) {
		return this.jogadores.get(idJogador).getMaoFromId(mao).soma;
	}

	public ArrayList<Integer> getApostaJogador(int idJogador, int mao) {
		Jogador jog = this.jogadores.get(idJogador);
		Mao m = jog.getMaoFromId(mao);

		// inicia a lista de retorno
    	ArrayList<Integer> ret = new ArrayList<>();
		if (jog.fichasAposta == null) {
			return ret;
		}

		List<Ficha> listaFichas;
		// pegando a lista de fichas do jogador
		if (m.apostaDobrada) {		// se a lista deve ser dobrada
			listaFichas = jog.getFichasApostaDouble();
		} else {
			listaFichas = jog.fichasAposta;
		}
		// adiciona os valores de cada ficha na lista de retorno
		for (Ficha f: listaFichas) {
			ret.add(f.valor);
		}
		return ret;
	}
	public int getDinheiroJogador(int idJogador) {
    	return this.jogadores.get(idJogador).dinheiro;
	}
	public int getValorApostaJogador(int idJogador, int mao) {
    	return this.jogadores.get(idJogador).getMaoFromId(mao).aposta;
	}
	public boolean getPodeApostaJogador(int idJogador) { return this.jogadores.get(idJogador).apostaValida(); }

	public String getResultado(int idJogador, int idMao) {
    	Jogador jog = this.jogadores.get(idJogador);
    	return verificaGanhador(jog, idMao, this.dealer).toString();
	}
}
