package model;

import java.util.List;
import java.util.ArrayList;

import model.cartas.*;
import model.*;

public class API {
    public static Baralho baralho;
    public static Dealer dealer;
    public static Jogador jogador;

    public static void iniciar() {
        baralho = new Baralho(2);
        dealer = new Dealer();
        jogador = new Jogador();
    }

    public static void exibir_baralho() {
        baralho.exibir_todos();
    }
}
