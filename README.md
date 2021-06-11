# Blackjack - INF1636 - 2021.1

O projeto do Blackjack foi o projeto desenvolvido para a matéria de *INF1636 - Programação Orientada a Objetos* da PUC-Rio

Esse projeto consiste em um jogo funcional de Blackjack, desenvolvido em Java, seguindo o modelo de desenvolvimento MVC.

**Autores:** 

* Daniel Guimarães - 1910462

* Mariana Barreto - 1820673

## Execução

Para executar o programa, execute o método *main* da classe *Main* , dentro do pacote *src.main*.

Para executar os testes, utilize o Suite de testes, em ```test.model.SuiteTest```.

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