package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import controller.observer.NotificacoesDealer;
import controller.observer.ObservadoDealer;
import controller.observer.ObservadorDealer;

/**
 * Janela do Dealer
 * É inicializada no meio da tela, e contem o fundo da mesa, baralho
 * mão do dealer.
 */

public class FrameDealer extends JFrame implements ActionListener, ObservadoDealer {
    public final int COMPRIMENTO = 900;
    public final int ALTURA = 700;
    private final Image background = Imagem.get("dealer");
    private final Image cartaAzul = Imagem.get("azul");
    private final Image cartaVermelha = Imagem.get("vermelho");

    protected JButton botaoEncerrar;
    protected JButton botaoNovaRodada;
    protected JButton botaoSalvar;

    protected ArrayList<String> listaCartas;

    protected ArrayList<ObservadorDealer> listaObservadores;
    private NotificacoesDealer notificacao;


    public FrameDealer() {
        this.listaCartas = new ArrayList<>();

        // pegando informacoes da monitor
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        // pegando o meio da tela
        int comp = screenSize.width;
        int altu = screenSize.height;
        int x = (comp - ALTURA) / 2;
        int y = (altu - COMPRIMENTO) / 2;

        // iniciando o frame
        setBounds(x, y, COMPRIMENTO, ALTURA);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Dealer [BLACKJACK]");
        getContentPane().setLayout(null);

        // colocando os botoes
        colocarBotoes();
        setVisible(true);
    }

    public void adicionarCarta(String carta) {
        listaCartas.add(carta);
        repaint();
    }

    public void reiniciarDealer() {
        listaCartas.clear();
        this.botaoNovaRodada.setEnabled(true);
        repaint();
    }

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
        if (listaCartas.size() > 0) {
            int deslocamentoPorCarta = 20;
            int inicio = (COMPRIMENTO/2) - deslocamentoPorCarta * listaCartas.size();
            int y = 350;

            // bota a primeira carta virada pra cima
            String[] carta = listaCartas.get(0).split("-");
            Image imagemCarta = Imagem.get(carta[0], carta[1]);
            g2d.drawImage(imagemCarta, inicio, y, null);

            for (int i = 1; i < listaCartas.size(); i ++) {
                carta = listaCartas.get(i).split("-");
                if (carta[1].equals("ESPADAS") || carta[1].equals("PAUS")) {
                    imagemCarta = cartaAzul;
                } else { imagemCarta = cartaVermelha; }
                g2d.drawImage(imagemCarta, inicio + deslocamentoPorCarta * i, y, null);
            }
        }
    }

    public void fechar() { setVisible(false);}

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();  // pega de onde veio o evento (qual botao)
        if (obj.equals(botaoEncerrar)) {
            notificacao = NotificacoesDealer.PartidaEncerrada;
        } else if (obj.equals(botaoNovaRodada)) {
            notificacao = NotificacoesDealer.RodadaNova;
            this.botaoNovaRodada.setEnabled(false);
        } else {  // botaoSalvar
            notificacao = NotificacoesDealer.PartidaSalva;
        }
        // notifica os observadores
        for (ObservadorDealer x : listaObservadores) { x.notificar(this); }
    }

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

    public void registraObservador(ObservadorDealer o) {
        if (listaObservadores == null) {listaObservadores = new ArrayList<>();}
        listaObservadores.add(o);
    }
    public void retiraObservador(ObservadorDealer o) { listaObservadores.remove(o); }
    public NotificacoesDealer get() {
        return notificacao;
    }


}
