package model;
import static org.junit.Assert.*;
import org.junit.Test;

import model.cartas.Carta;
import model.cartas.Baralho;

public class JogadorTest {
	@Test
	public void testTemDinheiro() {
		Jogador actual = new Jogador("Joao");
		assertTrue("O jogador tem dinheiro", actual.temDinheiro());
	}

	@Test
	public void testNaoTemDinheiro() {
		Jogador actual = new Jogador("Joao");
		actual.dinheiro = 0;
		assertTrue("O jogador nao tem dinheiro", actual.temDinheiro());
	}
	
		
	@Test
	public void testTerApostado() {
		Jogador actual = new Jogador("Joao");
		actual.aposta = 50;
		assertTrue("O jogador fez sua aposta", actual.terApostado());
	}
	
	@Test
	public void testNaoTerApostado() {
		Jogador actual = new Jogador("Joao");
		assertTrue("O jogador nao fez sua aposta", actual.terApostado());
	}

	@Test
	public void testChecaFalencia() {
		Jogador actual = new Jogador("Joao");
		actual.dinheiro = 0;
		assertTrue("O jogador esta falido", actual.checaFalencia());
	}
	
	@Test
	public void testChecaNaoFalencia() {
		Jogador actual = new Jogador("Joao");
		assertFalse("O jogador nao esta falido", actual.checaFalencia());
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
	public void testRetirarDinheiro() throws Exception { //eu fiquei em duvida nao era pra ser aquele expected = Exception.class? 
		Jogador actual = new Jogador("Joao");
		actual.retirarDinheiro(400);
		assertEquals("O dinheiro foi retirado", actual.dinheiro, 100);
	}

	@Test
	public void testNaoPodeHitBlackjackMao() {
		Jogador actual = new Jogador("Joao");
		Carta c1 = new Carta(Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		actual.mao.ganharCarta(c1);
		actual.mao.ganharCarta(c2);
		assertFalse("O jogador nao pode fazer Hit por causa do Blackjack", actual.podeHit(actual.mao));
	}
	
	@Test
	public void testPodeHitMao() {
		Jogador actual = new Jogador("Joao");
		Carta c1 = new Carta(Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.TRES, Naipe.PAUS);
		actual.mao.ganharCarta(c1);
		actual.mao.ganharCarta(c2);
		assertTrue("O jogador pode fazer Hit", actual.podeHit(actual.mao));
	}

	@Test
	public void testPodeHit() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testNaoPodeStandBlackjackMao() {
		Jogador actual = new Jogador("Joao");
		Carta c1 = new Carta(Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		actual.mao.ganharCarta(c1);
		actual.mao.ganharCarta(c2);
		assertFalse("O jogador nao pode fazer Stand por causa do Blackjack", actual.podeStand(actual.mao));
	}
	
	@Test
	public void testPodeStandMao() {
		Jogador actual = new Jogador("Joao");
		Carta c1 = new Carta(Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		actual.mao.ganharCarta(c1);
		actual.mao.ganharCarta(c2);
		assertTrue("O jogador pode fazer Stand", actual.podeStand(actual.mao));
	}	

	@Test
	public void testPodeStand() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testNaoPodeDoubleMao() {
		Jogador actual = new Jogador("Joao");
		Baralho baralho = new Baralho(52*4);
		Carta c1 = new Carta(Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.TRES, Naipe.PAUS);
		actual.mao.ganharCarta(c1);
		actual.mao.ganharCarta(c2);
		actual.fazerHit(baralho, m);
		assertFalse("O jogador nao pode fazer Double pois nao é sua primeira jogada", actual.podeDouble(m));
	}
	
	@Test
	public void testPodeDoubleMao() {
		Jogador actual = new Jogador("Joao");
		Baralho baralho = new Baralho(52*4);
		Carta c1 = new Carta(Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		actual.mao.ganharCarta(c1);
		assertTrue("O jogador pode fazer Double", actual.podeDouble(actual.mao));
	}

	@Test
	public void testPodeDouble() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testPodeSplitMao() {
		Jogador actual = new Jogador("Joao");
		Carta c1 = new Carta(Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		actual.mao.ganharCarta(c1);
		actual.mao.ganharCarta(c2);
		assertTrue("O jogador pode fazer split", actual.podeSplit(actual.mao));
	}
	
	@Test
	public void testNaoPodeSplitMao() {
		Jogador actual = new Jogador("Joao");
		Carta c1 = new Carta(Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.TRES, Naipe.PAUS);
		actual.mao.ganharCarta(c1);
		actual.mao.ganharCarta(c2);
		assertFalse("O jogador pode fazer split", actual.podeSplit(actual.mao));
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
		Carta c1 = new Carta(Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		actual.mao.ganharCarta(c1);
		actual.mao.ganharCarta(c2);
		actual.finalizarAposta();
		actual.fazerStand(actual.mao);
		assertEquals("O jogador fez um stand", actual.mao.finalizado); //queria tentar por ultima jogada, mas ai é privada
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
