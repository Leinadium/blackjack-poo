package views;

import controller.Controller;
import controller.observer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class FrameInicial extends JFrame implements ActionListener, ObservadoMenu {
    public final int ALTURA = 130;
    public final int COMPRIMENTO = 300;

    private int quantidadeJogadores = 1;
    protected JButton botaoNovaPartida;
    protected JButton botaoCarregarPartida;
    protected JComboBox<String> comboQuantidadeJogadores;
    protected String[] opcoesJogadores;

    protected ArrayList<ObservadorMenu> listaObservadores;
    private NotificacoesMenu notificacao;

    public boolean ativado;

    public FrameInicial() {
        // pega as informacoes do monitor
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int comp = screenSize.width;
        int altu = screenSize.height;
        int x = (comp - COMPRIMENTO) / 2;
        int y = (altu - ALTURA) / 2;

        setBounds(x, y, COMPRIMENTO, ALTURA);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("BLACKJACK");

        // adicionado os botoes

        botaoNovaPartida = new JButton("Nova partida");
        botaoNovaPartida.addActionListener(this);

        botaoCarregarPartida = new JButton("Carregar Partida");
        botaoCarregarPartida.addActionListener(this);

        opcoesJogadores = new String[]{"1 jogador", "2 jogadores", "3 jogadores", "4 jogadores"};
        comboQuantidadeJogadores = new JComboBox<>(opcoesJogadores);
        comboQuantidadeJogadores.setSelectedIndex(0);
        comboQuantidadeJogadores.addActionListener(this);

        // criando os botoes e colocando no painel do frame
        // adiciona os botoes
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // coloca os botoes nas posicoes
        // https://docs.oracle.com/javase/tutorial/uiswing/layout/group.html
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER) // alinha horizontalmente no centro
                        .addGroup(layout.createSequentialGroup()  // a linha de cima (nova partida e lista)
                                .addComponent(botaoNovaPartida)
                                .addComponent(comboQuantidadeJogadores))
                        .addComponent(botaoCarregarPartida)  // a linha de baixo (botao de carregar)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER) // alinha verticalmente no centro
                                // os tres parametros sao para forcar o tamanho vertical no PREFERRED_SIZE
                                .addComponent(botaoNovaPartida, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)

                                .addComponent(comboQuantidadeJogadores, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))

                        .addComponent(botaoCarregarPartida, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
    }

    public void abrir() {
        this.setVisible(true);
        this.ativado = true;
    }

    public void fechar() {
        this.setVisible(false);
        this.ativado = false;
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        boolean devoNotificar = false;

        if (obj.equals(botaoNovaPartida)) {  // apertou o botao de nova partida
            this.notificacao = NotificacoesMenu.Iniciar;
            devoNotificar = true;

        } else if (obj.equals(botaoCarregarPartida)) {  // apertou o botao de carregar partida
            this.notificacao = NotificacoesMenu.Carregar;
            devoNotificar = true;

        } else {  // mudou a quantidade de jogadores
            JComboBox<String> cb = (JComboBox<String>) obj;
            String quantidade = (String)cb.getSelectedItem();
            this.quantidadeJogadores = Integer.parseInt(String.valueOf(quantidade.charAt(0)));
            System.out.println("quantidade: " + quantidade);
        }
        if (devoNotificar) { for (ObservadorMenu x : listaObservadores) { x.notificar(this); } }

    }

    public void registraObservador(ObservadorMenu o) {
        if (listaObservadores == null) { listaObservadores = new ArrayList<>(); }
        listaObservadores.add(o);
    }
    public void retiraObservador(ObservadorMenu o) {
        listaObservadores.remove(o);
    }
    public NotificacoesMenu get() {
        return notificacao;
    }
    public int getQuantidadeJogadores() { return quantidadeJogadores; }
    public File getArquivo() { return null; }  // ainda nao implementado
}
