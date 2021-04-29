# Funcionalidades previstas

*Ultima atualizacao:* 26/04

## cartas

### Carta

* Cor *cor*: enum da carta.

* Nome *nome*: enum do nome da carta.

* Naipe *naipe*: enum do naipe da carta.

* int valor(): valor da carta. -1 para uma carta As

* void print(): exibe a carta no terminal

* boolean equals(): para comparar duas cartas

### Baralho

* List *cartas*: lista de cartas

* void *exibir_todos()*: exibe o baralho todo no terminal

* Carta *pop()*: retira uma carta do baralho e retorna

## Ficha

* int *valor*: o valor da ficha

* String *cor*: cor da ficha

## Mao

* List *cartas*: lista de cartas na mao

* int *soma*: soma dos valores de cada carta

* void ganhar_carta(Carta c): adiciona uma nova carta na mao

* Mao dividir_mao(): divide a mao e retorna a nova mao gerada. Levanta erros

## Jogador

## Dealer