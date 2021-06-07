/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model.cartas;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

// disclaimer: era para ser n�o-p�blica, mas n�o seria acessivel para classes fora de model.cartas
public class Baralho {

	public List<Carta> cartas = new ArrayList<>();
	private final int quantidade;
	private final int cartasPorBaralho = Nome.values().length * Naipe.values().length;

	/**
	 * Cria um baralho ja embaralhado considerando a quantidade de baralhos a ser juntados
	 * @param quantidade Quantidade de baralhos
	 */
	public Baralho(int quantidade) {
		Cor cor;
		Carta carta;
		for (int i = 0; i < quantidade; i ++) {
			for (Nome nome : Nome.values()) {  // iterando sobre os nomes das cartas
				for (Naipe naipe : Naipe.values()) {  // iterando sobre os naipes
					// achando a cor da carta de acordo com o naipe dela
					if ((naipe == Naipe.COPAS) || (naipe == Naipe.OUROS)) {
						cor = Cor.VERMELHO;
					} else {
						cor = Cor.PRETO;
					}
					// criando a nova carta e adicionando a lista
					carta = new Carta(cor, nome, naipe);
					cartas.add(carta);
				}
			}
		}
		// embaralhando
		Collections.shuffle(cartas);
		this.quantidade = quantidade;
		
	}

	/**
	 * Exibe todos no stout (para testes)
	 */
	public void exibirTodos() {
		for (Carta carta : cartas) {
			carta.print();
		}
	}

	/**
	 * Tira uma carta do baralho.
	 * @return	Uma carta do baralho
	 * @throws IndexOutOfBoundsException Se nao tiver nenhuma carta
	 */
	public Carta pop() throws IndexOutOfBoundsException{
		if (cartas.size() == 0) {
			throw new IndexOutOfBoundsException("Tentativa de remocao de Baralho vazio");
		}
		else {
			Carta c = cartas.get(0);
			cartas.remove(0);
			return c;
		}
	}

	/**
	 * Verifica se o baralho precisa ser reembaralhado com todas as cartas
	 * @return true se precisa
	 */
	public boolean precisaEmbaralhar() {
		return this.cartas.size() <= 9 * this.quantidade * 52 / 10;
	}

	/**
	 * Embaralha o baralho, restaurando todas as cartas ja retiradas
	 */
	public void embaralhar() {
		// reinicia o baralho
		Baralho b = new Baralho(this.quantidade);
		this.cartas = b.cartas;
	}

	/**
	 * Remove uma carta especifica do baralho.
	 * Se tiver mais de uma, remove so a primeira ocorrencia.
	 * Se nao tiver essa carta, nao altera nada.
	 * @param c Carta para retirar do baralho
	 */
	public void removerCarta(Carta c) {
		// como o equals da carta retorna true se a carta possui o mesmo nome,
		// nao eh possivel usar o ArrayList.remove(Object)

		for (int i = 0; i < cartasPorBaralho; i ++) {
			if (this.cartas.get(i).fullEquals(c)) {
				this.cartas.remove(i);
				break;
			}
		}
	}
}
