package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import controller.observer.*;

/**
 * Janela do Jogador
 * � inicializada no meio da tela, e contem o fundo da mesa, baralho
 * e m�o do jogador.
 */
public class FrameJogador extends JFrame implements ActionListener, ObservadorAPI, MouseListener {
    public final int COMPRIMENTO = 700;
    public final int ALTURA = 550;
    private final Image background = Imagem.get("background");
    private final Image cartaAzul = Imagem.get("azul");
    private final Image cartaVermelha = Imagem.get("vermelho");

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
    protected HashMap<Integer, Integer> mapaFichas;
    protected int valorDinheiro;
    protected JLabel labelDinheiro;
    // para a aposta do jogador
    protected ArrayList<Integer> listaAposta;
    protected JLabel labelAposta;
    protected int valorAposta;


    protected Controller controller;
    protected String numJogador;
    protected int idJogador;
    protected int idMao;

    /**
     * Cria um novo jogador, configurando os itens b�sicos dele
     * @param controller instacia do Controlador do jogo
     * @param numJogador nome do jogagdor, opcional
     * @param idJogador id do jogador, na lista do controlador. Eh o identificador dele
     * @param idMao id da mao do jogador, na lista do controlador.
     */
    public FrameJogador(Controller controller, String numJogador, int idJogador, int idMao) {
        this.controller = controller;
        this.numJogador = numJogador;
        this.idMao = idMao;
        this.idJogador = idJogador;

        // inicia as fichas
        mapaFichas = new HashMap<>();
        listaAposta = new ArrayList<>();

        // iniciando o frame
        setBounds(0, 0, COMPRIMENTO, ALTURA);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        String titulo = String.format("Jogador n� %s [BLACKJACK]", numJogador);
        setTitle(titulo);
        getContentPane().setLayout(null);
        addMouseListener(this);

        // colocando os botoes
        colocarBotoes();
        mudarEstadoBotoes(false);
        botaoFinalizarAposta.setVisible(false);

        // criando e posicionando os labels
        criaLabels();

        // iniciado
        setVisible(true);
    }

    /**
     * Reinicia os labels do jogador e retira as cartas dele
     */
    public void reiniciarJogador() {
        listaCartas = null;
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

        labelAposta.setText("Aposta: " + valorAposta);
        labelDinheiro.setText("Dinheiro: $" + valorDinheiro + ".00");
        labelAposta.repaint();
        labelDinheiro.repaint();


        // desenha as fichas do jogador
        int v, q;
        Integer[] xy;
        Image img;
        for (Map.Entry<Integer, Integer> entry : mapaFichas.entrySet()) {
            v = entry.getKey();
            q = entry.getValue();
            xy = posicaoFicha(v);
            img = Imagem.get("ficha" + v);
            for (int i = 0; i < q; i ++) {
                g2d.drawImage(img, xy[0], xy[1] - 5*(i+1), null);
            }
        }
    }
    /**
     * Cria todas as labels
     */
    void criaLabels() {
        labelValorCartas = criaLabel(valorCartas, COMPRIMENTO / 2 - 50, ALTURA / 2 + 30, 20);
        labelAposta = criaLabel(valorAposta, 30, ALTURA / 2 - 10, 100);
        labelDinheiro = criaLabel(valorDinheiro, COMPRIMENTO / 2 - 50, ALTURA - 30, 150);
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
        jl.setOpaque(false);
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
    public void fechar() { setVisible(false);}

    /**
     * Detecta os pressionamentos dos botoes.
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();  // pega de onde veio o evento (qual botao)
        if (obj.equals(botaoStand)) {
        	this.controller.fazerJogada("STAND");
        	mudarEstadoBotoes(false);   // temporario. deve ser implementado nas notificacoes?
        } else if (obj.equals(botaoHit)) {
        	this.controller.fazerJogada("HIT");
        } else if (obj.equals(botaoDouble)) {
        	this.controller.fazerJogada("DOUBLE");
        } else if (obj.equals(botaoSplit)) {
        	this.controller.fazerJogada("SPLIT");
        } else if (obj.equals(botaoSurrender)) {
        	this.controller.fazerJogada("SURRENDER");
        } else if (obj.equals(botaoFinalizarAposta)) {
            this.controller.finalizarAposta();
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
        botaoFinalizarAposta.setVisible(false);
        labelValorCartas.setVisible(true);
    }

    /**
     * Prepara para fazer as apostas.
     */
    public void iniciarAposta() {
        labelAposta.setVisible(true);
        botaoFinalizarAposta.setVisible(true);
    }

    /**
     * Implementacao de recebimento de notificacao da api
     * Notificacoes consideradas:
     *  - JogadorCartas
     *  - JogadorAposta
     *
     * @param o Api do blackjack
     */
    public void notificar(ObservadoAPI o) {
        switch (o.getNotificacao()) {
            case JogadorCartas: {
                listaCartas = o.getCartasJogador(idJogador, idMao);
                valorCartas = o.getValorJogador(idJogador, idMao);

                // TODO: precisa verificar os botoes (se pode acionar ou nao)
                repaint();
            }
            case JogadorAposta: {
                mapaFichas = o.getFichasJogador(idJogador);
                listaAposta = o.getApostaJogador(idJogador);
                valorDinheiro = o.getDinheiroJogador(idJogador);
                valorAposta = o.getValorApostaJogador(idJogador);

                botaoFinalizarAposta.setEnabled(o.getPodeApostaJogador(idJogador));
                repaint();
            }
            default: {}     // outras notificacoes sao ignoradas
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
     * @param e
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
