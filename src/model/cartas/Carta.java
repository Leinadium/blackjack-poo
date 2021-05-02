/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model.cartas;

// disclaimer: era para ser não-publica, mas aí não seria acessivel para classes fora de model.cartas
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
            case VALETE, REI, DAMA, DEZ -> ret = 10;
            default -> ret = 0;
        }
        return ret;
    }

    public void print() {
        System.out.println("Carta " + this.nome + " de " + this.naipe);
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o == null || this.getClass() != o.getClass()) { return false;}
    	Carta c = (Carta) o;
    	return (this.nome == c.nome);
    }
}
