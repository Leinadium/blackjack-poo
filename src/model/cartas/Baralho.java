package model.cartas;

import java.util.List;
import java.util.ArrayList;

// disclaimer: era para ser não-publica, mas aí não seria acessivel para classes fora de model.cartas
public class Baralho {

	List<Carta> cartas = new ArrayList<>();

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
	}

	public void exibir_todos() {
		for (Carta carta : cartas) {
			carta.print();
		}
	}

}
