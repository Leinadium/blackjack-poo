package model.baralho;

public class Carta {
    Cor cor;
    Nome nome;
    Naipe naipe;

    public Carta (Cor c, Nome no, Naipe na) {
        cor = c;
        nome = no;
        naipe = na;
    }

    public int valor() {
        int ret;
        switch (this.nome) {
            case AS -> ret = -1;
            case DOIS -> ret = 2;
            case TRES -> ret = 3;
            case QUATRO -> ret = 4;
            case CINCO -> ret = 5;
            case SEIS -> ret = 6;
            case SETE -> ret = 7;
            case OITO -> ret = 8;
            case NOVE -> ret = 9;
            case VALETE, REI, DAMA -> ret = 10;
            default -> ret = 0;
        }
        return ret;
    }

    public void print() {
        System.out.println("Carta " + this.nome + " de " + this.naipe);
    }
}
