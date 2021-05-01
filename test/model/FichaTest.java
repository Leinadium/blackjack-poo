package model;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FichaTest {

    @Test
    public void criacaoFichaBranco() {
        Ficha f = new Ficha(5);
        assertEquals(f.cor, "branco");
    }
    @Test
    public void criacaoFichaVermelho() {
        Ficha f = new Ficha(10);
        assertEquals(f.cor, "vermelho");
    }
    @Test
    public void criacaoFichaVerde() {
        Ficha f = new Ficha(20);
        assertEquals(f.cor, "verde");
    }
    @Test
    public void criacaoFichaAzul() {
        Ficha f = new Ficha(50);
        assertEquals(f.cor, "azul");
    }
    @Test
    public void criacaoFichaPreto() {
        Ficha f = new Ficha(100);
        assertEquals(f.cor, "preto");
    }
    @Test(expected = IllegalArgumentException.class)
    public void criacaoFichaNaoExistente() {
        Ficha f = new Ficha(101);
    }

    @Test
    public void calculaValorLista() {
        List<Ficha> lista = new ArrayList<>();
        lista.add(new Ficha(5));
        lista.add(new Ficha(10));
        lista.add(new Ficha(20));
        lista.add(new Ficha(50));
        lista.add(new Ficha(100));

        assertEquals(Ficha.calculaValor(lista), 185);
    }

    @Test
    public void calculaValorListaRepetida() {
        List<Ficha> lista = new ArrayList<>();
        lista.add(new Ficha(10));
        lista.add(new Ficha(5));
        lista.add(new Ficha(10));
        assertEquals(Ficha.calculaValor(lista), 25);
    }

    @Test(expected = Exception.class)
    public void calculaFichaNaoSuficiente() throws Exception {
        List<Ficha> lista = new ArrayList<>();
        lista.add(new Ficha(10));
        Ficha.calculaFicha(100, lista);
    }

    @Test
    public void calculaFichaCorreta() throws Exception {
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
    public void calculaFichaNaoCorreta() throws Exception {
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
}
