/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import model.cartas.Baralho;
import model.cartas.Carta;

/**
 * Classe Dealer.
 *
 * Metodos e funcoes:
 *     Mao mao -> mao do dealer
 */
class Dealer {
    public Mao mao;
    private boolean finalizado;

    Dealer () {
        this.mao = new Mao();
        this.finalizado = false;

    }

    /**
     * Reinicia a mao do dealer e se esta finalizado, para uma nova rodada
     */
    public void iniciaDealer() {
        this.mao = new Mao();
        this.finalizado = false;
    }

    /** 
    * Verifica se o dealer pode fazer um HIT
    * @return True se o dealer pode fazer HIT
    * Consta nas regras do jogo que o dealer deve fazer HIT enquanto sua soma de pontos é menor do que 17.
     */
    public boolean podeHit() {
        // nao finalizado e a quantidade total de pontos é menor do que 17
        return (!this.finalizado && this.mao.soma < 17);
    }
    /** 
    * Verifica se o dealer pode fazer um STAND
    * @return True se o dealer pode fazer STAND
    * Lembrando que, segundo a estratégia básica, a melhor jogada para qualquer soma de pontos maior ou igual que 17 é STAND
     */
    public boolean podeStand() {
        //nao finalizado, nao pode fazer hit e nao pode ter feito blackjack
        return (!this.mao.finalizado && !podeHit() && !this.mao.blackjack);
    }
    /**
     * Funcao para verificar se foi finalizado
     * @return true se a mao tiver sido finalizada
     */
    public boolean verificaFinalizadoGeral() {
       return (this.mao.finalizado);
    }
    /**
     * Faz a jogada de STAND para aquela mao;
     * Finaliza a jogada
     * @param m Mao do dealer
     */
    public void fazerStand(Mao m) {
        m.finalizado = true;
        // verifica se esse stand foi o ultimo de todas as maos
        this.finalizado = this.verificaFinalizadoGeral();
    }
    public void fazerStand() { fazerStand(this.mao); }
    /**
     * Faz a jogada de HIT para aquela mao;
     * @param m Mao do dealer
     * @param baralho Baralho
     */
    public void fazerHit(Baralho baralho, Mao m) {
        Carta c = baralho.pop();
        m.ganharCarta(c);
    }
    public void fazerHit(Baralho baralho) { this.fazerHit(baralho, this.mao); }

}
