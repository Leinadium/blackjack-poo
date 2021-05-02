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
    public Blackjack(int quantidadejogadores, String n1){
    	this.baralho = new Baralho(4);
    	this.dealer = new Dealer();
    	this.jogadores = new ArrayList<>();
    	ArrayList<String> nomes = new ArrayList<>();
    	int i;
    	nomes.add(n1);
    	for (i = 0; i < quantidadejogadores; i++) {
    		this.jogadores.add(new Jogador(nomes.get(i)));
    	}
    }
    
    public void exibirBaralho() {
        this.baralho.exibirTodos();
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
    		return Resultado.JOGADOR;
    	}
    	else if (jog.mao.quebrado) {
    		return Resultado.DEALER;
    	}
    	else if (d.mao.quebrado) {
    		return Resultado.JOGADOR;
    	}
    	else if ((21-jog.calculaMelhorValor()) < (21-d.mao.soma)) {
    		return Resultado.JOGADOR;
    	}
    	else if ((21-jog.calculaMelhorValor()) > (21-d.mao.soma)) {
    		return Resultado.DEALER;
    	}
    	return Resultado.PUSH;
    }
}
