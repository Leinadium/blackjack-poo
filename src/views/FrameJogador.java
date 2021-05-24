package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import controller.Controller;
import controller.observer.*;

/**
 * Janela do Jogador
 * É inicializada no meio da tela, e contem o fundo da mesa, baralho
 * e mão do jogador.
 */

public class FrameJogador extends JFrame implements ActionListener, ObservadorAPI {
    public final int COMPRIMENTO = 900;
    public final int ALTURA = 700;
    private final Image background = Imagem.get("background");
    private final Image cartaAzul = Imagem.get("azul");
    private final Image cartaVermelha = Imagem.get("vermelho");
    protected JButton botaoStand;
    protected JButton botaoSplit;
    protected JButton botaoHit;
    protected JButton botaoSurrender;
    protected JButton botaoDouble;

    protected String[] listaCartas;
    protected Controller controller;
    protected String numJogador;
    protected int idJogador;
    protected int idMao;


    public FrameJogador(Controller controller, String numJogador, int idJogador, int idMao) {
        this.controller = controller;
        this.numJogador = numJogador;
        this.idMao = idMao;
        this.idJogador = idJogador;

        // iniciando o frame
        setBounds(0, 0, 900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        String titulo = String.format("Jogador nº %s [BLACKJACK]", numJogador);
        setTitle(titulo);
        getContentPane().setLayout(null);

        // colocando os botoes
        colocarBotoes();
        mudarEstadoBotoes(false);
        
        setVisible(true);
    }

    // FOI SUBSTITUIDO PELA ATUALIZACAO DENTRO DO notificar()
    /*
    public void adicionarCarta(String carta) {
        listaCartas.add(carta);
        repaint();
    }

    // FOI SUBSTITUIDO PELA ATUALIZACAO DENTRO DO notificar()
    public void substituirCarta(int indice, String carta) {
    	listaCartas.set(indice, carta);
    	repaint();
    }
     */

    public void reiniciarJogador() {
        listaCartas = null;
        repaint();
    }

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

        // desenha o baralho
        int deslocamentoBaralhoX = 30;
        int deslocamentoBaralhoY = 50;
        g2d.drawImage(cartaAzul, deslocamentoBaralhoX, deslocamentoBaralhoY, null);
        g2d.drawImage(cartaVermelha, deslocamentoBaralhoX + 4, deslocamentoBaralhoY + 2, null);
        g2d.drawImage(cartaAzul, deslocamentoBaralhoX + 8, deslocamentoBaralhoY + 4, null);

        // desenha as cartas do jogador
        if (listaCartas != null) {
            int deslocamentoPorCarta = 20;
            int inicio = (COMPRIMENTO - deslocamentoPorCarta * listaCartas.length) / 2;
            int y = 350;
            
            // bota as cartas viradas pra cima
            for (int i = 0; i < listaCartas.length; i++) {
                String[] carta = listaCartas[i].split("-");
                Image imagemCarta = Imagem.get(carta[0], carta[1]);
                g2d.drawImage(imagemCarta, inicio + deslocamentoPorCarta * i, y, null);
            }
            //coloca o total de pontos da mao
        }
    }

    public void fechar() { setVisible(false);}

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();  // pega de onde veio o evento (qual botao)
        if (obj.equals(botaoStand)) {
        	this.controller.fazerJogada(idJogador, "STAND");
        } else if (obj.equals(botaoHit)) {
        	this.controller.fazerJogada(idJogador,"HIT");
        } else if (obj.equals(botaoDouble)) {
        	this.controller.fazerJogada(idJogador,"DOUBLE");
        } else if (obj.equals(botaoSplit)) {
        	this.controller.fazerJogada(idJogador,"SPLIT");
        } else {
        	this.controller.fazerJogada(idJogador,"SURRENDER");
        }
    }

    void colocarBotoes() {
        // criando os botoes
        botaoStand = new JButton("STAND");
        botaoHit = new JButton("HIT");
        botaoDouble = new JButton("DOUBLE");
        botaoSurrender = new JButton("SURRENDER");
        botaoSplit = new JButton("SPLIT");

        // colocando o listener
        botaoStand.addActionListener(this);
        botaoHit.addActionListener(this);
        botaoDouble.addActionListener(this);
        botaoSurrender.addActionListener(this);
        botaoSplit.addActionListener(this);

        // posicionando
        botaoStand.setBounds(700, 30, 150, 30);
        botaoHit.setBounds(700, 80, 150, 30);
        botaoDouble.setBounds(700, 130, 150, 30);
        botaoSurrender.setBounds(700, 180, 150, 30);
        botaoSplit.setBounds(700, 230, 150, 30);
        
        // colocando
        getContentPane().add(botaoStand);
        getContentPane().add(botaoHit);
        getContentPane().add(botaoDouble);
        getContentPane().add(botaoSurrender);
        getContentPane().add(botaoSplit);
    }
    
    void mudarEstadoBotoes(boolean estado) {
        // botando como invisivel ou nao porque eles nao aparecem antes da rodada ser iniciada
        botaoStand.setVisible(estado);
        botaoHit.setVisible(estado);
        botaoDouble.setVisible(estado);
        botaoSurrender.setVisible(estado);
        botaoSplit.setVisible(estado);
        repaint();
    }
    
    public void iniciarRodada() {
    	mudarEstadoBotoes(true);
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
                // TODO: precisa verificar os botoes (se pode acionar ou nao)
                repaint();
            }
            case JogadorAposta: {
                // TODO
            }
            default: {}     // outras notificacoes sao ignoradas
        }
    }
}
