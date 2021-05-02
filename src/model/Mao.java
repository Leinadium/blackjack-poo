/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import java.util.ArrayList;
import java.util.List;
import java.lang.IllegalStateException;

import model.cartas.Carta;

/**
 * Classe Mao.
 * A mao possui as cartas que o jogador/dealer possui para jogar.
 * Metodos e atributos:
 *     List cartas -> possui uma lista de Carta
 *     int soma -> possui a soma dos valores das cartas
 *     void ganhar_carta(Carta c) -> adiciona uma carta na mao
 *     Mao dividir_mao() -> divide a mao, retornando a nova mao.
 *
 */

class Mao {
	public List<Carta> cartas;
	public int soma;
	public boolean quebrado = false;
	public boolean finalizado = false;

	/* o blackjack sera falsificado se a mao nova for splitada, ou
	* ao ter duas cartas e a soma nao for 21. */
	public boolean blackjack = true;
	
	Mao() {
		this.cartas = new ArrayList<>();
		this.soma = 0;
	}
	
	/**
	 * Faz o calculo da soma dos valores das cartas, e salva no atributo .valor
	 */
	private void calcularValor() {
		int soma = 0;
		int valor;
		int quantidadeAs = 0;
		for (Carta c: cartas) {
			valor = c.valor();
			if (valor == -1) {  // se for as, calcula depois
				quantidadeAs += 1;
			} else {
				soma += c.valor();
			}
		}
		if (quantidadeAs > 0) {
			if (soma > 10) { soma += quantidadeAs; }
			else { soma += 11 +  (quantidadeAs - 1); }
		}
		this.soma = soma;
	}
	/**
	 * Verifica se a mao quebrou
	 */
	private boolean verificaQuebrado(){
		return (this.soma > 21);
	}
	/**
	 * Verifica se tem blackjack
	 */
	private boolean verificaBlackjack(){
		return (this.soma == 21);
	}
	/**
	 * Atualiza as variaveis se a mao quebrou
	 */
	public void atualizaQuebrado(){
		if (this.verificaQuebrado()){
			this.finalizado = true;
			this.quebrado = true;
		}
	}
	/**
	 * Atualiza as variaveis se a mao tem blackjack
	 */
	public void atualizaBlackjack(){
		if (this.verificaBlackjack()){
			this.finalizado = true;
			this.blackjack = true;
		}
	}
	/**
	 * Adiciona uma nova carta na mao, recalculando o valor total e definindo blackjack/quebrado
	 * @param c Carta uma nova carta a ser adicionada
	 */
	public void ganharCarta(Carta c) {
		cartas.add(c);
		this.calcularValor();

		if (this.cartas.size() == 2 && this.soma != 21) {
			this.blackjack = false;
		}
		atualizaQuebrado(); //tem que ver isso tambem porque se quebrar mas pegar as cartas pelo baralho.pop vai dar problema
		atualizaBlackjack();
	}
	/**
	 * Faz o split da mao.
	 * @return Mao a nova mao
	 * @throws IllegalStateException Se nao houver duas cartas, ou elas forem diferentes.
	 */
	public Mao fazerSplit() throws IllegalStateException{
		if (this.cartas.size() != 2) {throw new IllegalStateException("Nao possui duas cartas."); }
		if (!this.cartas.get(0).equals(this.cartas.get(1))) {
			throw new IllegalStateException("As duas cartas sao diferentes"); 
		}
		Mao m = new Mao();
		m.ganharCarta(this.cartas.get(1));
		m.blackjack = false;   // inicia a mao split como nao tendo um blackjack (apenas 1 carta)
		this.blackjack = false;

		this.cartas.remove(1);
		
		return m;
	}

	public boolean podeSplit() {
		return (this.cartas.size() == 2 && this.cartas.get(0).equals(this.cartas.get(1))); //seriam duas funcoes de podeSplit mesmo?
	}
}
