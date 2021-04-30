package model;

import java.util.List;
import java.util.ArrayList;


class Jogador {
    String nome;
    Mao mao;
    Mao mao_split;
    List<Ficha> fichas;
    int dinheiro;

    Jogador (String nome) {
        int i;
        this.nome = nome;
        this.mao = new Mao();
        this.mao_split = new Mao();
        this.dinheiro = 500;

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


}
