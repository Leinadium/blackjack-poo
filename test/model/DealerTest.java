/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import org.junit.*;

/*
  Daniel Guimarães - 1910462
  Mariana Barreto - 1820673
 */
import static org.junit.Assert.*;

import model.cartas.*;

public class DealerTest {

    @Test
    public void podeHitInicial() {
        Dealer d = new Dealer();
        assertTrue("Pode Hit no inicio", d.podeHit());
    }

    @Test
    public void podeHitSegundo() {
        Dealer d = new Dealer();
        Baralho b = new Baralho(4);
        d.mao.ganharCarta(b.pop());
        assertTrue("Pode Hit na primera carta", d.podeHit());
    }

    @Test
    public void podeHitMais17() {
        Dealer d = new Dealer();
        Carta as = new Carta(Cor.PRETO, Nome.AS, Naipe.COPAS);
        Carta dez = new Carta(Cor.PRETO, Nome.DEZ, Naipe.COPAS);
        d.mao.ganharCarta(as);
        d.mao.ganharCarta(dez);
        assertFalse("Pode Hit com mais de 17 pontos", d.podeHit());
    }

    @Test
    public void podeStandInicial() {
        Dealer d = new Dealer();
        assertFalse("Pode Stand no inicio", d.podeStand());
    }

    @Test
    public void podeStandSegundo() {
        Dealer d = new Dealer();
        Baralho b = new Baralho(4);
        d.mao.ganharCarta(b.pop());

        assertFalse("Pode Stand na primeira carta", d.podeStand());
    }

    @Test
    public void podeStand16() {
        Dealer d = new Dealer();
        Carta dez = new Carta(Cor.PRETO, Nome.DEZ, Naipe.COPAS);
        Carta seis = new Carta(Cor.PRETO, Nome.SEIS, Naipe.COPAS);
        d.mao.ganharCarta(seis);
        d.mao.ganharCarta(dez);
        assertFalse("Pode Stand com 16 pontos", d.podeStand());
    }

    @Test
    public void podeStand17() {
        Dealer d = new Dealer();
        Carta dez = new Carta(Cor.PRETO, Nome.DEZ, Naipe.COPAS);
        Carta sete = new Carta(Cor.PRETO, Nome.SETE, Naipe.COPAS);
        d.mao.ganharCarta(sete);
        d.mao.ganharCarta(dez);
        assertTrue("Pode Stand com 17 pontos", d.podeStand());
    }

    @Test
    public void podeStandBlackjack() {
        Dealer d = new Dealer();
        Carta dez = new Carta(Cor.PRETO, Nome.DEZ, Naipe.COPAS);
        Carta as = new Carta(Cor.PRETO, Nome.AS, Naipe.COPAS);
        d.mao.ganharCarta(as);
        d.mao.ganharCarta(dez);
        assertFalse("Pode Stand com blackjack", d.podeStand());
    }
}
