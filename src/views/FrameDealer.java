package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.Controller;
import controller.observer.*;


/**
 * Janela do Dealer
 * É inicializada no meio da tela, e contem o fundo da mesa, baralho e a
 * mão do dealer, alem de tres botoes.
 *
 * Eh observador da API do model (model.blackjack)
 */

public class FrameDealer extends JFrame implements ActionListener, ObservadorAPI {
    public final int COMPRIMENTO = 900;
    public final int ALTURA = 700;
    private final Image background = Imagem.get("background");      // imagens carregadas
    private final Image cartaAzul = Imagem.get("azul");
    private final Image cartaVermelha = Imagem.get("vermelho");
    private final Image ficha1 = Imagem.get("ficha1");
    private final Image ficha5 = Imagem.get("ficha5");
    private final Image ficha10 = Imagem.get("ficha10");
    private final Image ficha20 = Imagem.get("ficha20");
    private final Image ficha50 = Imagem.get("ficha50");
    private final Image ficha100 = Imagem.get("ficha100");

    protected JButton botaoEncerrar;                                // botoes
    protected JButton botaoNovaRodada;
    protected JButton botaoSalvar;
    protected JLabel labelValorCartas;

    protected String[] cartas;          // cartas do dealer. Sao coletadas atraves da api
    protected int valorCartas;          // valor das cartas do dealer. Sao coletadas atraves da api

    protected Controller controller;

