# Regras - Blackjack

## O jogador pode apostar

* Precisa ter dinheiro

* Precisa nao ter apostado

* Ou o dealer apostou um valor maior ou igual a aposta do jogador atual

## O jogador pode jogar

* Precisa ter apostado

* Precisa nao ter quebrado

* Precisa nao ter se rendido

### Hit

* Precisa nao ter jogado um double na rodada passada.

### Stand

### Double

* Precisa nao ter jogado Hit/Stand/Surrender.

* Precisa ter fichas o suficiente para dobrar.

### Split

* Precisa ter tirado duas cartas de mesmo valor.

* Maximo de duas divisoes.

### Surrender

* Precisa nao ter jogado Hit/Stand/Surrender/Split.

## O dealer pode jogar

### Hit

* Precisa nao ter quebrado.

* Precisa ter uma soma menor que 17.

### Stand

* Ele nao pode fazer hit.

### Seguro

* Precisa estar na primeira rodada.

* A carta virada para cima do dealer precisa ser um as.

## Verifica ganhador.

### Jogador ganhou

* Precisa nao ter quebrado.

* O dealer nao tem um Blackjack.

* A soma das cartas foi maior que a do dealer e a ultima jogada de ambos foram stand

### Dealer ganhou

* O jogador se rendeu

* O dealer possui um blackjack

### Empate (Push)

* A ultima jogada do jogador e do dealer foram stand ou os dois fizeram blackjack.
