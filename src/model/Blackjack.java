/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import java.util.ArrayList;
import java.util.List;

import model.cartas.*;

public class Blackjack {
	public Baralho baralho;
	public Dealer dealer;
	public List<Jogador> jogadores;
    public Blackjack(int quantidadeJogadores){
    	int i;
    	this.baralho = new Baralho(4);
    	this.dealer = new Dealer();
    	this.jogadores = new ArrayList<Jogador>();
    	for (i = 0; i < quantidadeJogadores; i++) {
    		jogadores.add(new Jogador(i));
    	}
    }
    
    public void exibirBaralho() {
        this.baralho.exibirTodos();
    }


	/**
	 * Distribui duas cartas para o Dealer, e retorna as cartas
	 * no formato string delas ("NOME-NAIPE")
	 * @return String[] = {"NOME-NAIPE", "NOME-NAIPE"}
	 */
	public String[] distribuiCartasDealer() {
    	this.dealer.mao.ganharCarta(baralho.pop());
		this.dealer.mao.ganharCarta(baralho.pop());

		List<Carta> cartas = this.dealer.mao.cartas;
		return new String[]{cartas.get(0).toString(), cartas.get(1).toString()};
	}

	/**
	 * Distribui duas cartas para um jogador, e retorna as cartas
	 * no formato string delas ("NOME-NAIPE")
	 * @return String[] = {"NOME-NAIPE", "NOME-NAIPE"}
	 */
	public String[] distribuiCartasJogador(int numJogador, boolean split) {
		List<Carta> cartas = this.jogadores.get(numJogador).mao.cartas;
		if (split) {
			try {
				this.jogadores.get(numJogador).fazerSplit(this.jogadores.get(numJogador).mao, baralho);
				List<Carta> cartas_mao1 = this.jogadores.get(numJogador).maosSplit.get(0).cartas;
				List<Carta> cartas_mao2 = this.jogadores.get(numJogador).maosSplit.get(1).cartas;
				return new String[]{cartas_mao1.get(0).toString(), cartas_mao1.get(1).toString(),
						cartas_mao2.get(0).toString(), cartas_mao2.get(1).toString()};
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		else {
	    	this.jogadores.get(numJogador).mao.ganharCarta(baralho.pop());
			this.jogadores.get(numJogador).mao.ganharCarta(baralho.pop());
			cartas = this.jogadores.get(numJogador).mao.cartas;
			return new String[]{cartas.get(0).toString(), cartas.get(1).toString()};
		}
		return new String[]{cartas.get(0).toString(), cartas.get(1).toString()};
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
}
