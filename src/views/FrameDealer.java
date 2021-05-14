package views;

import javax.swing.*;
import java.awt.*;

import views.Imagem;

/**
 * Janela do Dealer
 * É inicializada no meio da tela, e contem o fundo da mesa, baralho
 * mão do dealer.
 */
public class FrameDealer extends JFrame {
    public final int COMPRIMENTO = 900;
    public final int ALTURA = 700;
    public Image imagemBackground;

    public FrameDealer() {
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

        // colocando o fundo
        // imagemBackground = Imagem.get("dealer");
        getContentPane().add(new PainelDealer());

        setVisible(true);
    }
}

class PainelDealer extends JPanel {
    private final Image background = Imagem.get("dealer");
    private final Image cartaAzul = Imagem.get("azul");
    private final Image cartaVermelha = Imagem.get("vermelho");


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // desenha o fundo
        g2d.drawImage(background, 0, 0, null);

        // desenha o baralho
        int deslocamentoBaralhoX = 30;
        int deslocamentoBaralhoY = 50;
        g2d.drawImage(cartaAzul, deslocamentoBaralhoX, deslocamentoBaralhoY, null);
        g2d.drawImage(cartaVermelha, deslocamentoBaralhoX + 2, deslocamentoBaralhoY + 1, null);
        g2d.drawImage(cartaAzul, deslocamentoBaralhoX + 4, deslocamentoBaralhoY + 2, null);

    }
}
