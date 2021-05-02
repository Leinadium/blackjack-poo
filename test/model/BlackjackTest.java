/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import static org.junit.Assert.*;

import org.junit.Test;

import model.cartas.Carta;
import model.cartas.Cor;
import model.cartas.Naipe;
import model.cartas.Nome;

public class BlackjackTest {

	private void adicionaCartaMao(Mao m,  Cor cor, Nome nome, Naipe naipe) {
		Carta c = new Carta(cor, nome, naipe);
		m.ganharCarta(c);
	}
	
	private void criaBlackjack(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.AS, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
	}
	
	private void criaSplit(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.VALETE, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
	}
	
	private void criaMaoComumMenosPontos(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.TRES, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.TRES, Naipe.PAUS);
	}
	
	private void criaMaoComumMaisPontos(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.AS, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.QUATRO, Naipe.PAUS);
	}
	
	private void criaMaoQuebrada(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.VALETE, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		adicionaCartaMao(m, Cor.PRETO, Nome.QUATRO, Naipe.PAUS);
	}
	
	@Test
	public final void testeJogadorGanhandoComBlackjack() {
		Blackjack actual = new Blackjack(1, "Joao");
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaBlackjack(actual_jogador.mao);
		criaMaoComumMenosPontos(actual_dealer.mao);
		assertEquals("O jogador nao foi vencedor da partida", Resultado.JOGADOR, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testeJogadorGanhandoNormal() {
		Blackjack actual = new Blackjack(1, "Joao");
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaMaoComumMaisPontos(actual_jogador.mao);
		criaMaoComumMenosPontos(actual_dealer.mao);
		assertEquals("O jogador nao foi vencedor da partida", Resultado.JOGADOR, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testeJogadorGanhandoDealerQuebrando() {
		Blackjack actual = new Blackjack(1, "Joao");
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaMaoComumMaisPontos(actual_jogador.mao);
		criaMaoQuebrada(actual_dealer.mao);
		assertEquals("O jogador nao foi vencedor da partida", Resultado.JOGADOR, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testeDealerGanhandoDoisQuebrando() {
		Blackjack actual = new Blackjack(1, "Joao");
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaMaoQuebrada(actual_jogador.mao);
		criaMaoQuebrada(actual_dealer.mao);
		assertEquals("O dealer nao foi vencedor da partida", Resultado.DEALER, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testeDealerGanhandoDoisBlackjacks() {
		Blackjack actual = new Blackjack(1, "Joao");
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaBlackjack(actual_jogador.mao);
		criaBlackjack(actual_dealer.mao);
		assertEquals("O dealer nao foi vencedor da partida", Resultado.DEALER, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testePush() {
		Blackjack actual = new Blackjack(1, "Joao");
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaMaoComumMenosPontos(actual_jogador.mao);
		criaMaoComumMenosPontos(actual_dealer.mao);
		assertEquals("O empate nao aconteceu", Resultado.PUSH, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testeJogadorGanhandoComMaosSplit() throws Exception {
		Blackjack actual = new Blackjack(1, "Joao");
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaSplit(actual_jogador.mao);
		actual_jogador.fazerSplit(actual.baralho);
		criaMaoComumMenosPontos(actual_dealer.mao);
		assertEquals("O jogador ganhou com a mao com split", Resultado.JOGADOR, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testeDealerGanhandoComMaosSplit() throws Exception {
		Blackjack actual = new Blackjack(1, "Joao");
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaMaoComumMenosPontos(actual_jogador.mao);
		actual_jogador.fazerSplit(actual.baralho);
		criaMaoComumMaisPontos(actual_dealer.mao);
		assertEquals("O jogador ganhou com a mao com split", Resultado.DEALER, actual.verificaGanhador(actual_jogador, actual_dealer));
	}

}
