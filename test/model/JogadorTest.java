/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import org.junit.*;

import static org.junit.Assert.*;

import model.cartas.*;

public class JogadorTest {

	private void adicionaCartaMao(Mao m,  Cor cor, Nome nome, Naipe naipe) {
		Carta c = new Carta(cor, nome, naipe);
		m.ganharCarta(c);
	}
	
	private void criaBlackjack(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.AS, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
	}
	
	private void criaSplit(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.SETE, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.SETE, Naipe.PAUS);
	}
	
	private void criaMaoComumMenosPontos(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.TRES, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.TRES, Naipe.PAUS);
	}
	
	private void criaMaoComumMaisPontos(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.AS, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.QUATRO, Naipe.PAUS);
	}

	@Test
	public void testTemDinheiro() {
		Jogador actual = new Jogador(1);
		assertTrue("O jogador tem dinheiro", actual.temDinheiro());
	}

	@Test
	public void testNaoTemDinheiro() {
		Jogador actual = new Jogador(1);
		actual.dinheiro = 0;
		assertFalse("O jogador tem dinheiro", actual.temDinheiro());
	}

	@Test
	public void testTerApostado() {
		Jogador actual = new Jogador(1);
		actual.aposta = 50;
		assertTrue("O jogador nao fez sua aposta", actual.terApostado());
	}
	
	@Test
	public void testNaoTerApostado() {
		Jogador actual = new Jogador(1);
		assertFalse("O jogador fez sua aposta", actual.terApostado());
	}

	@Test
	public void testChecaFalencia() {
		Jogador actual = new Jogador(1);
		actual.dinheiro = 0;
		actual.alteraJogadaFinalizada(true);
		assertTrue("O jogador nao esta falido", actual.checaFalencia());
	}
	
	@Test
	public void testChecaNaoFalencia() {
		Jogador actual = new Jogador(1);
		assertFalse("O jogador esta falido", actual.checaFalencia());
	}

	@Test
	public void testFazApostaValida() {
		Jogador actual = new Jogador(1);
		int valor_esperado = actual.dinheiro;
		actual.fazAposta(100);
		assertEquals("As apostas nao sao iguais", valor_esperado-100, actual.dinheiro);
	}

	@Test(expected = IllegalStateException.class)
	public void testFazApostaInvalidaMaisQue100() {
		Jogador actual = new Jogador(1);
		actual.fazAposta(101);
	}

	@Test(expected = IllegalStateException.class)
	public void testFazApostaInvalidaMenosQue20() {
		Jogador actual = new Jogador(1);
		actual.fazAposta(19);
	}

	@Test(expected = IllegalStateException.class)
	public void testFazApostaInvalidaSemDinheiro() {
		Jogador actual = new Jogador(1);
		actual.dinheiro = 30;
		actual.fazAposta(31);
	}

	@Test
	public void testAumentarAposta() {
		Jogador actual = new Jogador(1);
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
	public void testRetirarDinheiro() throws Exception {
		Jogador actual = new Jogador(1);
		actual.retirarDinheiro(400);
		assertEquals("O dinheiro nao foi retirado", actual.dinheiro, 100);
	}

	@Test
	public void testRecebeDinheiro() {
		Jogador jog = new Jogador(1);
		jog.recebeDinheiro(1234);
		assertEquals("O dinheiro nao foi recebido", 1234+500, jog.dinheiro);
	}

	@Test (expected = Exception.class)
	public void testRetirarDinheiroSemDinheiro() throws Exception {
		Jogador actual = new Jogador(1);
		actual.retirarDinheiro(500);
		actual.retirarDinheiro(100);
	}

	@Test
	public void testNaoPodeHitBlackjack() {
		Jogador actual = new Jogador(1);
		criaBlackjack(actual.mao);
		assertFalse("O jogador pode fazer Hit por causa do Blackjack", actual.podeHit(actual.mao));
	}

	@Test
	public void testPodeHit() {
		Jogador actual = new Jogador(1);
		criaMaoComumMenosPontos(actual.mao);
		assertTrue("O jogador pode fazer Hit", actual.podeHit());
	}

	@Test
	public void testNaoPodeStandBlackjack() {
		Jogador actual = new Jogador(1);
		criaBlackjack(actual.mao);
		assertFalse("O jogador nao pode fazer Stand por causa do Blackjack", actual.podeStand());
	}

	@Test
	public void testPodeStand() {
		Jogador actual = new Jogador(1);
		criaMaoComumMaisPontos(actual.mao);
		assertTrue("O jogador pode fazer Stand", actual.podeStand());
	}

	@Test
	public void testNaoPodeDoubleSegundaJogada() {
		Jogador actual = new Jogador(1);
		Baralho baralho = new Baralho(4);
		criaMaoComumMaisPontos(actual.mao);
		actual.fazerHit(baralho);
		assertFalse("O jogador nao pode fazer Double pois nao é sua primeira jogada", actual.podeDouble());
	}
	
	@Test
	public void testPodeDouble() {
		Jogador actual = new Jogador(1);
		adicionaCartaMao(actual.mao, Cor.VERMELHO, Nome.AS, Naipe.COPAS);
		assertTrue("O jogador pode fazer Double", actual.podeDouble(actual.mao));
	}

	@Test
	public void testPodeSplit() {
		Jogador actual = new Jogador(1);
		criaSplit(actual.mao);
		assertTrue("O jogador nao pode fazer split", actual.podeSplit());
	}

	@Test
	public void testNaoPodeSplit() {
		Jogador actual = new Jogador(1);
		criaMaoComumMaisPontos(actual.mao);
		assertFalse("O jogador pode fazer split", actual.podeSplit());
	}

	@Test
	public void testFazerHit() {
		Jogador actual = new Jogador(1);
		Baralho baralho = new Baralho(4);
		baralho.pop();
		baralho.pop();
		actual.fazerHit(baralho);
		assertEquals("Ultima jogada nao foi um hit", Jogada.HIT, actual.retornaUltimaJogada());
	}

	@Test
	public void testFazerSplit() throws Exception {
		Jogador actual = new Jogador(1);
		Baralho b = new Baralho(4);
		criaSplit(actual.mao);
		actual.fazerSplit(b);
		assertEquals("Ultima jogada nao foi um split", Jogada.SPLIT, actual.retornaUltimaJogada()); //queria tentar por ultima jogada, mas ai é privada
	}

	@Test
	public void testFazerDouble() throws Exception{
		Jogador actual = new Jogador(1);
		Baralho baralho = new Baralho(4);
		criaSplit(actual.mao);
		actual.fazerDouble(baralho);
		assertEquals("Ultima jogada nao foi um double", Jogada.DOUBLE, actual.retornaUltimaJogada());
	}

	@Test
	public void testFazerStand() {
		Jogador actual = new Jogador(1);
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
		Jogador actual = new Jogador(1);
		int aposta_esperada = actual.aposta/2;
		criaMaoComumMenosPontos(actual.mao);
		actual.fazerSurrender(actual.mao);
		assertEquals("A aposta nao foi modificada", aposta_esperada, actual.aposta);
	}

	@Test
	public void testFazerSurrender() {
		Jogador actual = new Jogador(1);
		criaMaoComumMenosPontos(actual.mao);
		actual.fazerSurrender(actual.mao);
		assertEquals("O jogador nao fez um surrender", Jogada.SURRENDER, actual.retornaUltimaJogada());
	}
	
	private void exemploCalculaMelhorValorSplit(Jogador jog) throws Exception{
		Baralho baralho = new Baralho(4);
		criaSplit(jog.mao);
		jog.fazerSplit(baralho);
	}
	
	private Mao decideMelhorMao(Jogador jog){
		Mao m1 = jog.maosSplit.get(0);
		Mao m2 = jog.maosSplit.get(1);
		if (m1.soma >= m2.soma) {
			return m1;
		}
		return m2;
	}	
	
	@Test
	public void testCalculaMelhorValorMaosComSplit() throws Exception{
		Jogador actual = new Jogador(1);
		exemploCalculaMelhorValorSplit(actual);
		Mao expected_mao = decideMelhorMao(actual);
		assertEquals("O total de pontos da mao nao foi como esperado", expected_mao.soma, actual.calculaMelhorValor());
	}

}
