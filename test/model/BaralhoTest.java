package model;

import static org.junit.Assert.*;

import org.junit.*;

import model.cartas.*;

public class BaralhoTest {

	@Test
	public void testPop() {
		Baralho actual = new Baralho(4);
		actual.pop();
		assertEquals("O pop nao esta funcionando", 52*4-1, actual.cartas.size());
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void testNaoPopBaralhoVazio() {
		Baralho actual = new Baralho(0);
		actual.pop();
	}

}
