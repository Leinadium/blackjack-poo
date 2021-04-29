package model;

public class Ficha {
    public int valor;
    public String cor;

    public Ficha(int valor) throws IllegalArgumentException {
        if (valor == 5) { this.cor = "branco"; }
        else if (valor == 10) { this.cor = "vermelho"; }
        else if (valor == 20) { this.cor = "verde"; }
        else if (valor == 50) { this.cor = "azul"; }
        else if (valor == 100){ this.cor = "preto"; }
        else { throw new IllegalArgumentException(); }
        this.valor = valor;
    }
}
