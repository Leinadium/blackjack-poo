package model;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

import model.cartas.*;
import model.cartas.Baralho;

public class JogadorTest {

	private void adicionaCarta(Jogador jog,  Cor cor, Nome nome, Naipe naipe) {
		Carta c = new Carta(cor, nome, naipe);
		jog.mao.ganharCarta(c);
	}

	@Test
	public void testTemDinheiro() {
		Jogador actual = new Jogador("Joao");
		assertTrue("O jogador tem dinheiro", actual.temDinheiro());
	}

	@Test
	public void testNaoTemDinheiro() {
		Jogador actual = new Jogador("Joao");
		actual.dinheiro = 0;
		assertFalse("O jogador tem dinheiro", actual.temDinheiro());
	}

	@Test
	public void testTerApostado() {
		Jogador actual = new Jogador("Joao");
		actual.aposta = 50;
		assertTrue("O jogador nao fez sua aposta", actual.terApostado());
	}
	
	@Test
	public void testNaoTerApostado() {
		Jogador actual = new Jogador("Joao");

		assertFalse("O jogador fez sua aposta", actual.terApostado());
	}

	@Test
	public void testChecaFalencia() {
		Jogador actual = new Jogador("Joao");
		actual.finalizarAposta();
		actual.dinheiro = 0;
		assertTrue("O jogador nao esta falido", actual.checaFalencia()); //como finalizado é privado, como vou testar esse metodo? ele so vira true depois do stand
	}
	
	@Test
	public void testChecaNaoFalencia() {
		Jogador actual = new Jogador("Joao");
		assertFalse("O jogador esta falido", actual.checaFalencia());
	}

	@Test
	public void testFinalizarAposta() {
		fail("Not yet implemented"); //nessa aqui o rendido é privado ai estou pensando como testar essa metodo?
	}

	@Test
	public void testFazApostaValida() {
		Jogador actual = new Jogador("Joao");
		int valor_esperado = actual.dinheiro;
		actual.fazAposta(100);
		assertEquals("As apostas nao sao iguais", valor_esperado-100, actual.dinheiro);
	}

	@Test(expected = IllegalStateException.class)
	public void testFazApostaInvalidaMaisQue100() {
		Jogador actual = new Jogador("Joao");
		actual.fazAposta(101);
	}

	@Test(expected = IllegalStateException.class)
	public void testFazApostaInvalidaMenosQue20() {
		Jogador actual = new Jogador("Joao");
		actual.fazAposta(19);
	}

	@Test(expected = IllegalStateException.class)
	public void testFazApostaInvalidaSemDinheiro() {
		Jogador actual = new Jogador("Joao");
		actual.dinheiro = 30;
		actual.fazAposta(31);
	}

	@Test
	public void testAumentarAposta() {
		Jogador actual = new Jogador("Joao");
		int valor_ficha = 100;
		Ficha f = new Ficha(valor_ficha);
		int aposta_esperada = actual.aposta+100;
		actual.aumentarAposta(f); //eu acho que voce nao aumentaria a aposta em fichas e sim aumentaria em dinheiro
		assertEquals("A aposta nao foi aumentada", aposta_esperada, actual.aposta);
		int saldo_antigo = actual.dinheiro;
		actual.aumentarAposta(f);
		assertEquals("A aposta foi aumentada", saldo_antigo, actual.dinheiro+valor_ficha);
	}

	@Test
	public void testAdicionarFicha() {
		Jogador actual = new Jogador("Joao");
		Ficha f = new Ficha(100);
		actual.adicionarFicha(f);
		assertEquals("A ficha nao foi adicionada", f, actual.fichas.get(actual.fichas.size()-1));
	}

	@Test
	public void testRetirarFichaTamanhoReduzido() {
		Jogador actual = new Jogador("Joao");
		Ficha f = new Ficha(100);
		int tamanho_esperado = actual.fichas.size()-1;
		actual.retirarFicha(f);
		assertEquals("A ficha nao foi retirada", tamanho_esperado, actual.fichas.size());
	}

	@Test (expected = IllegalStateException.class)
	public void testRetirarFichaValorIgual() {
		Jogador actual = new Jogador("Joao");
		Ficha f1 = new Ficha(100);
		actual.retirarFicha(f1);
		actual.retirarFicha(f1);
		actual.retirarFicha(f1); //terceira ocorrencia, o jogador nao pode ter mais

	}

	@Test (expected = IllegalStateException.class)
	public void testRetirarFichaListaVazia() {
		Jogador actual = new Jogador("Joao");
		actual.fichas = new ArrayList<>();
        actual.retirarFicha(new Ficha(100));
	}

	@Test (expected = IllegalStateException.class)
	public void testRetirarFichaInexistente() {
		Jogador actual = new Jogador("Joao");
		Ficha f1 = new Ficha(100);
		Ficha f2 = new Ficha(50);
		actual.fichas = null;
		actual.adicionarFicha(f1);
		actual.retirarFicha(f2);
	}

	@Test
	public void testRetirarDinheiro() throws Exception {
		Jogador actual = new Jogador("Joao");
		actual.retirarDinheiro(400);
		assertEquals("O dinheiro nao foi retirado", actual.dinheiro, 100);
	}

