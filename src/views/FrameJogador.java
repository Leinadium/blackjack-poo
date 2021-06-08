package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


import controller.Controller;
import controller.observer.*;

/**
 * Janela do Jogador
 * É inicializada no meio da tela, e contem o fundo da mesa, baralho
 * e mão do jogador.
 */
public class FrameJogador extends JFrame implements ActionListener, ObservadorAPI, MouseListener {
    public final int COMPRIMENTO = 700;
    public final int ALTURA = 550;
    private final Image background = Imagem.get("background");

    private final int sizeFicha = 60;

    // botoes
    protected JButton botaoStand;
    protected JButton botaoSplit;
    protected JButton botaoHit;
    protected JButton botaoSurrender;
    protected JButton botaoDouble;
    protected JButton botaoFinalizarAposta;

    // cada item visual tem sua lista de "objetos", um label e o valor no label
    // para as cartas
    protected String[] listaCartas;
    protected JLabel labelValorCartas;
    protected int valorCartas;
    // para as fichas do jogador
    protected int valorDinheiro;
    protected JLabel labelDinheiro;
    // para a aposta do jogador
    protected ArrayList<Integer> listaAposta;
    protected JLabel labelAposta;
    protected int valorAposta;
    // para o resultado
    protected JLabel labelResultado;


    protected Controller controller;
    public String nomeJogador;
    public int idJogador;
    public int idMao;

    /**
     * Cria um novo jogador, configurando os itens básicos dele
     * @param controller instacia do Controlador do jogo
     * @param nomeJogador nome do jogagdor,
     * @param idJogador id do jogador, na lista do controlador. Eh o identificador dele
     * @param idMao id da mao do jogador, na lista do controlador.
     */
    public FrameJogador(Controller controller, String nomeJogador, int idJogador, int idMao) {
        this.controller = controller;
        this.nomeJogador = nomeJogador;
        this.idMao = idMao;
        this.idJogador = idJogador;

        // inicia as fichas
        listaAposta = new ArrayList<>();

        // iniciando o frame
        setBounds(0, 0, COMPRIMENTO, ALTURA);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        String titulo = String.format("%s [BLACKJACK]", nomeJogador);
        setTitle(titulo);
        getContentPane().setLayout(null);
        addMouseListener(this);

        // colocando os botoes
        colocarBotoes();
        mudarEstadoBotoes(false);
        botaoFinalizarAposta.setVisible(false);

        // criando e posicionando os labels
        criaLabels();
        labelDinheiro.setVisible(true);
        labelResultado.setVisible(false);

        // iniciado
        setVisible(true);
    }

    /**
     * Reinicia os labels do jogador e retira as cartas dele
     */
    public void reiniciarJogador() {
        listaCartas = null;
        mudarEstadoBotoes(false);
        labelResultado.setVisible(false);
        labelValorCartas.setVisible(false);
        labelAposta.setVisible(false);
        repaint();
    }

    /**
     * Override do metodo de pintar o frame. Desenha tudo
     * @param g Graphics
     */
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // desenha o fundo
        g2d.drawImage(background, 0, 0, null);

        // desenha os botoes
        botaoHit.repaint();
        botaoStand.repaint();
        botaoDouble.repaint();
        botaoSurrender.repaint();
        botaoSplit.repaint();
        botaoFinalizarAposta.repaint();

        // posicao default das fichas da aposta.
        int posicaoApostaX = COMPRIMENTO / 2 - sizeFicha / 2;       // default para modo de aposta
        int posicaoApostaY = ALTURA / 2 - sizeFicha + 3 * listaAposta.size();

        // desenha as cartas do jogador
        if (listaCartas != null) {
            // se tem cartas, coloca as fichas para o lado
            posicaoApostaX = 50;
            posicaoApostaY -= 10;

            int deslocamentoPorCarta = 20;
            int inicio = (COMPRIMENTO - deslocamentoPorCarta * listaCartas.length) / 2 - 50;
            int y = ALTURA / 2 - 50;
            
            // bota as cartas viradas pra cima
            for (int i = 0; i < listaCartas.length; i++) {
                String[] carta = listaCartas[i].split("-");
                Image imagemCarta = Imagem.get(carta[0], carta[1]);
                g2d.drawImage(imagemCarta, inicio + deslocamentoPorCarta * i, y, null);
            }
            //coloca o total de pontos da mao
            labelValorCartas.setText(Integer.toString(this.valorCartas));
            labelValorCartas.repaint();
        }

        // fichas da aposta
        int valor;
        if (listaAposta.size() > 0) {
            for (int i = 0; i < listaAposta.size(); i++) {
                valor = listaAposta.get(i);
                Image img = Imagem.get("ficha" + valor);
                g2d.drawImage(img, posicaoApostaX, posicaoApostaY - 6 * i, null);
            }
        }

        labelAposta.setText("Aposta: $" + valorAposta + ".00");
        labelDinheiro.setText("Dinheiro: $" + valorDinheiro + ".00");
        labelAposta.repaint();
        labelDinheiro.repaint();

