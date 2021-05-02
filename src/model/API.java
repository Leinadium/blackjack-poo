/*
  Daniel Guimarães - 1910462
  Mariana Barreto - 1820673
 */
package model;

import model.cartas.*;

public class API {
    public static Baralho baralho;
    public static Dealer dealer;
    public static Jogador jogador;

    public static void iniciar() {
        baralho = new Baralho(2);
        dealer = new Dealer();
        // jogador = new Jogador();
    }

    public static void exibirBaralho() {
        baralho.exibirTodos();
    }
}
