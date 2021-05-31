/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import model.cartas.*;

import java.util.List;
import java.util.ArrayList;

import java.lang.IllegalStateException;

/**
 * Classe Jogador.
 *
 * Metodos e funcoes:
 *     String nome -> nome do jogador
 *     Mao mao -> mao principal do jogador
 *     List maos_split -> lista com as maos para quando ocorrer um split
 *     List fichas -> lista contendo todas as fichas do jogador
 *     int dinheiro -> dinheiro associado as fichas do jogador
 *
 *     boolean tem_dinheiro() -> retorna se o jogador tem dinheiro
 *     void adicionar_ficha(Ficha) -> adiciona uma ficha a lista do jogador, atualizando o dinheiro
 *     void retirar_ficha(Ficha) -> remove aquela ficha do jogador, atualizando o dinheiro.
 */


class Jogador {
    public int numero;
    public Mao mao;
    public List<Mao> maosSplit;
    public int dinheiro;
    public int aposta;
    public List<Ficha> fichasAposta;

    private boolean rendido;
    private boolean finalizado;
    private Jogada ultimaJogada;
    private int quantidadeJogadas;
    private int quantidadeSplits;

    Jogador (int numJogador) {
        int i;
        this.numero = numJogador;
        this.mao = new Mao();
        this.maosSplit = new ArrayList<>();
        this.fichasAposta = new ArrayList<>();
        this.dinheiro = 500;
        this.aposta = 0;
        this.rendido = false;
        this.finalizado = false;
        this.quantidadeJogadas = 0;
        this.ultimaJogada = null;

        // criando as maos split nulas
        for (i = 0; i < 2; i ++) {maosSplit.add(null);}
    }
    /**
     * Prepara o jogador para uma nova rodada
     */
    public void iniciaJogada() {
    	int i;
        this.mao = new Mao();
        this.maosSplit = new ArrayList<>();
        // criando as maos split nulas
        for (i = 0; i < 2; i ++) {maosSplit.add(null);}
        this.fichasAposta = new ArrayList<>();
        this.aposta = 0;
        this.rendido = false;
        this.finalizado = false;
        this.quantidadeJogadas = 0;
        this.ultimaJogada = null;

    }
    /**
     * Retorna se o jogador possui algum dinheiro para poder apostar
     * @return true se tiver dinheiro
     */
    public boolean temDinheiro() {
        return (this.dinheiro > 0);
    }

    /**
     * Verifica se a aposta atual do jogador eh valida
     * @return true se for valida
     */
    public boolean apostaValida() {
        return this.aposta >= 20 && this.aposta <= 100;
    }


    /**
     * Verifica se o jogador ja tinha apostado naquela jogada
     * @return true se tiver apostado
     */
    public boolean terApostado() {
        return (this.aposta > 0);
    }
    /**
     * Retorna se o jogador finalizou aquela jogada
     * @return true se ele tiver finalizado
     */
    public boolean retornaJogadaFinalizada() {
    	return (this.finalizado);
    }
    /**
     * Altera o estado do jogador caso tenha sido finalizada uma jogada
     * @param estado - estado (boolean) para qual deve ser alterado
     */
    public void alteraJogadaFinalizada(boolean estado) {
    	this.finalizado = estado;
    }
    

    /**
     * Verifica se o jogador faliu e nao pode mais jogar
     * @return true se o jogador tiver falido
     */
    public boolean checaFalencia() {
        return (this.finalizado && this.dinheiro <= 0); //acho que aqui eh menor ou igual porque se ele finalizou entao ele nao pode apostar mais pq faliu
    }
    /**
     * (depreciated)
     * Verifica se uma aposta pode ser feita
     * @return true se o jogador pode fazer a aposta
     */
    private boolean verificaAposta(int valor){
        return valor >= 20 && valor <= 100 && valor <= this.dinheiro;
    }
    /**
     * (depreciated)
     * Faz uma aposta a partir do dinheiro
     */
    public void fazAposta(int valor) throws IllegalStateException {
    	if (verificaAposta(valor)) {
    		this.dinheiro -= valor;
    		this.aposta = valor;
    	}
    	else {
    		throw new IllegalStateException("O jogador nao pode fazer a aposta com o valor inserido");
    	}
    }