    public FrameDealer(Controller c) {
        this.controller = c;

        // pegando informacoes da monitor
        // Toolkit tk = Toolkit.getDefaultToolkit();
        // Dimension screenSize = tk.getScreenSize();
        // pegando o meio da tela
        // int comp = screenSize.width;
        // int altu = screenSize.height;
        // int x = (comp - ALTURA) / 2;
        // int y = (altu - COMPRIMENTO) / 2;

        // iniciando o frame
        // setBounds(x, y, COMPRIMENTO, ALTURA);
        setBounds(0, 0, 900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Dealer [BLACKJACK]");
        getContentPane().setLayout(null);

        // colocando os botoes
        colocarBotoes();
        // cria o label com o valor das cartas
        criaLabelValor();
        setVisible(true);
    }

    /**
     * Reinicia as propriedades do Dealer:
     * - Exclui as cartas da mao dele
     * - Esconde o label
     * - Libera o botao de Nova Rodada
     */
    public void reiniciarDealer() {
        cartas = null;
        labelValorCartas.setVisible(false);
        this.botaoNovaRodada.setEnabled(true);
        repaint();
    }

    /**
     * Override do metodo de pintar o Frame
     * @param g Graphics
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // desenha o fundo
        g2d.drawImage(background, 0, 0, null);

        // desenha os botoes
        botaoEncerrar.repaint();
        botaoNovaRodada.repaint();
        botaoSalvar.repaint();

        // desenha o baralho
        int deslocamentoBaralhoX = 30;
        int deslocamentoBaralhoY = 50;
        g2d.drawImage(cartaAzul, deslocamentoBaralhoX, deslocamentoBaralhoY, null);
        g2d.drawImage(cartaVermelha, deslocamentoBaralhoX + 4, deslocamentoBaralhoY + 2, null);
        g2d.drawImage(cartaAzul, deslocamentoBaralhoX + 8, deslocamentoBaralhoY + 4, null);

        // desenha as cartas do dealer
        if (cartas != null) {
            int deslocamentoPorCarta = 20;
            int inicio = (COMPRIMENTO/2) - deslocamentoPorCarta * cartas.length;
            int y = 350;

            // bota a primeira carta virada para cima
            String[] carta = this.cartas[0].split("-");
            Image imagemCarta = Imagem.get(carta[0], carta[1]);
            g2d.drawImage(imagemCarta, inicio, y, null);

            // coloca as outras cartas viradas para baixo
            for (int i = 1; i < this.cartas.length; i ++) {
                carta = this.cartas[i].split("-");
                if (carta[1].equals("ESPADAS") || carta[1].equals("PAUS")) {
                    imagemCarta = cartaAzul;
                } else { imagemCarta = cartaVermelha; }
                g2d.drawImage(imagemCarta, inicio + deslocamentoPorCarta * i, y, null);
            }
            // desenha o label
            labelValorCartas.setText(Integer.toString(this.valorCartas));
            labelValorCartas.repaint();

        }
        //desenha as fichas (temporario)
        int deslocamentoFichaX = 30;
        int deslocamentoFichaY = 600;
        
        g2d.drawImage(ficha1, deslocamentoFichaX, deslocamentoFichaY, null);
        g2d.drawImage(ficha5, 4*deslocamentoFichaX, deslocamentoFichaY, null);
        g2d.drawImage(ficha10, 7*deslocamentoFichaX, deslocamentoFichaY, null);
        g2d.drawImage(ficha20, 10*deslocamentoFichaX, deslocamentoFichaY, null);
        g2d.drawImage(ficha50, 13*deslocamentoFichaX, deslocamentoFichaY, null);
        g2d.drawImage(ficha100, 16*deslocamentoFichaX, deslocamentoFichaY, null);
    }

    /**
     * Fecha a tela do Dealer
     */
    public void fechar() { setVisible(false);}

    /**
     * Implementacao do Listener de acoes
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();  // pega de onde veio o evento (qual botao)
        if (obj.equals(botaoEncerrar)) { this.controller.fecharPartida(); }
        else if (obj.equals(botaoNovaRodada)) {
            this.botaoNovaRodada.setEnabled(false);
            this.controller.iniciarRodada();
        }
        else { this.controller.salvarPartida(); }
    }

    /**
     * Cria o label para exibir o valor das cartas.
     */
    void criaLabelValor() {
        labelValorCartas = new JLabel(Integer.toString(valorCartas));
        labelValorCartas.setOpaque(false);
        labelValorCartas.setHorizontalTextPosition(JLabel.CENTER);
        labelValorCartas.setFont(new Font("Serif", Font.BOLD, 18));
        labelValorCartas.setBounds(COMPRIMENTO / 2 - 10, 420, 20, 30);

        labelValorCartas.setVisible(false);
        getContentPane().add(labelValorCartas);
    }

    /**
     * Cria os botoes e coloca nas posicoes corretas
     */
    void colocarBotoes() {
        // criando os botoes
        botaoEncerrar = new JButton("Fechar partida");
        botaoNovaRodada = new JButton("Nova rodada");
        botaoSalvar = new JButton("Salvar partida");

        // colocando o listener
        botaoEncerrar.addActionListener(this);
        botaoNovaRodada.addActionListener(this);
        botaoSalvar.addActionListener(this);

        // posicionando
        botaoEncerrar.setBounds(700, 30, 150, 30);
        botaoSalvar.setBounds(700, 80, 150, 30);

        botaoNovaRodada.setBounds(COMPRIMENTO / 2 - 75, 100, 150, 30);

        // colocando
        getContentPane().add(botaoEncerrar);
        getContentPane().add(botaoNovaRodada);
        getContentPane().add(botaoSalvar);
    }

    /**
     * Coloca as fichas nas posicoes corretas
     */
    void colocarFichas() {
    	// TODO
    }

    /**
     * Implementacao de recebimento de notificacao da api
     * Notificacoes consideradas:
     * - DealerCartas
     * - DealerAposta
     *
     * @param o Api do blackjack
     */
    public void notificar(ObservadoAPI o) {
        switch (o.getNotificacao()) {   // pega a notificacao
            case DealerCartas: {
                labelValorCartas.setVisible(true);          // mostra o label
                this.cartas = o.getCartasDealer();          // atualiza as cartas
                this.valorCartas = o.getValorDealer();      // pega a soma das cartas
                repaint();                                  // atualiza a tela inteira
            }
            case DealerAposta: {
                // TODO
            }
            default: {}     // outras notificacoes sao ignoradas
        }
    }
}
