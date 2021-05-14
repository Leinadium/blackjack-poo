package views;

import javax.swing.*;
import java.awt.*;

/**
 * Janela do Dealer
 * É inicializada no meio da tela, e contem o fundo da mesa, baralho
 * mão do dealer.
 */
public class FrameDealer extends JFrame {
    public final int ALTURA = 600;
    public final int COMPRIMENTO = 600;

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

        setVisible(true);
    }
}
