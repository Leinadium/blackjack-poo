/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

class Ficha {
    public int valor;
    public String cor;

    public Ficha(int valor) throws IllegalArgumentException {
        if (valor == 1) { this.cor = "roxo"; }
        else if (valor == 5)  { this.cor = "branco"; }
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

    /**
     * Calcula uma combina��o de fichas que mais aproxima ao dinheiro desejado
     * @param dinheiro Dinheiro a ser tranformado em fichas
     * @param listaPossiveis Lista de fichas disponiveis
     * @return Lista de fichas relativas ao dinheiro
     * @throws Exception Error - Se n�o houver melhor escolha
     */
    public static List<Ficha> calculaFicha(int dinheiro, List<Ficha> listaPossiveis) throws Exception{
        // Fazendo uma copia
        int i;
        List<Ficha> lista = new ArrayList<>();
        for (i = 0; i < listaPossiveis.size(); i ++) {
            lista.add(listaPossiveis.get(i));
        }

        // fazendo um sort da lista
        lista.sort(new SortByValorReverse());

        // algoritmo para achar a melhor combinacao
        // greedy -> maior para o menor.
        // se nao conseguir, tentar com o segundo maior, e assim por diante
        int diff, soma, j;
        int menorDiferenca = 100000;
        List<Ficha> melhorEscolha = null;
        List<Ficha> escolhas;

        for (i = 0; i < lista.size(); i ++) {
            escolhas = new ArrayList<>();
            soma = 0;
            for (j=i; j < lista.size(); j ++) {
                if (soma < dinheiro) {
                    soma += lista.get(j).valor;
                    escolhas.add(lista.get(j));
                }
            }
            diff = soma - dinheiro;
            if (diff >= 0 && diff < menorDiferenca) {
                melhorEscolha = escolhas;
                menorDiferenca = diff;
            } else if ((diff == menorDiferenca) && (escolhas.size() < melhorEscolha.size())) {
                melhorEscolha = escolhas;
                menorDiferenca = diff;
            }
        }

        if (melhorEscolha == null) {throw new Exception("Fichas insuficientes para o dinheiro");}
        return melhorEscolha;
    }
}

// para a ordenacao da lista
class SortByValorReverse implements Comparator<Ficha> {
    public int compare(Ficha a, Ficha b) {
        return b.valor - a.valor;
    }
}
