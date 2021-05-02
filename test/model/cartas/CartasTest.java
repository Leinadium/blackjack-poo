package model.cartas;

import static org.junit.Assert.*;
import org.junit.Test;

public class CartasTest {

	@Test
	public void CartaValorAs() {
		Carta c = new Carta(Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		assertEquals("Valor As",c.valor(), -1);
	}

	@Test
	public void CartaValorDois() {
		Carta c = new Carta(Cor.VERMELHO, Nome.DOIS, Naipe.COPAS);
		assertEquals("Valor Dois",c.valor(), 2);
	}
	
	@Test
	public void CartaValorTres() {
		Carta c = new Carta(Cor.VERMELHO, Nome.TRES, Naipe.COPAS);
		assertEquals("Valor Tres",c.valor(), 3);
	}
	
	@Test
	public void CartaValorQuatro() {
		Carta c = new Carta(Cor.VERMELHO, Nome.QUATRO, Naipe.COPAS);
		assertEquals("Valor Quatro",c.valor(), 4);
	}
	
	@Test
	public void CartaValorCinco() {
		Carta c = new Carta(Cor.VERMELHO, Nome.CINCO, Naipe.COPAS);
		assertEquals("Valor Cinco",c.valor(), 5);
	}
	
	@Test
	public void CartaValorSeis() {
		Carta c = new Carta(Cor.VERMELHO, Nome.SEIS, Naipe.COPAS);
		assertEquals("Valor Seis",c.valor(), 6);
	}
	
	@Test
	public void CartaValorSete() {
		Carta c = new Carta(Cor.VERMELHO, Nome.SETE, Naipe.COPAS);
		assertEquals("Valor Sete",c.valor(), 7);
	}
	
	@Test
	public void CartaValorOito() {
		Carta c = new Carta(Cor.VERMELHO, Nome.OITO, Naipe.COPAS);
		assertEquals("Valor Oito",c.valor(), 8);
	}
	
	@Test
	public void CartaValorNove() {
		Carta c = new Carta(Cor.VERMELHO, Nome.NOVE, Naipe.COPAS);
		assertEquals("Valor Nove",c.valor(), 9);
	}
	
	@Test
	public void CartaValorDez() {
		Carta c = new Carta(Cor.VERMELHO, Nome.DEZ, Naipe.COPAS);
		assertEquals("Valor Dez",c.valor(), 10);
	}
	
	@Test
	public void CartaValorValete() {
		Carta c = new Carta(Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		assertEquals("Valor Valete",c.valor(), 10);
	}
	
	@Test
	public void CartaValorDama() {
		Carta c = new Carta(Cor.VERMELHO, Nome.DAMA, Naipe.COPAS);
		assertEquals("Valor Dama",c.valor(), 10);
	}
	
	@Test
	public void CartaValorRei() {
		Carta c = new Carta(Cor.VERMELHO, Nome.REI, Naipe.COPAS);
		assertEquals("Valor Rei",c.valor(), 10);
	}
	
	@Test
	public void CartaIgual() {
		Carta c = new Carta(Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		Carta c2 = new Carta(Cor.PRETO, Nome.AS, Naipe.PAUS);
		
		assertTrue("Cartas sao iguais", c.equals(c2));
	}
	
	@Test
	public void CartaNaoIgual() {
		Carta c = new Carta(Cor.PRETO, Nome.REI, Naipe.PAUS);
		Carta c2 = new Carta(Cor.PRETO, Nome.AS, Naipe.PAUS);
		
		assertFalse("Cartas sao iguais", c.equals(c2));
	}
}
