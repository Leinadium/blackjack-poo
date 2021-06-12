/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

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
	// List<Jogador> jogadores;
	HashMap<Integer, Jogador> jogadores;
	public int qtdJogadores;
	protected int vez;
	protected Iterator<Integer> iterVez;
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
		this.jogadores = new HashMap<>();
		this.qtdJogadores = quantidadeJogadores;

		// os jogadores sao adicionados no hashmap usando um id iniciado em 0
		// pois anteriormente os jogadores estavam em um arraylist. Assim, adaptar
		// de arraylist para hashmap foi mais facil e com menos problemas

		for (i = 0; i < quantidadeJogadores; i++) {
			jogadores.put(i, new Jogador(i));
		}
		iterVez = this.jogadores.keySet().iterator();
		vez = iterVez.next();
		jogadoresFinalizados = false;
	}

	/**
	 * Remove os jogadores que nao conseguem mais apostar,
	 * ou seja, estao fora do jogo.
	 * Notifica a parte grafica caso remove alguem
	 */
	public ArrayList<Integer> removerJogadoresFalidos() {
		ArrayList<Integer> temp = new ArrayList<>();
		for (int id: jogadores.keySet()) {
			Jogador jog = jogadores.get(id);
			if (jog.checaFalencia()) {
				temp.add(id);
				// o comando abaixo causa ConcurrentModificationException
				// jogadores.remove(id);
			}
		}
		// se alguem faliu, remove e notifica
		if (temp.size() > 0) {
			for (int id: temp) {
				jogadores.remove(id);
			}
			return temp;
		}
		return null;
	}

	/**
	 * Reinicia os jogadores para mais uma rodada
	 * (o dinheiro nao eh alterado)
	 * Também embaralha o baralho se precisar
	 */
	public void reiniciarJogadores() {
    	for (Jogador jog: this.jogadores.values()) {
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
		if (!iterVez.hasNext()) {
			jogadoresFinalizados = true;
		}
		else {
			vez = iterVez.next();
		}
	}

    /**
     * Reseta a vez e os jogadoresFinalizados
     */
	public void resetVez() {
		iterVez = this.jogadores.keySet().iterator();
	    this.vez = iterVez.next();
	    this.jogadoresFinalizados = false;
    }

	/**
	 * Retorna se ainda tem algum jogador vivo para jogar
	 */
	public boolean temJogadores() {
		return this.jogadores.size() > 0;
	}

	/**
     * Define a vez de um jogador
     * @param vez a ser definida
     */
	public void defineVez(int vez) {
	    this.vez = vez;
    }

	/**
	 * Retorna quantas maos um jogador tem, similar a nivelSplit, porem vai de 0 a 3.
	 * @param id do jogador
	 */
	public int getQuantidadeMaos(int idJogador) {
		Jogador jog = this.jogadores.get(idJogador);
		if (jog.mao == null) {
			return 0;
		}
		else if (jog.maoSplit == null) {
			return 1;
		}
		else if (jog.maoSplit2 == null) {
			return 2;
		}
		return 3;
	}
	
	public void adicionaMaoJogador(int idJogador, int idMao) {
		Jogador jog = this.jogadores.get(idJogador);
		jog.adicionaMao(idMao);
	}


	/**
     * Define a aposta de um jogador.
     * A lista de fichas eh um atributo do jogador e representa em fichas a aposta da sua mao original.
     * Caso a mao seja derivada de um split, o valor de sua aposta so pode ser diferente da mao original caso tenha sido feito um double.
     * No caso em que o double eh feito, o atributo da mao chamado de apostaDobrada eh alterado para informar se a lista de fichas deve ser dobrada tambem ou nao.
     * @param idJogador - id do jogador cuja aposta sera atualizada
     * @param idMao - id da mao cuja aposta ser atualizada
     * @param aposta - valor da aposta a ser atualizado
     */
	public void defineAposta(int idJogador, int idMao, int aposta) {
		int aposta_original;
		Jogador jog = this.jogadores.get(idJogador);
		
		this.jogadores.get(idJogador).adicionaApostaMao(idMao, aposta);
		aposta_original = this.jogadores.get(idJogador).getMaoFromId(0).aposta;
		
		if (idMao == 0) {
			List<Ficha> lista_fichas = Ficha.calculaFicha(aposta_original);
			jog.fichasAposta = lista_fichas;
		}
		else if (aposta > aposta_original){
			this.jogadores.get(idJogador).getMaoFromId(idMao).apostaDobrada = true;
		}
		
		notificarTodos(NotificacaoAPI.JogadorAposta);
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
		Carta carta = new Carta(Carta.retornaCor(naipe_carta), nome_carta, naipe_carta);
		this.dealer.mao.ganharCarta(carta);
		this.baralho.removerCarta(carta);
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
	    	jog.mao.ganharCarta(baralho.pop());
			jog.mao.ganharCarta(baralho.pop());

			// para forcar alguma carta, de teste
			//jog.mao.ganharCarta(new Carta(Cor.VERMELHO, Nome.REI, Naipe.COPAS));
			//jog.mao.ganharCarta(new Carta(Cor.VERMELHO, Nome.REI, Naipe.OUROS));

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
	public void distribuiCartaJogador(int idJogador, int idMao, String nome_string, String naipe_string) {
		Jogador jog = this.jogadores.get(idJogador);
		Naipe naipe_carta = Carta.toNaipe(naipe_string);
		Nome nome_carta = Carta.toNome(nome_string);
		Carta carta = new Carta(Carta.retornaCor(naipe_carta), nome_carta, naipe_carta);
		if (idMao == 0) {
			jog.mao.ganharCarta(carta);
			jog.finalizado = jog.mao.finalizado;
		}
		else if (idMao == 1) {
			jog.maoSplit.ganharCarta(carta);
			jog.finalizado = jog.maoSplit.finalizado;
		}
		else {
			jog.maoSplit2.ganharCarta(carta);
			jog.finalizado = jog.maoSplit2.finalizado;
		}
		this.baralho.removerCarta(carta);
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

    	if (jog.retornaUltimaJogada() == Jogada.SURRENDER) {
    		return Resultado.DEALER;
		}

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
				} else {
					// pega seu dinheiro de volta + a aposta
					// a conta nao muda para uma jogada dobrada, pois a aposta aqui ja está com seu valor dobrado.
					jog.recebeDinheiro(m.aposta + m.aposta);
				}
				break;
			}
			case DEALER: {
				// verifica quando for surrender (tem que ser valido)
				if (jog.retornaUltimaJogada() == Jogada.SURRENDER && jog.validaSurrender(dealer)) {
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
		for (Jogador jog: this.jogadores.values()) {
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
    
	public boolean getRendidoJogador(int idJogador) {
		Jogador jog = this.jogadores.get(idJogador);
		return jog.rendido;
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
    	jog.fazerSurrender(jog.mao);
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
    		System.out.println("Houve algum problema");
    		System.exit(-1);
    	}
    	notificarTodos(NotificacaoAPI.JogadorAcao);
    }

    public boolean jogadorEhFinalizado() {
    	return this.jogadores.get(this.vez).finalizado;
	}

	public boolean maoEhFinalizado(int idMao) { return this.jogadores.get(this.vez).getMaoFromId(idMao).finalizado; }

	public int nivelSplitJogador() { return this.jogadores.get(this.vez).nivelSplit(); }

    /* ==== FUNCOES DO OBSERVADOR ==== */
	/* as respectivas documentacoes estao no codigo da interface*/

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
    	if (listaObservadores == null || listaObservadores.size() == 0) { return; }
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

	public int getApostaMao (int idJogador, int idMao) {
		return this.jogadores.get(idJogador).getMaoFromId(idMao).aposta;
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
