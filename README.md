# Blackjack - INF1636 - 2021.1

**Autores:** 

* Daniel Guimarães - 1910462

* Mariana Barreto - 1820673

## Primeira Iteração

### Código

Os arquivos de código estão na pasta ```/src```. 

Dentro da pasta contém o componente **model**, que possui as regras e funcionalidades das principais classes do Blackjack,
como *Dealer*, *Jogador*, *Cartas*, *Ficha*, *Mão*, etc.

Além disso, há uma classe *API*, que a princípio seria a classe wrapper para que o componente futuro *Controller* usaria
para acessar as classes protegidas. Porém, ele é só um protótipo, não foi terminado nem muito implementado.

Além do **model**, há um pacote **main** que possui uma classe *Main*, que foi usada para rápidos testes de componentes.
Neste momento, o último teste foi para verificar se o baralho foi iniciado corretamente.

### Testes

Os arquivos para teste estão na pasta ```/test```.

Eles seguem a mesma estrutura da pasta de códigos, porém com os testes.

Para a entrega, há dois testes que ainda não foram completados com sucesso, mas há o planejamento de resolver os erros.

### Documentação

Grande parte da documentação está nas próprias classes. Há dois documentos ```docs/funcionalidades.md```
e ```docs/regras.md``` que servem de documentação para o projeto, porém ambos estão desatualizados.

## Ferramentas utilizadas

* [Eclipse IDE](https://www.eclipse.org/eclipseide/) para desenvolvimento do projeto

* [Intellij IDEA](https://www.jetbrains.com/pt-br/idea/) para ferramentas de desenvolvimento avançadas

* [Git Bash](https://git-scm.com/downloads) e [Github Desktop]() para compartilhamento e versionamento do projeto

* [Junit 4](https://junit.org/junit4/) para framework de testes