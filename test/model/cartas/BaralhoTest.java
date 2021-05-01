package model.cartas;

import org.junit.*;
import static org.junit.Assert.*;

public class BaralhoTest {

    @Test
    public void criacaoBaralho() {
        Baralho b = new Baralho(4);
        assertEquals("Testando criacao com 4", b.cartas.size(), 52 * 4);
    }

    @Test
    public void criacaoBaralho2() {
        Baralho b = new Baralho(2);
        assertEquals("Testando criacao com 2", b.cartas.size(), 52 * 2);
    }

    @Test
    public void retirandoUmaCarta() {
        Baralho b = new Baralho(2);
        b.pop();
        assertEquals("Retirando uma carta", b.cartas.size(), 52*2-1);
    }

    @Test
    public void retirandoEmbaralhando() {
        Baralho b = new Baralho(1);
        for (int i = 0; i < 6; i ++) { b.pop(); }
        assertEquals("Embaralhando depois de 10% de cartas retirada", 52, b.cartas.size());
    }
}

