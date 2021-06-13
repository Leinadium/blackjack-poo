# Blackjack - INF1636 - 2021.1

O projeto do Blackjack foi o projeto desenvolvido para a matéria de *INF1636 - Programação Orientada a Objetos* da PUC-Rio

Esse projeto consiste em um jogo funcional de Blackjack, desenvolvido em Java, seguindo o modelo de desenvolvimento MVC.

**Autores:** 

* Daniel Guimarães - 1910462

* Mariana Barreto - 1820673

## Execução

Para executar o programa, execute o método *main* da classe *Main* , dentro do pacote *src.main*.

Para executar os testes, utilize o Suite de testes, em ```test.model.SuiteTest```.

## Jogando

Ao executar o código, irá aparecer uma tela de menu. Escolha a quantidade de jogadores e inicie uma nova partida,
ou carrega uma partida anterior.

Uma partida constitui de uma tela para o Dealer e uma tela para cada jogador.

Para iniciar uma rodada, clique no botão de Iniciar Rodada na tela do Dealer.

Para cada jogador, clique nas fichas na tela do dealer para incrementar a aposta. Para diminuir, clique na pilha de fichas
na tela do jogador. Para finalizar a aposta de um jogador, clique no botão na tela do jogador. Repita esses procedimentos para cada jogador.

Após todos os jogadores apostarem, a rodada iniciará com a distribuição de cartas. Cada jogador joga somente na sua vez.

Na sua vez, o jogador pode fazer as cinco ações disponíveis para aquela mão. Se não for possível fazer aquela ação, o botão estará escuro.

Caso o jogador decida fazer uma jogada *Split*, uma nova tela se abrirá para a sua nova mão.

Depois de cada jogador fazer a sua jogada, em cada tela irá aparecer os resultados, e o dinheiro atualizado.

Antes de uma jogada, é possível salvar a partida para continuar posteriormente, clicando no botão na tela do Dealer.

Em qualquer momento do jogo, é possível sair da partida, clicando no botão na tela do Dealer, ou fechando qualquer janela.

## Código

O código foi desenvolvido na arquitetura **MVC**, ou *Model-View-Controller*:

* **Model**: Pacote contendo as "regras" do jogo, ou seja toda a funcionalidade do jogo, dos jogadores, do dealer,
das fichas, das cartas, etc. O pacote em si não executa nada.
  
* **View**: Pacote contendo a parte gráfica do jogo.

* **Controller**: Pacote contendo a parte controladora do jogo. Este pacote chama as regras para saber como jogar, o que
acontece durante a partida, e controla a parte gráfica.
  

Além disso, também foram utilizadas as seguintes arquiteturas:

* *Facade*: Para evitar o acesso direto às classes do Model, foi desenvolvido uma "api" para poder controlar essas classes,
sem causar problemas. O controller chama essa "api", e a "api" chama essas classes privadas.
  
* *Singleton*: Essa "api" é uma classe que pode ser instanciada uma única vez.

* *Observer*: As classes da parte gráfica se registram na "api" para serem notificadas cada vez que há alguma atualização
nas regras do jogo (por exemplo, um jogador recebeu uma carta na sua mão).


A relação entre as classes foi representada de uma maneira simples no arquivo ```docs/diagrama.png```

## Ferramentas utilizadas

* [Eclipse IDE](https://www.eclipse.org/eclipseide/) para desenvolvimento do projeto

* [Intellij IDEA](https://www.jetbrains.com/pt-br/idea/) para ferramentas de desenvolvimento avançadas

* [Git Bash](https://git-scm.com/downloads) e [Github Desktop]() para compartilhamento e versionamento do projeto

* [Junit 4](https://junit.org/junit4/) para framework de testes

* [Java Swing](https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html) para framework gráfico.

* [Inkscape](https://inkscape.org/pt-br/) para a criação das imagens das fichas.