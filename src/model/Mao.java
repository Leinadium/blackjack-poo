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
 *     int valor -> possui a soma dos valores das cartas
 *     void ganhar_carta(Carta c) -> adiciona uma carta na mao
 *     Mao dividir_mao() -> divide a mao, retornando a nova mao.
 * @author Daniel
 *
 */

class Mao {
	public List<Carta> cartas;
	int valor;
	
	Mao() {
		this.cartas = new ArrayList<>();
		this.valor = 0;
	}
	
	/**
	 * Faz o calculo da soma dos valores das cartas, e salva no atributo .valor
	 * @return null
	 */
	private void calcular_valor() {
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
			if (soma > 10) { soma += 1 * quantidadeAs; }
			else { soma += 11 + 1 * (quantidadeAs - 1); }
		}
		
		this.valor = soma;
		return;
	}
	/**
	 * Adiciona uma nova carta na mao, recalculando o valor total
	 * @param c Carta uma nova carta a ser adicionada
	 */
	public void ganhar_carta(Carta c) {
		cartas.add(c);
		this.calcular_valor();
		return;
	}
	/**
	 * Faz o split da mao.
	 * @return Mao a nova mao
	 * @throws IllegalStateException Se nao houver duas cartas, ou elas forem diferentes.
	 */
	public Mao fazer_split() throws IllegalStateException{
		if (this.cartas.size() != 2) {throw new IllegalStateException("Nao possui duas cartas."); }
		if (!this.cartas.get(0).equals(this.cartas.get(1))) {
			throw new IllegalStateException("As duas cartas sao diferentes"); 
		}
		
		Mao m = new Mao();
		m.ganhar_carta(this.cartas.get(1));
		this.cartas.remove(1);
		
		return m;
	}
}
