/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import model.cartas.*;

import java.util.List;
import java.util.ArrayList;

import java.lang.IllegalStateException;

/**
 * Classe Jogador.
 *
 * Metodos e funcoes:
 *     String nome -> nome do jogador
 *     Mao mao -> mao principal do jogador
 *     List maos_split -> lista com as maos para quando ocorrer um split
 *     List fichas -> lista contendo todas as fichas do jogador
 *     int dinheiro -> dinheiro associado as fichas do jogador
 *
 *     boolean tem_dinheiro() -> retorna se o jogador tem dinheiro
 *     void adicionar_ficha(Ficha) -> adiciona uma ficha a lista do jogador, atualizando o dinheiro
 *     void retirar_ficha(Ficha) -> remove aquela ficha do jogador, atualizando o dinheiro.
 */


class Jogador {
    public int numero;
    public Mao mao;
    public Mao maoSplit;
    public Mao maoSplit2;
    public int dinheiro;
    // public int aposta;
    public List<Ficha> fichasAposta;
    public boolean finalizado;

    public boolean rendido;
    private Jogada ultimaJogada;
    private int quantidadeJogadas;
    private int quantidadeSplits;

    Jogador (int numJogador) {
        int i;
        this.numero = numJogador;
        this.mao = new Mao();
        this.maoSplit = null;
        this.maoSplit2 = null;
        this.fichasAposta = new ArrayList<>();
        this.dinheiro = 500;
        // this.aposta = 0;
        this.rendido = false;
        this.finalizado = false;
        this.quantidadeJogadas = 0;
        this.ultimaJogada = null;

        // criando as maos split nulas
        // for (i = 0; i < 2; i ++) {maosSplit.add(null);}
    }
    /**
     * Prepara o jogador para uma nova rodada
     */
    public void iniciaJogada() {
    	int i;
        this.mao = new Mao();
        this.maoSplit = null;
        this.maoSplit2 = null;
        this.fichasAposta = new ArrayList<>();
        // this.aposta = 0;
        this.rendido = false;
        this.finalizado = false;
        this.quantidadeJogadas = 0;
        this.ultimaJogada = null;
    }
    /**
     * Retorna se o jogador possui algum dinheiro para poder apostar
     * @return true se tiver dinheiro
     */
    public boolean temDinheiro() {
        return (this.dinheiro > 0);
    }

    /**
     * Verifica se a aposta atual do jogador eh valida
     * @return true se for valida
     */
    public boolean apostaValida() {
        return this.mao.aposta >= 20 && this.mao.aposta <= 100;
    }

    /**
     * Verifica se o jogador ja tinha apostado naquela jogada
     * @return true se tiver apostado
     */
    public boolean terApostado() {
        return (this.mao.aposta > 0);
    }
    /**
     * Retorna se o jogador finalizou aquela jogada
     * @return true se ele tiver finalizado
     */
    public boolean retornaJogadaFinalizada() {
    	return (this.finalizado);
    }
    /**
     * Altera o estado do jogador caso tenha sido finalizada uma jogada
     * @param estado - estado (boolean) para qual deve ser alterado
     */
    public void alteraJogadaFinalizada(boolean estado) {
    	this.finalizado = estado;
    }

    /**
     * Verifica se o jogador faliu e nao pode mais jogar
     * @return true se o jogador tiver falido
     */
    public boolean checaFalencia() {
        return this.dinheiro < 20;
    }
    /**
     * Verifica se uma aposta pode ser feita
     * @return true se o jogador pode fazer a aposta
     */
    private boolean verificaAposta(int valor){
        return valor >= 20 && valor <= 100 && valor <= this.dinheiro;
    }
    /**
     * (depreciated)
     * Faz uma aposta a partir do dinheiro
     */
    public void fazAposta(int valor) throws IllegalStateException {
    	if (verificaAposta(valor)) {
    		this.dinheiro -= valor;
    		this.mao.aposta = valor;
    	}
    	else {
    		throw new IllegalStateException("O jogador nao pode fazer a aposta com o valor inserido");
    	}
    }

    /**
     * Finaliza a aposta atual, e coloca pronto para a partida
     */
    public void finalizarAposta() {
        this.rendido = false;
        this.ultimaJogada = null;
        this.finalizado = false;
    }

    /**
     * Aumenta a aposta pelo valor da ficha
     * @param f Ficha para aumentar a aposta
     */
    public void aumentarAposta(Ficha f) {
        if (this.dinheiro >= f.valor) {
            this.dinheiro -= f.valor;
            this.fichasAposta.add(f);
            this.mao.aposta += f.valor;
        }
    }

    /**
     * Diminui a aposta pelo valor da ficha
     * @param f Ficha para aumentar a posta
     */
    public void diminuirAposta(Ficha f) {
        this.dinheiro += f.valor;
        this.fichasAposta.remove(f);
        this.mao.aposta -= f.valor;

    }

