package model;

import java.util.List;

public class Ficha {
    public int valor;
    public String cor;

    public Ficha(int valor) throws IllegalArgumentException {
        if (valor == 5) { this.cor = "branco"; }
        else if (valor == 10) { this.cor = "vermelho"; }
        else if (valor == 20) { this.cor = "verde"; }
        else if (valor == 50) { this.cor = "azul"; }
        else if (valor == 100){ this.cor = "preto"; }
        else { throw new IllegalArgumentException("Valor nao existe"); }
        this.valor = valor;
    }

    /**
     * Calcula o valor total de uma lista de fichas
     * @param lista lista de fichas
     * @return o dinheiro total associado
     */
    public static int calculaValor(List<Ficha> lista) {
        int soma = 0;
        for (Ficha ficha : lista) {
            soma += ficha.valor;
        }
        return soma;
    }
}
