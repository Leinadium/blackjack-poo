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

enum Jogada {
    HIT, STAND, DOUBLE, SPLIT
}


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
    }


    /**
     * Retorna se o jogador possui algum dinheiro para poder apostar
     * @return true se tiver dinheiro
     */
    public boolean temDinheiro() {
        return (this.dinheiro > 0);
    }

    /**
     * Verifica se o jogado ja tinha apostado naquela jogada
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
        return (this.finalizado && this.dinheiro == 0);
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
     * Verifica se o jogador pode fazer um HIT
     * @return true se o jogador pode fazer HIT
     */
    public boolean podeHit() {
        // nao finalizado, e nao jogou um double na ultima jogada
        return (!this.finalizado && this.ultimaJogada != Jogada.DOUBLE);
    }

    public boolean podeStand(Mao m) {
        return (!m.finalizado);
    }
    public boolean podeStand() { return podeStand(this.mao);}


    public boolean podeDouble(Mao m) {
        return(!m.finalizado && this.quantidadeJogadas == 0 && this.dinheiro > this.aposta);
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
}