    /**
     * Retira o dinheiro da conta do jogador
     * @param valor Total de dinheiro a ser retirado
     * @throws Exception caso nao tenha o dinheiro necessario
     */
    public void retirarDinheiro(int valor) throws Exception {
        if (valor > this.dinheiro) {
            throw new Exception("O jogador nao pode retirar esse dinheiro");
        }
        this.dinheiro -= valor;
    }

    /**
     * Recebe o dinheiro e adiciona na conta do jogador
     * @param valor valor a ser adicionado
     */
    public void recebeDinheiro(int valor) {
        this.dinheiro += valor;
    }

    /**
     * Retorna o nivel de split atual.
     * Se não tiver split ainda, retorna 0.
     * Se tiver um split, retorna 1.
     * Se tiver dois splits, retorna 2.
     * @return o nivel do split;
     */
    public int nivelSplit() {
        if (maoSplit2 != null) { return 2; }
        else if (maoSplit != null) { return 1; }
        else { return 0; }
    }

    /**
     * Recebe pagamento caso tenha feito Blackjack
     */
    public void recebePagamentoBlackjack() {
    	this.dinheiro += (this.mao.aposta * 3 / 2);
    }

    public Jogada retornaUltimaJogada() {
        return (this.ultimaJogada);
    }

    /**
     * Retorna uma lista de fichas dobrada.
     *
     * Essa funcao pois teria que ter uma lista de fichas em cada mao.
     * Em vez disso, o jogador possui uma lista de fichas, e caso ele splita e
     * peça double, o valor sera alterado da mão, mas a lista de fichas não.
     * Porém, se a parte gráfica pedir a lista de mãos, será retornada uma copia duplicada do jogador
     * @return lista com as fichas das apostas dobradas
     */
    public ArrayList<Ficha> getFichasApostaDouble() {
        // foi criada uma lista temporaria, pois addAll eh perigoso
        // se a lista muda enquanto o addAll roda.
        ArrayList<Ficha> temp = new ArrayList<>(this.fichasAposta);
        temp.addAll(this.fichasAposta);
        return temp;
    }

    /**
     * Funcao privada para verificar todas as maos se foram finalizadas
     * @return true se todas as maos estiverem finalizadas
     */
    private boolean verificaFinalizadoGeral() {
        boolean ret = this.mao.finalizado;
        if (this.maoSplit != null) {
            ret = ret && this.maoSplit.finalizado;
        }
        if (this.maoSplit2 != null) {
            ret = ret && this.maoSplit2.finalizado;
        }
        return ret;
    }

    /*
    Os métodos a seguir sao respectivos as jogadas que o jogador pode fazer
    Para cada jogada, ha quatro métodos:
        podeJogada(Mao) -> diz se aquela jogada para aquela mao eh valida
        podeJogada() -> overload de podeJogada(Mao), passando a mao normal
        fazerJogada(Mao) -> faz aquela jogada para aquela mao
        fazerJogada() -> overload de fazerJogada(Mao), passando a mao normal
    */

    public boolean podeHit(Mao m) {
        return (!m.finalizado && !this.mao.blackjack);
    }
    public boolean podeHit() { return podeHit(this.mao); }

    public boolean podeStand(Mao m) {
        return (!m.finalizado && !m.blackjack);
    }
    public boolean podeStand() { return podeStand(this.mao);}

    public boolean podeDouble(Mao m) {
        return  (!m.finalizado &&
                (m.cartas.size() == 2) &&
                this.dinheiro >= m.aposta);
    }
    public boolean podeDouble() { return podeDouble(this.mao);}

    public boolean podeSplit(Mao m) {
        return ((this.maoSplit == null || this.maoSplit2 == null)
                && m.aposta <= this.dinheiro
                && m.podeSplit());
    }
    
    public boolean podeSplit() { return podeSplit(this.mao);}
    
    public boolean podeSurrender(Mao m) {
    	return (!m.finalizado && this.ultimaJogada == null && this.mao.cartas.size() == 2);
    }
    public boolean podeSurrender() { return podeSurrender(this.mao);}

    /**
     * Retorna se o Surrender eh valido, de acordo com a mao do dealer
     * @param d Dealer do jogo.
     * @return true se for valido.
     */
    public boolean validaSurrender(Dealer d) {
    	return (!d.mao.blackjack);
    }

    /**
     * Faz o surrender:
     * finaliza o jogador, coloca como rendido
     * e diz a ultima jogada
     * @param m Mao do jogador a se render (a principio, so pode a mao normal)
     */
    public void fazerSurrender(Mao m) {
    	this.rendido = true;
    	this.mao.finalizado = true;
    	this.finalizado = true;
    	this.ultimaJogada = Jogada.SURRENDER;
    }
    public void fazerSurrender() { fazerSurrender(this.mao); }

