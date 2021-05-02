/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import org.junit.*;
import static org.junit.Assert.*;

import model.cartas.*;

public class DealerTest {
	
	private void adicionaCartaMao(Mao m,  Cor cor, Nome nome, Naipe naipe) {
		Carta c = new Carta(cor, nome, naipe);
		m.ganharCarta(c);
	}
	
    @Test
    public void podeHitInicial() {
        Dealer actual = new Dealer();
        assertTrue("Pode Hit no inicio", actual.podeHit());
    }

    @Test
    public void podeHitSegundo() {
        Dealer actual = new Dealer();
        Baralho b = new Baralho(4);
        actual.mao.ganharCarta(b.pop());
        assertTrue("Pode Hit na primera carta", actual.podeHit());
    }

    @Test
    public void podeHitMais17() {
        Dealer actual = new Dealer();
        adicionaCartaMao(actual.mao, Cor.PRETO, Nome.AS, Naipe.PAUS);
        adicionaCartaMao(actual.mao, Cor.PRETO, Nome.DEZ, Naipe.ESPADAS);
        assertFalse("Pode Hit com mais de 17 pontos", actual.podeHit());
    }

    @Test
    public void podeStandInicial() {
        Dealer actual = new Dealer();
        assertFalse("Pode Stand no inicio", actual.podeStand());
    }

    @Test
    public void podeStandSegundo() {
        Dealer actual = new Dealer();
        Baralho b = new Baralho(4);
        actual.mao.ganharCarta(b.pop());

        assertFalse("Pode Stand na primeira carta", actual.podeStand());
    }

    @Test
    public void podeStand16() {
        Dealer actual = new Dealer();
        adicionaCartaMao(actual.mao, Cor.PRETO, Nome.DEZ, Naipe.PAUS);
        adicionaCartaMao(actual.mao, Cor.PRETO, Nome.SEIS, Naipe.ESPADAS);
        assertFalse("Pode Stand com 16 pontos", actual.podeStand());
    }

    @Test
    public void podeStand17() {
        Dealer actual = new Dealer();
        adicionaCartaMao(actual.mao, Cor.PRETO, Nome.DEZ, Naipe.PAUS);
        adicionaCartaMao(actual.mao, Cor.PRETO, Nome.SETE, Naipe.PAUS);
        assertTrue("Pode Stand com 17 pontos", actual.podeStand());
    }

    @Test
    public void podeStandBlackjack() {
        Dealer actual = new Dealer();
        adicionaCartaMao(actual.mao, Cor.PRETO, Nome.DEZ, Naipe.PAUS);
        adicionaCartaMao(actual.mao, Cor.PRETO, Nome.AS, Naipe.PAUS);
        assertFalse("Pode Stand com blackjack", actual.podeStand());
    }
    
	@Test
	public void testFazerStand() {
		Dealer actual = new Dealer();
		Baralho b = new Baralho(4);
		Carta c1 = b.pop();
		Carta c2 = b.pop();
		actual.mao.ganharCarta(c1);
		actual.mao.ganharCarta(c2);
		actual.fazerStand(actual.mao);
		assertTrue("Nao foi feita uma jogada", actual.verificaFinalizadoGeral());
	}
}
