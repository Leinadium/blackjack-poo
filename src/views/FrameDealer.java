/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */

package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import controller.Controller;
import controller.observer.*;


/**
 * Janela do Dealer
 * É inicializada no meio da tela, e contem o fundo da mesa, baralho e a
 * mão do dealer, alem de tres botoes.
 *
 * Eh observador da API do model (model.blackjack)
 */
public class FrameDealer extends JFrame implements ActionListener, MouseListener, ObservadorAPI {
    public final int COMPRIMENTO = 700;
    public final int ALTURA = 550;
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
    protected boolean ehFinalizado;     // para saber se exibe o total das cartas, ou so o verso das cartas

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
        setBounds(0, 0, COMPRIMENTO, ALTURA);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Dealer [BLACKJACK]");
        getContentPane().setLayout(null);

        // colocando os botoes
        colocarBotoes();
        // cria o label com o valor das cartas
        criaLabelValor();

        // adiciona ele mesmo para implementar o pressionamento de mouse
        getContentPane().addMouseListener(this);

        setVisible(true);
    }

    /**
     * Reinicia as propriedades do Dealer:
     * - Exclui as cartas da mao dele
     * - Esconde o label
     */
    public void reiniciarDealer() {
        cartas = null;
        labelValorCartas.setVisible(false);
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
            int inicio = (COMPRIMENTO - deslocamentoPorCarta * cartas.length) / 2;
            int y = ALTURA / 2;

            // bota a primeira carta virada para cima
            String[] carta = this.cartas[0].split("-");
            Image imagemCarta = Imagem.get(carta[0], carta[1]);
            g2d.drawImage(imagemCarta, inicio, y, null);

            if (!this.ehFinalizado) {   // exibe as cartas viradas se nao finalizou
                // coloca as outras cartas viradas para baixo
                for (int i = 1; i < this.cartas.length; i++) {
                    carta = this.cartas[i].split("-");
                    if (carta[1].equals("ESPADAS") || carta[1].equals("PAUS")) {
                        imagemCarta = cartaAzul;
                    } else {
                        imagemCarta = cartaVermelha;
                    }
                    g2d.drawImage(imagemCarta, inicio + deslocamentoPorCarta * i, y, null);
                }
            } else {    // desenha o label e as cartas viradas pra cima
                for (int i = 1; i < this.cartas.length; i++) {
                    carta = this.cartas[i].split("-");
                    Image img = Imagem.get(carta[0], carta[1]);
                    g2d.drawImage(img, inicio + deslocamentoPorCarta * i, y, null);
                }
                // desenha o label
                labelValorCartas.setText(Integer.toString(this.valorCartas));
                labelValorCartas.repaint();
            }
        }
        //desenha as fichas
        int deslocamentoFichaX = 90;
        int inicioX = (COMPRIMENTO - 60 * 9) / 2;
        int deslocamentoFichaY = ALTURA * 4 / 5;
        
        g2d.drawImage(ficha1, inicioX, deslocamentoFichaY, null);
        g2d.drawImage(ficha5, inicioX + deslocamentoFichaX, deslocamentoFichaY, null);
        g2d.drawImage(ficha10, inicioX + 2 * deslocamentoFichaX, deslocamentoFichaY, null);
        g2d.drawImage(ficha20, inicioX + 3 * deslocamentoFichaX, deslocamentoFichaY, null);
        g2d.drawImage(ficha50, inicioX + 4 * deslocamentoFichaX, deslocamentoFichaY, null);
        g2d.drawImage(ficha100, inicioX + 5 *deslocamentoFichaX, deslocamentoFichaY, null);
    }

    /**
     * Fecha a tela do Dealer
     */
    public void fechar() {
        setVisible(false);
        dispose();
    }
    /**
     * Altera o estado do botao salvar, para disponivel ou nao disponivel.
     * Ele eh inicializado como indisponivel. Quando uma rodada eh iniciada, ele passa a ser disponivel.
     * Quando o jogador faz uma jogada, o botao fica indisponivel.
     * Ele so volta a ficar disponivel quando a vez eh passada.
     */
    public void alteraEstadoBotaoSalvar(boolean estado) {
    	this.botaoSalvar.setEnabled(estado);
    }
    

    /**
     * Implementacao do Listener de acoes
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();  // pega de onde veio o evento (qual botao)
        if (obj.equals(botaoEncerrar)) { this.controller.fecharPartida(); }
        else if (obj.equals(botaoNovaRodada)) {
            this.botaoNovaRodada.setEnabled(false);
            this.controller.iniciarAposta();
        }
        else { this.controller.salvarPartida(); }
    }

    /**
     * Implemlentacao de pressionamento de mouse para poder aumentar a aposta.
     * Chama o controller caso tenha clicado em alguma ficha, pedindo para
     * aumentar a aposta
     * @param e MouseEvent
     */
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int inicioX = (COMPRIMENTO - 60 * 9) / 2 - 20; // -20 ??
        int fichaY = ALTURA * 4 / 5 - 30; // -30 ??
        int fichaFinal = -1;

        if ((y >= fichaY && y <= fichaY + 60) && (x >= inicioX && x <= inicioX + 60)) {
            fichaFinal = 1;
        } else if (y >= fichaY && y <= fichaY + 60 && x >= inicioX+90 && x <= inicioX+150) {
            fichaFinal = 5;
        } else if (y >= fichaY && y <= fichaY + 60 && x >= inicioX+180 && x <= inicioX+240) {
            fichaFinal = 10;
        } else if (y >= fichaY && y <= fichaY + 60 && x >= inicioX+270 && x <= inicioX+330) {
            fichaFinal = 20;
        } else if (y >= fichaY && y <= fichaY + 60 && x >= inicioX+360 && x <= inicioX+420) {
            fichaFinal = 50;
        } else if (y >= fichaY && y <= fichaY + 60 && x >= inicioX+450 && x <= inicioX+510) {
            fichaFinal = 100;
        }

        System.out.println("pos: " + x + ", " + y + " --> ficha: " + fichaFinal);

        if (fichaFinal != -1) {
            this.controller.aumentaAposta(fichaFinal);
        }
    }

    /* Implementacao vazias para poder usar MouseListener */
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    /**
     * Cria o label para exibir o valor das cartas.
     */
    void criaLabelValor() {
        labelValorCartas = new JLabel(Integer.toString(valorCartas));
        labelValorCartas.setOpaque(false);
        labelValorCartas.setHorizontalTextPosition(JLabel.CENTER);
        labelValorCartas.setFont(new Font("Serif", Font.BOLD, 18));
        labelValorCartas.setBounds(COMPRIMENTO / 2 - 10, ALTURA / 2 - 60 , 20, 20);

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
        botaoEncerrar.setBounds(COMPRIMENTO * 3 / 5, 30, 150, 30);
        botaoSalvar.setBounds(COMPRIMENTO * 3 / 5, 80, 150, 30);
        botaoNovaRodada.setBounds(COMPRIMENTO * 3 / 5, 130, 150, 30);

        // colocando
        getContentPane().add(botaoEncerrar);
        getContentPane().add(botaoNovaRodada);
        getContentPane().add(botaoSalvar);
        
        // botao de salvar inicia desativado
        botaoSalvar.setEnabled(false);
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
                this.cartas = o.getCartasDealer();              // atualiza as cartas
                this.valorCartas = o.getValorDealer();          // pega a soma das cartas
                this.ehFinalizado = o.getFinalizadoDealer();    // verifica se o dealer jogou
                labelValorCartas.setVisible(this.ehFinalizado);
                repaint();                                      // atualiza a tela inteira
                break;
            }
            case DealerAposta: {
                // ignorado
                break;
            }
            case JogadorResultado: {
                // significa que o jogo finalizou
                this.botaoNovaRodada.setEnabled(true);
                repaint();
                break;
            }

            default: {}     // outras notificacoes sao ignoradas
        }
    }
}
