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

public class MaoTest {

	@Test
	public void testGanharCartaCom2Cartas() {
		Mao actual = new Mao();
		Carta c1 = new Carta(Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		actual.ganharCarta(c1);
		actual.ganharCarta(c2);
		assertEquals("A mao tem menos que duas cartas", 2, actual.cartas.size());
	}
	
	@Test
	public void testAtualizaNaoQuebradoBlackjack() {
		Mao actual = new Mao();
		Carta c1 = new Carta(Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		actual.ganharCarta(c1);
		actual.ganharCarta(c2);
		assertFalse("A mao quebrou", actual.quebrado);
	}
	
	@Test
	public void testAtualizaNaoQuebradoDoisAses() {
		Mao actual = new Mao();
		Carta c1 = new Carta(Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.AS, Naipe.PAUS);
		actual.ganharCarta(c1);
		actual.ganharCarta(c2);
		assertFalse("A mao quebrou", actual.finalizado);
	}
	
	@Test
	public void testAtualizaQuebrado() {
		Mao actual = new Mao();
		Carta c1 = new Carta(Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.DEZ, Naipe.PAUS);
		Carta c3 = new Carta(Cor.PRETO, Nome.DAMA, Naipe.PAUS);
		actual.ganharCarta(c1);
		actual.ganharCarta(c2);
		actual.ganharCarta(c3);
		assertTrue("A mao nao quebrou", actual.finalizado);
	}
	
	@Test
	public void testPodeSplit() {
		Mao actual = new Mao();
		Carta c1 = new Carta(Cor.PRETO, Nome.DEZ, Naipe.PAUS);
		Carta c2 = new Carta(Cor.PRETO, Nome.DEZ, Naipe.ESPADAS);
		actual.ganharCarta(c1);
		actual.ganharCarta(c2);
		assertTrue("Nao � poss�vel fazer split", actual.podeSplit());
	}
	
	@Test
	public void testNaoPodeSplit() {
		Mao actual = new Mao();
		Carta c1 = new Carta(Cor.PRETO, Nome.DEZ, Naipe.PAUS);
		Carta c2 = new Carta(Cor.PRETO, Nome.DOIS, Naipe.ESPADAS);
		actual.ganharCarta(c1);
		actual.ganharCarta(c2);
		assertFalse("O split pode ser realizado", actual.podeSplit());
	}
	
	@Test
	public void testNaoPodeSplitMaisDeDuasCartas() {
		Mao actual = new Mao();
		Carta c1 = new Carta(Cor.PRETO, Nome.DEZ, Naipe.PAUS);
		Carta c2 = new Carta(Cor.PRETO, Nome.DEZ, Naipe.ESPADAS);
		Carta c3 = new Carta(Cor.VERMELHO, Nome.DEZ, Naipe.OUROS);
		actual.ganharCarta(c1);
		actual.ganharCarta(c2);
		actual.ganharCarta(c3);
		assertFalse("O split pode ser realizado", actual.podeSplit());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testNaoFazSplitDiferenteDeDuasCartas() {
		Mao actual = new Mao();
		Carta c1 = new Carta(Cor.PRETO, Nome.DEZ, Naipe.PAUS);
		actual.ganharCarta(c1);
		actual.fazerSplit();
	}
	
	@Test
	public void testFazSplit() {
		Mao actual = new Mao();
		Carta c1 = new Carta(Cor.PRETO, Nome.DEZ, Naipe.PAUS);
		Carta c2 = new Carta(Cor.PRETO, Nome.DEZ, Naipe.ESPADAS);
		actual.ganharCarta(c1);
		actual.ganharCarta(c2);
		actual.fazerSplit();
		assertEquals("Nao foi realizado o split", 1, actual.cartas.size());
	}

}