    /**
     * Finaliza a aposta atual, e coloca pronto para a partida
     */
    public void finalizarAposta() {
        this.rendido = false;
        this.ultimaJogada = null;
        this.finalizado = false;
    }

    /**
     * Aumenta a aposta pelo valor da ficha
     * @param f Ficha para aumentar a aposta
     */
    public void aumentarAposta(Ficha f) {
        if (this.dinheiro >= f.valor) {
            this.dinheiro -= f.valor;
            this.fichasAposta.add(f);
            this.aposta += f.valor;
        }
    }

    /**
     * Diminui a aposta pelo valor da ficha
     * @param f Ficha para aumentar a posta
     */
    public void diminuirAposta(Ficha f) {
        this.dinheiro += f.valor;
        this.fichasAposta.remove(f);
        this.aposta -= f.valor;

    }

    public void retirarDinheiro(int valor) throws Exception {
        if (valor > this.dinheiro) {
            throw new Exception("O jogador nao pode retirar esse dinheiro");
        }
        this.dinheiro -= valor;
    }

    public void recebeDinheiro(int valor) {
        this.dinheiro += valor;
    }

    /**
     * Recebe pagamento caso tenha feito Blackjack
     */
    public void recebePagamentoBlackjack() {
    	this.dinheiro += (this.aposta * 3 / 2);
    }


    public boolean podeHit(Mao m) {
        return (!m.finalizado && this.ultimaJogada != Jogada.DOUBLE && !this.mao.blackjack); //duvida em relacao as maos do split
    }
    public boolean podeHit() { return podeHit(this.mao); }

    public boolean podeStand(Mao m) {
        return (!m.finalizado && !m.blackjack);
    }
    public boolean podeStand() { return podeStand(this.mao);}

    public boolean podeDouble(Mao m) {
        return(!m.finalizado &&
                (this.quantidadeJogadas == 0 || this.ultimaJogada == Jogada.SPLIT) &&
                this.dinheiro > this.aposta);
    }
    public boolean podeDouble() { return podeDouble(this.mao);}

    public boolean podeSplit(Mao m) {
        return (this.quantidadeSplits < 2 && m.podeSplit());
    }
    
    public boolean podeSplit() { return podeSplit(this.mao);}
    
    public boolean podeSurrender(Mao m) {
    	return (!m.finalizado && this.ultimaJogada == null && this.mao.cartas.size() == 2);
    }
    public boolean podeSurrender() { return podeSurrender(this.mao);}
    
    public boolean validaSurrender(Dealer d) {
    	return (!d.mao.blackjack);
    }
    
    public void fazerSurrender(Mao m) {
    	this.rendido = true;
    	this.finalizado = true;
    	this.aposta = this.aposta/2;
    	this.ultimaJogada = Jogada.SURRENDER;
    }
    public void fazerSurrender() { fazerSurrender(this.mao); }
    
    public Jogada retornaUltimaJogada() {
    	return (this.ultimaJogada);
    }

    /**
     * Faz a jogada de HIT para aquela mao.
     * Pega uma nova carta, e recalcula o valor da mao
     * @param baralho Baralho da rodada
     * @param m Mao do jogador a receber uma carta
     */
    public void fazerHit(Baralho baralho, Mao m) {
        Carta c = baralho.pop();
        m.ganharCarta(c);
        this.quantidadeJogadas += 1;
        this.ultimaJogada = Jogada.HIT;
    }
    public void fazerHit(Baralho baralho) { this.fazerHit(baralho, this.mao); }

