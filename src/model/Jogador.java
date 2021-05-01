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
 *     void retirar_ficua(Ficha) -> remove aquela ficha do jogador, atualizando o dinheiro.
 */


class Jogador {
    public String nome;
    public Mao mao;
    public List<Mao> maosSplit;
    public List<Ficha> fichas;
    public int dinheiro;
    public int aposta;

    private boolean rendido;
    private boolean finalizado;
    private Jogada ultimaJogada;
    private int quantidadeJogadas;
    private int quantidadeSplits;

    Jogador (String nome) {
        int i;
        this.nome = nome;
        this.mao = new Mao();
        this.maosSplit = new ArrayList<>();
        this.dinheiro = 500;
        this.aposta = 0;
        this.rendido = false;
        this.finalizado = false;
        this.quantidadeJogadas = 0;
        this.ultimaJogada = null;

        // criando as fichas
        this.fichas = new ArrayList<>();
        for (i = 0; i < 2; i ++) {
            this.fichas.add(new Ficha(100));
            this.fichas.add(new Ficha(50));
        }
        for (i = 0; i < 5; i ++) {
            this.fichas.add(new Ficha(20));
            this.fichas.add(new Ficha(10));
        }
        for (i = 0; i < 8; i ++) {
            this.fichas.add(new Ficha(5));
        }

        // criando as maos split nulas
        for (i = 0; i < 2; i ++) {maosSplit.add(null);}
    }


    /**
     * Retorna se o jogador possui algum dinheiro para poder apostar
     * @return true se tiver dinheiro
     */
    public boolean temDinheiro() {
        return (this.dinheiro > 0);
    }

    /**
     * Verifica se o jogador ja tinha apostado naquela jogada
     * @return true se tiver apostado
     */
    public boolean terApostado() {
        return (this.aposta > 0);
    }

    /**
     * Verifica se o jogador faliu e nao pode mais jogar
     * @return true se o jogador tiver falido
     */
    public boolean checaFalencia() {
        return (this.finalizado && this.dinheiro < 0);
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
        this.retirarFicha(f);
        this.aposta += f.valor;
    }

    /**
     * Adiciona a ficha passada, atualizando o montante de dinheiro
     * @param f Ficha nova ficha do jogador
     */
    public void adicionarFicha(Ficha f) {
        this.dinheiro += f.valor;
        this.fichas.add(f);
    }

    /**
     * Retira uma ficha da lista do jogador, atualizando o montante de dinhneiro
     * @param f Ficha ficha a ser retirada
     * @throws IllegalStateException caso a ficha nao exista no jogador
     */
    public void retirarFicha(Ficha f) throws IllegalStateException {
        int i;
        for (i = 0; i < this.fichas.size(); i ++ ) {
            if (this.fichas.get(i).valor == f.valor) {
                this.fichas.remove(i);
                this.dinheiro -= f.valor;
                return;
            }
        }
        throw new IllegalStateException("Essa ficha nao existe");
    }

    /**
     * Retira uma quantidade de fichas do jogador equivalente aquele dinheiro
     * @param dinheiro Dinheiro a ser retirado
     * @throws Exception Erro caso nao tenha esse dinheiro.
     */
    public void retirarDinheiro(int dinheiro) throws Exception {
        // coletando as fichas a serem retiradas
        List<Ficha> fichasParaRetirar = Ficha.calculaFicha(dinheiro, this.fichas);

        // retirando as fichas
        for (Ficha f: fichasParaRetirar) {
            fichas.remove(f);
        }

        // removendo o dinheiro
        this.dinheiro -= dinheiro;
    }

    public boolean podeHit(Mao m) {
        return (!m.finalizado && this.ultimaJogada != Jogada.DOUBLE);
    }
    public boolean podeHit() { return podeHit(this.mao); }

    public boolean podeStand(Mao m) {
        return (!m.finalizado);
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
            ret = ret && m.finalizado;
        }
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
        this.retirarDinheiro(this.aposta);
        this.fazerHit(b, m);
        this.fazerStand(m);
        this.quantidadeJogadas -= 1;  // Hit e Stand adicionaram +2. Retirando 1 para ficar com +1 jogada
        this.ultimaJogada = Jogada.DOUBLE;
    }
    public void fazerDouble(Baralho b) throws Exception { this.fazerDouble(this.mao, b); }

    /**
     * Faz a jogada de SPLIT para aquela mao
     * @param m Mao a ser dividida
     * @param b Baralho para pegar as novas cartas
     */
    public void fazerSplit(Mao m, Baralho b) throws Exception{
        // achando a mao proxima mao aberta
        int pos;
        if (maosSplit.get(0) == null) {pos = 0;}
        else if (maosSplit.get(1) == null) {pos = 1;}
        else { throw new Exception("Todas as maos estao em uso"); }

        Mao nova_mao = m.fazerSplit();  // criando a nova mao
        this.maosSplit.set(pos, nova_mao); // salvando a mao na lista de maos
        this.aposta += this.aposta;    // aumentando a aposta
        this.retirarDinheiro(this.aposta);  // retirando o dinheiro da aposta do jogador

        this.fazerHit(b, m);   // adicionando uma carta em cada mao
        this.fazerHit(b, nova_mao);

        this.quantidadeJogadas -= 1; // os hits aumentaram +2 na quantidade. Retirando 1
        this.ultimaJogada = Jogada.SPLIT;
    }
    public void fazerSplit(Baralho b) throws Exception {this.fazerSplit(this.mao, b);}

    public void fazerSurrender() { /*TODO*/}
}