	@Test (expected = Exception.class)
	public void testRetirarDinheiroSemDinheiro() throws Exception {
		Jogador actual = new Jogador("Joao");
		actual.retirarDinheiro(500);
		actual.retirarDinheiro(100);
	}

	@Test
	public void testNaoPodeHitBlackjack() {
		Jogador actual = new Jogador("Joao");
		adicionaCarta(actual, Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		adicionaCarta(actual, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		assertFalse("O jogador pode fazer Hit por causa do Blackjack", actual.podeHit(actual.mao));
	}

	@Test
	public void testPodeHit() {
		Jogador actual = new Jogador("Joao");
		adicionaCarta(actual, Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		adicionaCarta(actual, Cor.PRETO, Nome.TRES, Naipe.PAUS);
		assertTrue("O jogador pode fazer Hit", actual.podeHit());
	}

	@Test
	public void testNaoPodeStandBlackjack() {
		Jogador actual = new Jogador("Joao");
		adicionaCarta(actual, Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		adicionaCarta(actual, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		assertFalse("O jogador nao pode fazer Stand por causa do Blackjack", actual.podeStand());
	}

	@Test
	public void testPodeStand() {
		Jogador actual = new Jogador("Joao");
		adicionaCarta(actual, Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		adicionaCarta(actual, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		assertTrue("O jogador pode fazer Stand", actual.podeStand());
	}

	@Test
	public void testNaoPodeDoubleSegundaJogada() {
		Jogador actual = new Jogador("Joao");
		Baralho baralho = new Baralho(4);
		adicionaCarta(actual, Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		adicionaCarta(actual, Cor.PRETO, Nome.TRES, Naipe.PAUS);
		actual.fazerHit(baralho);
		assertFalse("O jogador nao pode fazer Double pois nao é sua primeira jogada", actual.podeDouble());
	}
	
	@Test
	public void testPodeDouble() {
		Jogador actual = new Jogador("Joao");
		adicionaCarta(actual, Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		assertTrue("O jogador pode fazer Double", actual.podeDouble(actual.mao));
	}

	@Test
	public void testPodeSplit() {
		Jogador actual = new Jogador("Joao");
		adicionaCarta(actual, Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		adicionaCarta(actual, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		assertTrue("O jogador nao pode fazer split", actual.podeSplit());
	}

	@Test
	public void testNaoPodeSplit() {
		Jogador actual = new Jogador("Joao");
		adicionaCarta(actual, Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		adicionaCarta(actual, Cor.PRETO, Nome.TRES, Naipe.PAUS);
		assertFalse("O jogador pode fazer split", actual.podeSplit());
	}

	@Test
	public void testFazerHit() {
		Jogador actual = new Jogador("Joao");
		Baralho baralho = new Baralho(4);
		Carta c1 = baralho.pop();
		Carta c2 = baralho.pop();
		actual.fazerHit(baralho);
		assertEquals("Ultima jogada nao foi um hit", Jogada.HIT, actual.retornaUltimaJogada());
	}

	@Test
	public void testFazerSplit() throws Exception {
		Jogador actual = new Jogador("Joao");
		Baralho b = new Baralho(4);
		adicionaCarta(actual, Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		adicionaCarta(actual, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		actual.fazerSplit(b);
		assertEquals("Ultima jogada nao foi um split", Jogada.SPLIT, actual.retornaUltimaJogada()); //queria tentar por ultima jogada, mas ai é privada
	}

	@Test(expected = Exception.class)
	public void testFazerDouble() throws Exception {
		Jogador actual = new Jogador("Joao");
		Baralho baralho = new Baralho(4);
		adicionaCarta(actual, Cor.VERMELHO, Nome.VALETE, Naipe.COPAS);
		adicionaCarta(actual, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		actual.fazerDouble(baralho);
		assertEquals("Ultima jogada nao foi um double", Jogada.DOUBLE, actual.retornaUltimaJogada());
	}

	@Test
	public void testFazerStand() {
		Jogador actual = new Jogador("Joao");
		Baralho b = new Baralho(4);
		Carta c1 = b.pop();
		Carta c2 = b.pop();
		actual.mao.ganharCarta(c1);
		actual.mao.ganharCarta(c2);
		actual.fazerStand(actual.mao);
		assertEquals("O stand nao foi a ultima jogada", Jogada.STAND, actual.retornaUltimaJogada());
	}

	@Test
	public void testFazerSurrenderApostaReduzida() {
		Jogador actual = new Jogador("Joao");
		int aposta_esperada = actual.aposta/2;
		adicionaCarta(actual, Cor.VERMELHO, Nome.QUATRO, Naipe.COPAS);
		adicionaCarta(actual, Cor.PRETO, Nome.QUATRO, Naipe.PAUS);
		actual.fazerSurrender(actual.mao);
		assertEquals("A aposta nao foi modificada", aposta_esperada, actual.aposta);
	}

	@Test
	public void testFazerSurrender() {
		Jogador actual = new Jogador("Joao");
		int aposta_esperada = actual.aposta/2;
		adicionaCarta(actual, Cor.VERMELHO, Nome.QUATRO, Naipe.COPAS);
		adicionaCarta(actual, Cor.PRETO, Nome.QUATRO, Naipe.PAUS);
		actual.fazerSurrender(actual.mao);
		assertEquals("O jogador nao fez um surrender", Jogada.SURRENDER, actual.retornaUltimaJogada());
	}

}
