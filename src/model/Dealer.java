/*
  Daniel Guimarães - 1910462
  Mariana Barreto - 1820673
 */
package model;

import model.cartas.*;

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
        //nao finalizado e nao pode fazer hit
        return (!this.mao.finalizado && !podeHit());
    }
    
    private boolean verificaFinalizadoGeral() {
        boolean ret = this.mao.finalizado;
        return (ret);
    }


}
