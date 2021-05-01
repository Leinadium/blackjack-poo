package model;
import static org.junit.Assert.*;
import org.junit.Test;

import model.cartas.Carta;
import model.cartas.Baralho;

public class JogadorTest {
	public static Jogador jogador;
	@Test
	public void testTemDinheiro() {
		Jogador actual = new Jogador("Joao");
		assertTrue("O jogador tem dinheiro", actual.temDinheiro());
	}

	@Test
	public void testTerApostado() {
		Jogador actual = new Jogador("Joao");
		actual.aposta = 50;
		assertTrue("O jogador fez sua aposta", actual.terApostado());
	}

	@Test
	public void testChecaFalencia() {
		Jogador actual = new Jogador("Joao");
		actual.dinheiro = 0;
		assertTrue("O jogador esta falido", actual.checaFalencia());
	}

	@Test
	public void testFinalizarAposta() {
		fail("Not yet implemented");
	}

	@Test
	public void testAumentarAposta() {
		Jogador actual = new Jogador("Joao");
		int valor_ficha = 100;
		Ficha f = new Ficha(valor_ficha);
		int saldo_antigo = actual.dinheiro;
		actual.aumentarAposta(f);
		assertEquals("A aposta foi aumentada", saldo_antigo, actual.dinheiro-valor_ficha);
	}

	@Test
	public void testAdicionarFicha() {
		Jogador actual = new Jogador("Joao");
		int valor_ficha = 100;
		Ficha f = new Ficha(valor_ficha);
		actual.adicionarFicha(f);
		//aqui acho que voce ia mudar como ia ficar a lista entao nao terminei
	}

	@Test
	public void testRetirarFicha() {
		Jogador actual = new Jogador("Joao");
		int valor_ficha = 50;
		Ficha f = new Ficha(valor_ficha);
		actual.retirarFicha(f);
		//aqui acho que voce ia mudar como ia ficar a lista entao nao terminei
	}

	@Test
	public void testRetirarDinheiro() {
		Jogador actual = new Jogador("Joao");
		actual.retirarDinheiro(400);
		assertEquals("O dinheiro foi retirado", actual.dinheiro, 100);
	}

	@Test
	public void testPodeHitMao() {
		Jogador actual = new Jogador("Joao");
		Mao m = new Mao();
		Carta c1 = new Carta(VERMELHO, AS, COPAS);
		Carta c2 = new Carta(PRETO, VALETE, PAUS);
		m.ganharCarta(c1);
		m.ganharCarta(c2);
		assertFalse("O jogador nao pode fazer Hit por causa do Blackjack", actual.podeHit(m));
	}

	@Test
	public void testPodeHit() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testPodeStandMao() {
		Jogador actual = new Jogador("Joao");
		Mao m = new Mao();
		Carta c1 = new Carta(VERMELHO, AS, COPAS);
		Carta c2 = new Carta(PRETO, VALETE, PAUS);
		m.ganharCarta(c1);
		m.ganharCarta(c2);
		assertFalse("O jogador nao pode fazer Stand por causa do Blackjack", actual.podeStand(m));
	}

	@Test
	public void testPodeStand() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testPodeDoubleMao() {
		Jogador actual = new Jogador("Joao");
		Baralho baralho = new Baralho(52*4);
		Mao m = new Mao();
		Carta c1 = new Carta(VERMELHO, VALETE, COPAS);
		Carta c2 = new Carta(PRETO, TRES, PAUS);
		m.ganharCarta(c1);
		m.ganharCarta(c2);
		actual.fazerHit(baralho, m);
		assertFalse("O jogador nao pode fazer Double pois nao é sua primeira jogada", actual.podeDouble(m));
	}

	@Test
	public void testPodeDouble() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testPodeSplitMao() {
		Jogador actual = new Jogador("Joao");
		Mao m = new Mao();
		Carta c1 = new Carta(VERMELHO, VALETE, COPAS);
		Carta c2 = new Carta(PRETO, VALETE, PAUS);
		m.ganharCarta(c1);
		m.ganharCarta(c2);
		assertTrue("O jogador pode fazer split", actual.podeSplit(m));
	}

	@Test
	public void testPodeSplit() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFazerHitBaralhoMao() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFazerHitBaralho() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFazerStandMao() {
		Jogador actual = new Jogador("Joao");
		Mao m = new Mao();
		Carta c1 = new Carta(VERMELHO, VALETE, COPAS);
		Carta c2 = new Carta(PRETO, VALETE, PAUS);
		actual.finalizarAposta();
		actual.fazerStand(m);
		assertTrue("O jogador fez um stand", m.finalizado);
	}

	@Test
	public void testFazerStand() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFazerDoubleMaoBaralho() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFazerDoubleBaralho() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFazerSplitMaoBaralho() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFazerSplitBaralho() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFazerSurrender() {
		fail("Not yet implemented"); // TODO
	}

}
