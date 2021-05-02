package model.cartas;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

// disclaimer: era para ser não-pública, mas não seria acessivel para classes fora de model.cartas
public class Baralho {

	public List<Carta> cartas = new ArrayList<>();
	private final int quantidade;

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

	public void exibirTodos() {
		for (Carta carta : cartas) {
			carta.print();
		}
	}
	
	public Carta pop() throws IndexOutOfBoundsException{
		if (cartas.size() == 0) {
			throw new IndexOutOfBoundsException("Tentativa de remoção de Baralho vazio");
		}
		else {
			Carta c = cartas.get(0);
			cartas.remove(0);
	
			// checando se o baralho precisa ser reiniciado
			if (this.cartas.size() <= 9 * this.quantidade * 52 / 10) {this.embaralhar();}
	
			return c;
		}
	}

	public void embaralhar() {
		// reinicia o baralho
		Baralho b = new Baralho(this.quantidade);
		this.cartas = b.cartas;
	}
}
