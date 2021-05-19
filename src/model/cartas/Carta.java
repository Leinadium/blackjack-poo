/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model.cartas;

// disclaimer: era para ser não-publica, mas aí não seria acessivel para classes fora de model.cartas
public class Carta {
    public Cor cor;
    public Nome nome;
    public Naipe naipe;

    public Carta (Cor c, Nome no, Naipe na) {
        cor = c;
        nome = no;
        naipe = na;
    }

    public int valor() {
        int ret;
        switch (this.nome) {
            case AS: ret = -1; break;
            case DOIS: ret = 2; break;
            case TRES: ret = 3; break;
            case QUATRO: ret = 4; break;
            case CINCO: ret = 5; break;
            case SEIS: ret = 6; break;
            case SETE: ret = 7; break;
            case OITO: ret = 8; break;
            case NOVE: ret = 9; break;
            case VALETE, REI, DAMA, DEZ: ret = 10; break;
            default: ret = 0; break;
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

    public String toString() {
        return nome.toString() + "-" + naipe.toString();
    }
}