    /**
     * Faz a jogada de STAND para aquela mao;
     * Finaliza a jogada
     * @param m Mao do jogador
     */
    public void fazerStand(Mao m) {
        m.finalizado = true;
        this.ultimaJogada = Jogada.STAND;
        // verifica se esse stand foi o ultimo de todas as maos
        this.finalizado = this.verificaFinalizadoGeral();
    }
    public void fazerStand() { fazerStand(this.mao); }

    /**
     * Funcao privada para verificar todas as maos se foram finalizadas
     * @return true se todas as maos estiverem finalizadas
     */
    private boolean verificaFinalizadoGeral() {
        boolean ret = this.mao.finalizado;
	    for (Mao m: this.maosSplit) {
	    	if (m != null) {
	    		ret = ret && m.finalizado;
	    	}
	    } //aqui teoricamente o finalizado do jogador viraria verdadeiro?
        return ret;
    }

    /**
     * Faz a jogada de DOUBLE para aquela mao
     * @param m Mao da jogada DOUBLE
     * @param b Baralho para retirar uma nova carta
     * @throws Exception Erro caso nao tenha dinheiro o suficiente
     */
    public void fazerDouble(Mao m, Baralho b) throws Exception {
        aposta += aposta;
        try {
        	this.retirarDinheiro(this.aposta);
            this.fazerHit(b, m);
            this.fazerStand(m);
            this.quantidadeJogadas -= 1;  // Hit e Stand adicionaram +2. Retirando 1 para ficar com +1 jogada
            this.ultimaJogada = Jogada.DOUBLE;
        }
        catch (Exception e) {
        	throw new Exception("O jogador nao possui dinheiro para fazer double");
        }
    }
    public void fazerDouble(Baralho b) throws Exception{
    	try {
    		this.fazerDouble(this.mao, b);
    	}
    	catch (Exception e) {
    		throw new Exception("O jogador nao possui dinheiro para fazer double");
    	}
    }

    /**
     * Faz a jogada de SPLIT para aquela mao
     * @param m Mao a ser dividida
     * @param b Baralho para pegar as novas cartas
     * @throws Exception Error se todas as maos estiverem em uso
     */
    public void fazerSplit(Mao m, Baralho b) throws Exception{
        // achando a mao proxima mao aberta
        int pos, pos2;
        if (this.maosSplit.get(0) == null) {pos = 0; pos2 = 1;}
        else if (this.maosSplit.get(1) == null) {pos = 1; pos2 = 0;}
        else { throw new Exception("Todas as maos estao em uso"); }
        Mao nova_mao = m.fazerSplit();  // criando a nova mao
        this.maosSplit.set(pos, nova_mao); // salvando a mao na lista de maos
        this.maosSplit.set(pos2, m);
        this.aposta += this.aposta;    // aumentando a aposta
        try {
        	this.retirarDinheiro(this.aposta);  // retirando o dinheiro da aposta do jogador
        }
        catch (Exception e) {
        	throw new Exception("O jogador nao possui dinheiro");
        }

        this.fazerHit(b, m);   // adicionando uma carta em cada mao
        this.fazerHit(b, nova_mao);

        this.quantidadeJogadas -= 1; // os hits aumentaram +2 na quantidade. Retirando 1
        this.ultimaJogada = Jogada.SPLIT;
    }
    public void fazerSplit(Baralho b) throws Exception { this.fazerSplit(this.mao, b); }

	/**
	 * Calcula melhor valor da mao do jogador, contando o caso de ter mais de uma mao
	 * @return soma de pontos da m�o que indica a possibilidade para o jogador
	 */
	public int calculaMelhorValor() {
		Mao mao1 = maosSplit.get(0);
		Mao mao2 = maosSplit.get(1);
		if (mao1 != null && mao2 != null) {
			if (mao1.quebrado && mao2.quebrado) {
				return mao1.soma;
			}
			else if (mao1.quebrado) {
				return mao2.soma;
			}
			else if (mao2.quebrado) {
				return mao1.soma;
			}
			else if ((21 - mao1.soma) <= (21 - mao2.soma)) {
				return mao1.soma;
			}
			return mao2.soma;
		}
		return (this.mao.soma);
	}

}
