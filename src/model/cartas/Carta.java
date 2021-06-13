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
            case VALETE: ret = 10; break;
            case REI: ret = 10; break;
            case DAMA: ret = 10; break;
            case DEZ: ret = 10; break;
            default: ret = 0; break;
        }
        return ret;
    }
    
    public static Cor retornaCor(Naipe n) {
    	if (n == Naipe.COPAS || n == Naipe.OUROS) {
    		return (Cor.VERMELHO);
    	}
    	else {
    		return (Cor.PRETO);
    	} 
    }

    public void print() {
        System.out.println("Carta " + this.nome + " de " + this.naipe);
    }
    

    /**
     * Diz se o objeto eh um carta e possui o mesmo nome da carta
     */
    @Override
    public boolean equals(Object o) {
    	if (o == null || this.getClass() != o.getClass()) { return false;}
    	Carta c = (Carta) o;
    	return (this.nome == c.nome);
    }

    /**
     * Ao contrario do equals, diz se a carta eh totalmente igual a outra
     * @param c Carta para comparar
     * @return true se for totalmente igual
     */
    public boolean fullEquals(Carta c) {
        return (this.nome == c.nome &&
                this.naipe == c.naipe &&
                this.cor == c.cor
        );
    }

    public String toString() {
        return nome.toString() + "-" + naipe.toString();
    }

    public static Naipe toNaipe(String naipe) throws IllegalStateException{
    	if (naipe.equals("COPAS")) {
    		return Naipe.COPAS;
    	}
    	else if (naipe.equals("OUROS")) {
    		return Naipe.OUROS;
    	}
    	else if (naipe.equals("ESPADAS")) {
    		return Naipe.ESPADAS;
    	}
    	else if (naipe.equals("PAUS")) {
    		return Naipe.PAUS;
    	}
    	else {
    		throw new IllegalStateException("N�o existe tal naipe");
    	}
    }
    
    public static Nome toNome(String nome) {
    	Nome ret;
        switch (nome) {
            case "AS": ret = Nome.AS; break;
            case "DOIS": ret = Nome.DOIS; break;
            case "TRES": ret = Nome.TRES; break;
            case "QUATRO": ret = Nome.QUATRO; break;
            case "CINCO": ret = Nome.CINCO; break;
            case "SEIS": ret = Nome.SEIS; break;
            case "SETE": ret = Nome.SETE; break;
            case "OITO": ret = Nome.OITO; break;
            case "NOVE": ret = Nome.NOVE; break;
            case "VALETE": ret = Nome.VALETE; break;
            case "REI": ret = Nome.REI; break;
            case "DAMA": ret = Nome.DAMA; break;
            case "DEZ": ret = Nome.DEZ; break;
            default: ret = null; break;
        }
        return ret;
    }
    
}