    /**
     * Faz a jogada de HIT para aquela mao.
     * Pega uma nova carta, e recalcula o valor da mao
     * @param baralho Baralho da rodada
     * @param m Mao do jogador a receber uma carta
     */
    public void fazerHit(Baralho baralho, Mao m) {
        Carta c = baralho.pop();
        m.ganharCarta(c);
        this.quantidadeJogadas += 1;
        this.ultimaJogada = Jogada.HIT;
        this.finalizado = verificaFinalizadoGeral();
    }
    public void fazerHit(Baralho baralho) { this.fazerHit(baralho, this.mao); }

    /**
     * Faz a jogada de STAND para aquela mao;
     * Finaliza a jogada
     * @param m Mao do jogador
     */
    public void fazerStand(Mao m) {
        m.finalizado = true;
        this.ultimaJogada = Jogada.STAND;
        // verifica se esse stand foi o ultimo de todas as maos
        this.finalizado = this.verificaFinalizadoGeral();
    }
    public void fazerStand() { fazerStand(this.mao); }

    /**
     * Faz a jogada de DOUBLE para aquela mao
     * @param m Mao da jogada DOUBLE
     * @param b Baralho para retirar uma nova carta
     * @throws Exception Erro caso nao tenha dinheiro o suficiente
     */
    public void fazerDouble(Mao m, Baralho b) throws Exception {
        try {
            m.apostaDobrada = true;
        	this.retirarDinheiro(m.aposta);
            m.aposta += m.aposta;

            this.fazerHit(b, m);
            this.fazerStand(m);
            this.quantidadeJogadas -= 1;  // Hit e Stand adicionaram +2. Retirando 1 para ficar com +1 jogada
            this.ultimaJogada = Jogada.DOUBLE;
            this.finalizado = this.verificaFinalizadoGeral();
        }
        catch (Exception e) {
        	throw new Exception("O jogador nao possui dinheiro para fazer double");
        }
    }
    public void fazerDouble(Baralho b) throws Exception{
    	try {
    		this.fazerDouble(this.mao, b);
    	}
    	catch (Exception e) {
    		throw new Exception("O jogador nao possui dinheiro para fazer double");
    	}
    }
    
    public void adicionaMao(int idMao) throws IllegalStateException{
    	Mao novaMao = new Mao();
    	if (idMao == 1) {
    		this.maoSplit = novaMao;
    	}
    	else if (idMao == 2) {
    		this.maoSplit2 = novaMao;
    	}
    	else {
    		throw new IllegalStateException("Identificador da Mão Inválido.");
    	}
    }
    
    public void adicionaApostaMao(int idMao, int aposta) {
    	if (idMao == 0) {
    		this.mao.aposta = aposta;
    	} else if (idMao == 1) {
    		this.maoSplit.aposta = aposta;
    	}else {
    		this.maoSplit2.aposta = aposta;
    	}
    }
    
    

    /**
     * Faz a jogada de SPLIT para aquela mao:
     * EH ESPERADO QUE O SPLIT SEJA VALIDO. NAO EH VERIFICADO NOVAMENTE
     * Eh criada a nova mao, e guardada em maoSplit ou maoSplit2.
     * O dinheiro eh retirado e a aposta copiada para a outra mao.
     * Ambas as maos ganham as duas novas cartas. Se foi um split de As, ambas fazem STAND.
     *
     * @param m Mao a ser dividida
     * @param b Baralho para pegar as novas cartas
     * @throws Exception Error se todas as maos estiverem em uso
     */
    public void fazerSplit(Mao m, Baralho b) throws Exception {
        Mao novaMao = m.fazerSplit();  // criando a nova mao

        try {
        	this.retirarDinheiro(m.aposta);  // retirando o dinheiro da aposta do jogador
        }
        catch (Exception e) {
        	throw new Exception("O jogador nao possui dinheiro");
        }

        this.fazerHit(b, m);   // adicionando uma carta em cada mao
        this.fazerHit(b, novaMao);

        // caso especial, se ele splitou um par de ases
        if (m.cartas.get(0).equals(new Carta(Cor.VERMELHO, Nome.AS, Naipe.OUROS))) {
            this.fazerStand(m);
            this.fazerStand(novaMao);
        }

        // salva a nova mao no jogador
        if (maoSplit == null) {
            maoSplit = novaMao;
        } else {
            maoSplit2 = novaMao;
        }

        this.quantidadeJogadas -= 1; // os hits aumentaram +2 na quantidade. Retirando 1
        this.ultimaJogada = Jogada.SPLIT;
    }
    public void fazerSplit(Baralho b) throws Exception { this.fazerSplit(this.mao, b); }

    /**
     * Funcao de acesso às maos do jogador
     * @param id 0 (mao normal), 1 ou 2 (maos split)
     * @return a mao do jogador.
     */
    public Mao getMaoFromId(int id) {
        switch (id) {
            case 1: return this.maoSplit;
            case 2: return this.maoSplit2;
            default: return this.mao;
        }
    }
}