        labelResultado.repaint();
    }
    /**
     * Cria todas as labels
     */
    void criaLabels() {
        labelValorCartas = criaLabel(valorCartas, COMPRIMENTO / 2 - 50, ALTURA / 2 + 30, 20);
        labelAposta = criaLabel(valorAposta, 30, ALTURA / 2 - 10, 150);
        labelDinheiro = criaLabel(valorDinheiro, COMPRIMENTO / 2 - 75, ALTURA - 60, 150);
        labelResultado = criaLabel(123, COMPRIMENTO / 2 - 100, ALTURA - 100, 200);
    }

    /**
     * Cria uma label nas posicoes passadas, adiciona ao painel do frame retorna
     * @param nome nome do label
     * @param posicaoX posicao x do label no frame
     * @param posicaoY posicao y do label no frame
     * @return um componente JLabel ja posicionado e adicionado ao JFrame
     */
    JLabel criaLabel(int nome, int posicaoX, int posicaoY, int largura) {
        JLabel jl = new JLabel(Integer.toString(nome));
        // jl.setOpaque(false);
        jl.setHorizontalTextPosition(JLabel.CENTER);
        jl.setFont(new Font("Serif", Font.BOLD, 18));
        jl.setBounds(posicaoX, posicaoY , largura, 20);
        jl.setVisible(true);
        getContentPane().add(jl);
        return jl;
    }

    /**
     * Fecha a tela do jogador
     */
    public void fechar() {
        setVisible(false);
        dispose();
    }
    
    /**
     * Desativa o botao de uma acao
     * @param acao Nome da jogada do jogador
     * @param estado true para ligar o botão
     */
    private void alteraEstadoBotao(String acao, boolean estado) {
    	if (acao.equals("STAND")) {
    		this.botaoStand.setEnabled(estado);
    	}
    	else if (acao.equals("HIT")) {
    		this.botaoHit.setEnabled(estado);
    	}
    	else if (acao.equals("DOUBLE")) {
    		this.botaoDouble.setEnabled(estado);
    	}
    	else if (acao.equals("SURRENDER")) {
    		this.botaoSurrender.setEnabled(estado);
    	}
    	else if (acao.equals("SPLIT")) {
    		this.botaoSplit.setEnabled(estado);
    	}
    }
    
    
    /**
     * Detecta os pressionamentos dos botoes.
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();  // pega de onde veio o evento (qual botao)
        if (obj.equals(botaoStand)) {
        	this.controller.fazerJogada("STAND", idMao);
        	// mudarEstadoBotoes(false);   // temporario. deve ser implementado nas notificacoes?
        } else if (obj.equals(botaoHit)) {
        	this.controller.fazerJogada("HIT", idMao);
        } else if (obj.equals(botaoDouble)) {
        	this.controller.fazerJogada("DOUBLE", idMao);
        } else if (obj.equals(botaoSplit)) {
        	this.controller.fazerJogada("SPLIT", idMao);
        } else if (obj.equals(botaoSurrender)) {
        	this.controller.fazerJogada("SURRENDER", idMao);
        } else if (obj.equals(botaoFinalizarAposta)) {
            this.controller.finalizarAposta();
        	this.finalizarAposta();
        } else {
            System.out.println("AINDA NAO IMPLEMENTADO");
        }
    }

    /**
     * Cria e coloca os botoes em cada posicao
     */
    void colocarBotoes() {
        // criando os botoes
        botaoStand = new JButton("STAND");
        botaoHit = new JButton("HIT");
        botaoDouble = new JButton("DOUBLE");
        botaoSurrender = new JButton("SURRENDER");
        botaoSplit = new JButton("SPLIT");
        botaoFinalizarAposta = new JButton("FINALIZAR APOSTA");

        // colocando o listener
        botaoStand.addActionListener(this);
        botaoHit.addActionListener(this);
        botaoDouble.addActionListener(this);
        botaoSurrender.addActionListener(this);
        botaoSplit.addActionListener(this);
        botaoFinalizarAposta.addActionListener(this);

        // posicionando
        botaoStand.setBounds(500, 30, 150, 30);
        botaoHit.setBounds(500, 80, 150, 30);
        botaoDouble.setBounds(500, 130, 150, 30);
        botaoSurrender.setBounds(500, 180, 150, 30);
        botaoSplit.setBounds(500, 230, 150, 30);
        botaoFinalizarAposta.setBounds(500, 280, 150, 30);

        // colocando
        getContentPane().add(botaoStand);
        getContentPane().add(botaoHit);
        getContentPane().add(botaoDouble);
        getContentPane().add(botaoSurrender);
        getContentPane().add(botaoSplit);
        getContentPane().add(botaoFinalizarAposta);
    }

    /**
     * Muda os estados de todos os botoes de acao de jogaoda
     * @param estado true para deixar visivel, falso para esconder
     */
    void mudarEstadoBotoes(boolean estado) {
        // botando como invisivel ou nao porque eles nao aparecem antes da rodada ser iniciada
        botaoStand.setVisible(estado);
        botaoHit.setVisible(estado);
        botaoDouble.setVisible(estado);
        botaoSurrender.setVisible(estado);
        botaoSplit.setVisible(estado);
        repaint();
    }

    /**
     * Muda os estados dos botoes para iniciar a rodada
     */
    public void iniciarRodada() {
    	mudarEstadoBotoes(true);
        labelValorCartas.setVisible(true);
    }

    /**
     * Prepara para fazer as apostas.
     */
    public void iniciarAposta() {
        labelAposta.setVisible(true);
        botaoFinalizarAposta.setVisible(true);
        repaint();
    }
    
    /**
     * Quando um jogador terminar de fazer sua aposta.
     */
    public void finalizarAposta() {
    	//this.controller.finalizarAposta();
    	botaoFinalizarAposta.setVisible(false);
    	repaint();
    }

    /**
     * Implementacao de recebimento de notificacao da api
     * Notificacoes consideradas:
     *  - JogadorCartas
     *  - JogadorAposta
     *  - JogadorAcao
     *
     * @param o Api do blackjack
     */
    public void notificar(ObservadoAPI o) {
    	boolean mao_splitada;
    	//se quiser, pode fazer o switch case. mas nao sei pq do jeito que tava ele tava aceitando todos os casos que estavam embaixo.
    	//por ex: no switch case, se a acao era JogadorAposta, caia em JogadorCartas e JogadorAcao tambem
    	NotificacaoAPI not = o.getNotificacao();
        if (not == NotificacaoAPI.JogadorAposta) {
            listaAposta = o.getApostaJogador(idJogador, idMao);
            valorDinheiro = o.getDinheiroJogador(idJogador);
            valorAposta = o.getValorApostaJogador(idJogador, idMao);
            botaoFinalizarAposta.setEnabled(o.getPodeApostaJogador(idJogador));
            repaint();
    	}
    	else if (not == NotificacaoAPI.JogadorCartas) {
            listaCartas = o.getCartasJogador(idJogador, idMao);
            valorCartas = o.getValorJogador(idJogador, idMao);
            repaint();
    	}
    	else if (not == NotificacaoAPI.JogadorAcao) {
        	this.alteraEstadoBotao("STAND", o.getPodeStand(idJogador, idMao));
        	this.alteraEstadoBotao("HIT", o.getPodeHit(idJogador, idMao));
        	this.alteraEstadoBotao("DOUBLE", o.getPodeDouble(idJogador, idMao));
        	this.alteraEstadoBotao("SURRENDER", o.getPodeSurrender(idJogador, idMao));
        	this.alteraEstadoBotao("SPLIT", o.getPodeSplit(idJogador, idMao));
        	repaint();
    	}
    	else if (not == NotificacaoAPI.JogadorResultado) {
    	    String resultado = o.getResultado(idJogador, idMao);
    	    valorDinheiro = o.getDinheiroJogador(idJogador);
    	    labelResultado.setText("Vencedor: " + resultado);
    	    labelResultado.setVisible(true);
    	    System.out.println(idJogador + "[" + idMao + "]: " + resultado);
    	    repaint();
        }

    }

    /**
     * Retorna a posicao da ficha daquele valor na tela
     * @param valor 1, 5, 10, 20, 50, ou 100
     * @return {posX, posY}
     */
    Integer[] posicaoFicha(int valor) {
        Integer[] ret = {0, 450};
        int sep = (COMPRIMENTO - (sizeFicha * 6)) / 7;
        switch (valor) {
            case 1: ret[0] = sep;break;
            case 5: ret[0] = 2*sep + sizeFicha;break;
            case 10: ret[0] = 3*sep + 2 * sizeFicha;break;
            case 20: ret[0] = 4*sep + 3 * sizeFicha;break;
            case 50: ret[0] = 5*sep + 4 * sizeFicha;break;
            case 100: ret[0] = 6*sep + 5 * sizeFicha;break;
        }
        return ret;
    }

    /**
     * Implementacao de clique de mouse para detectar se o jogador deseja diminuir a aposta
     * @param e MouseEvent
     */
    public void mouseClicked(MouseEvent e) {
        // se estiver na hora da aposta, e tiver alguma ficha para retirar
        if (botaoFinalizarAposta.isVisible() && listaAposta.size() > 0) {
            int x = e.getX();   // pega a posicao do mouse
            int y = e.getY();

            // posicao basica das fichas da aposta
            int posicaoApostaX = COMPRIMENTO / 2 - sizeFicha / 2;       // posicao das fichas
            int posicaoApostaY = ALTURA / 2 - sizeFicha + 3 * listaAposta.size();

            // as posicoes X das fichas sao iguais
            // y pode ir de posicaoApostaY - (6 para cada ficha) ate posicaoApostaY
            if (x >= posicaoApostaX && x <= posicaoApostaX + 60 &&
                y >= posicaoApostaY - 6 * listaAposta.size() && y <= posicaoApostaY + sizeFicha) {

                // passa o ultimo da lista (poderia ser uma pilha...)
                this.controller.diminuiAposta(listaAposta.get(listaAposta.size() - 1));
            }

        }
    }
    /* metodos vazios mas necessarios para implementar MouseListener */
    
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

}
