/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */

package views;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FrameNomes extends JFrame implements ActionListener {
    final int COMPRIMENTO = 300;
    final int ESPACO = 20;
    final int LARGURA = 30;
    int ALTURA;

    Controller controller;
    int quantidadeJogadores;

    JButton botaoIniciarPartida;
    ArrayList<JTextField> campoTexto;

    public FrameNomes(Controller controller, int q) {
        this.controller = controller;
        this.quantidadeJogadores = q;
        this.ALTURA = (LARGURA + ESPACO) * (q + 1) + ESPACO;

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int comp = screenSize.width;
        int altu = screenSize.height;
        int x = (comp - COMPRIMENTO) / 2;
        int y = (altu - ALTURA) / 2;

        // coloca no meio da tela
        setBounds(x, y, COMPRIMENTO + 30, ALTURA + 50);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Escolha os nomes dos jogadores");

        // adicinando os campos
        campoTexto = new ArrayList<>();
        for (int i = 0; i < q; i ++) {
            JTextField j = new JTextField("Jogador " + (i+1));
            j.setAlignmentX(Component.CENTER_ALIGNMENT);
            j.setBounds(20, ESPACO + (ESPACO + LARGURA) * i,
                    COMPRIMENTO - 40, LARGURA);
            getContentPane().add(j);
            campoTexto.add(j);
        }
        // adicinando o botao
        botaoIniciarPartida = new JButton("Iniciar Partida");
        botaoIniciarPartida.setBounds(50, ESPACO + (ESPACO + LARGURA) * q,
                COMPRIMENTO - 100, LARGURA);
        botaoIniciarPartida.addActionListener(this);
        getContentPane().add(botaoIniciarPartida);
        getContentPane().setLayout(null);

    }

    public void abrir() {
        setVisible(true);
    }

    public void fechar() {
        setVisible(false);
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj.equals(botaoIniciarPartida)) {
            String[] nomes = new String[quantidadeJogadores];
            for (int i = 0; i < quantidadeJogadores; i ++) {
                nomes[i] = campoTexto.get(i).getText();
            }
            this.controller.iniciarPartida(quantidadeJogadores, nomes);
        }
    }
}
