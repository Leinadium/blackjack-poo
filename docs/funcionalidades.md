# Funcionalidades previstas

*Ultima atualizacao:* 26/04

**Desatualizado!** As novas classes e métodos não foram adicionados ainda.

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

* *void embaralhar(): embaralha o baralho*


## Ficha

* int *valor*: o valor da ficha

* String *cor*: cor da ficha

* static int calcular_valor(List): calcula o valor de uma lista de fichas.

## Mao

* List *cartas*: lista de cartas na mao

* int *soma*: soma dos valores de cada carta

* void ganhar_carta(Carta c): adiciona uma nova carta na mao

* Mao fazer_split(): divide a mao e retorna a nova mao gerada. Levanta erros

## Jogador

* String *nome*: nome do jogador

* Mao *mao*: mao principal do jogador

* List *maos_split*: uma lista de maos para o split do jogador

* int *dinheiro*: dinheiro do jogador

* void tem_dinheiro(): retorna se tem dinheiro

* void adicionar_ficha(): adiciona a ficha passada ao jogador

* void retirar_ficha(Ficha): retira essa ficha do jogador

## Dealer