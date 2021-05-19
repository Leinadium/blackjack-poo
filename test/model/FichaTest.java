/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FichaTest {

	@Test
	public void testCriacaoFichaRoxo() {
		Ficha f = new Ficha(1);
		assertEquals(f.cor, "roxo");
	}

    @Test
    public void testCriacaoFichaBranco() {
        Ficha f = new Ficha(5);
        assertEquals(f.cor, "branco");
    }
    @Test
    public void testCriacaoFichaVermelho() {
        Ficha f = new Ficha(10);
        assertEquals(f.cor, "vermelho");
    }
    @Test
    public void testCriacaoFichaVerde() {
        Ficha f = new Ficha(20);
        assertEquals(f.cor, "verde");
    }
    @Test
    public void testCriacaoFichaAzul() {
        Ficha f = new Ficha(50);
        assertEquals(f.cor, "azul");
    }
    @Test
    public void testCriacaoFichaPreto() {
        Ficha f = new Ficha(100);
        assertEquals(f.cor, "preto");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testCriacaoFichaNaoExistente() {
        Ficha f = new Ficha(101);
    }

    @Test
    public void testCalculaValorLista() {
        List<Ficha> lista = new ArrayList<>();
        int expected = 186;
        lista.add(new Ficha(1));
        lista.add(new Ficha(5));
        lista.add(new Ficha(10));
        lista.add(new Ficha(20));
        lista.add(new Ficha(50));
        lista.add(new Ficha(100));
        assertEquals(expected, Ficha.calculaValor(lista));
    }

    @Test
    public void testCalculaValorListaRepetida() {
        List<Ficha> lista = new ArrayList<>();
        lista.add(new Ficha(10));
        lista.add(new Ficha(5));
        lista.add(new Ficha(10));
        assertEquals(Ficha.calculaValor(lista), 25);
    }

    @Test(expected = Exception.class)
    public void testCalculaFichaNaoSuficiente() throws Exception {
        List<Ficha> lista = new ArrayList<>();
        lista.add(new Ficha(10));
        Ficha.calculaFicha(100, lista);
    }
    
    @Test
    public void testCalculaFichaCorreta() throws Exception {
        List<Ficha> totais = new ArrayList<>();
        List<Ficha> resposta = new ArrayList<>();
        List<Ficha> actual;
        totais.add(new Ficha(50));
        totais.add(new Ficha(20));
        totais.add(new Ficha(20));
        totais.add(new Ficha(10));
        totais.add(new Ficha(10));

        resposta.add(new Ficha(20));
        resposta.add(new Ficha(20));

        actual = Ficha.calculaFicha(40, totais);
        for (int i = 0; i < actual.size(); i++ ) {
            assertEquals("Calculando fichas com dinheiro exato " + i, resposta.get(i).valor, actual.get(i).valor);
        }
    }

    @Test
    public void testCalculaFichaNaoCorreta() throws Exception {
        List<Ficha> totais = new ArrayList<>();
        List<Ficha> resposta = new ArrayList<>();
        List<Ficha> actual;
        totais.add(new Ficha(100));
        totais.add(new Ficha(50));
        totais.add(new Ficha(20));

        resposta.add(new Ficha(50));
        resposta.add(new Ficha(20));

        actual = Ficha.calculaFicha(60, totais);
        for (int i = 0; i < actual.size(); i++ ) {
            assertEquals(resposta.get(i).valor, actual.get(i).valor);
        }
    }
    
	@Test
	public void testCalculaFichas100() {
		List<Ficha> actual = Ficha.calculaFicha(100);
		assertEquals("A lista de fichas nao corresponde ao esperado", 100, actual.get(0).valor);	
		
	}
	
	@Test
	public void testCalculaFichas50() {
		List<Ficha> actual = Ficha.calculaFicha(150);
		assertEquals("A lista de fichas nao corresponde ao esperado", 50, actual.get(1).valor);	
	}
	
	@Test
	public void testCalculaFichas20() {
		List<Ficha> actual = Ficha.calculaFicha(170);
		assertEquals("A lista de fichas nao corresponde ao esperado", 20, actual.get(2).valor);	
	}
	
	@Test
	public void testCalculaFichas10() {
		List<Ficha> actual = Ficha.calculaFicha(180);
		assertEquals("A lista de fichas nao corresponde ao esperado", 10, actual.get(3).valor);	
	}
	
	@Test
	public void testCalculaFichas5() {
		List<Ficha> actual = Ficha.calculaFicha(185);
		assertEquals("A lista de fichas nao corresponde ao esperado", 5, actual.get(4).valor);	
	}
	
	@Test
	public void testCalculaFichas1() {
		List<Ficha> actual = Ficha.calculaFicha(186);
		assertEquals("A lista de fichas nao corresponde ao esperado", 1, actual.get(5).valor);	
	}
	
	@Test
	public void testCalculaFichasMaisDeUmaIgual() {
		List<Ficha> actual = Ficha.calculaFicha(40);
		assertEquals("A lista de fichas nao corresponde ao esperado", 20, actual.get(1).valor);	
	}
    
}
