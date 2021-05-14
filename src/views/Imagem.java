package views;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Imagem {

    private static final HashMap<String, Image> d = new HashMap<>();
    private static final String f = "src/views/img/";

    public static void carregar() {
        try {
            Toolkit tk = Toolkit.getDefaultToolkit();
            d.put("DOIS-PAUS", ImageIO.read(new File(f+"2c.gif")));
            d.put("TRES-PAUS", ImageIO.read(new File(f+"3c.gif")));
            d.put("QUATRO-PAUS", ImageIO.read(new File(f+"4c.gif")));
            d.put("CINCO-PAUS", ImageIO.read(new File(f+"5c.gif")));
            d.put("SEIS-PAUS", ImageIO.read(new File(f+"6c.gif")));
            d.put("SETE-PAUS", ImageIO.read(new File(f+"7c.gif")));
            d.put("OITO-PAUS", ImageIO.read(new File(f+"8c.gif")));
            d.put("NOVE-PAUS", ImageIO.read(new File(f+"9c.gif")));
            d.put("DEZ-PAUS", ImageIO.read(new File(f+"tc.gif")));
            d.put("VALETE-PAUS", ImageIO.read(new File(f+"jc.gif")));
            d.put("DAMA-PAUS", ImageIO.read(new File(f+"qc.gif")));
            d.put("REI-PAUS", ImageIO.read(new File(f+"kc.gif")));

            d.put("DOIS-OUROS", ImageIO.read(new File(f+"2d.gif")));
            d.put("TRES-OUROS", ImageIO.read(new File(f+"3d.gif")));
            d.put("QUATRO-OUROS", ImageIO.read(new File(f+"4d.gif")));
            d.put("CINCO-OUROS", ImageIO.read(new File(f+"5d.gif")));
            d.put("SEIS-OUROS", ImageIO.read(new File(f+"6d.gif")));
            d.put("SETE-OUROS", ImageIO.read(new File(f+"7d.gif")));
            d.put("OITO-OUROS", ImageIO.read(new File(f+"8d.gif")));
            d.put("NOVE-OUROS", ImageIO.read(new File(f+"9d.gif")));
            d.put("DEZ-OUROS", ImageIO.read(new File(f+"td.gif")));
            d.put("VALETE-OUROS", ImageIO.read(new File(f+"jd.gif")));
            d.put("DAMA-OUROS", ImageIO.read(new File(f+"qd.gif")));
            d.put("REI-OUROS", ImageIO.read(new File(f+"kd.gif")));

            d.put("DOIS-COPAS", ImageIO.read(new File(f+"2c.gif")));
            d.put("TRES-COPAS", ImageIO.read(new File(f+"3c.gif")));
            d.put("QUATRO-COPAS", ImageIO.read(new File(f+"4c.gif")));
            d.put("CINCO-COPAS", ImageIO.read(new File(f+"5c.gif")));
            d.put("SEIS-COPAS", ImageIO.read(new File(f+"6c.gif")));
            d.put("SETE-COPAS", ImageIO.read(new File(f+"7c.gif")));
            d.put("OITO-COPAS", ImageIO.read(new File(f+"8c.gif")));
            d.put("NOVE-COPAS", ImageIO.read(new File(f+"9c.gif")));
            d.put("DEZ-COPAS", ImageIO.read(new File(f+"tc.gif")));
            d.put("VALETE-COPAS", ImageIO.read(new File(f+"jc.gif")));
            d.put("DAMA-COPAS", ImageIO.read(new File(f+"qc.gif")));
            d.put("REI-COPAS", ImageIO.read(new File(f+"kc.gif")));

            d.put("DOIS-ESPADAS", ImageIO.read(new File(f+"2s.gif")));
            d.put("TRES-ESPADAS", ImageIO.read(new File(f+"3s.gif")));
            d.put("QUATRO-ESPADAS", ImageIO.read(new File(f+"4s.gif")));
            d.put("CINCO-ESPADAS", ImageIO.read(new File(f+"5s.gif")));
            d.put("SEIS-ESPADAS", ImageIO.read(new File(f+"6s.gif")));
            d.put("SETE-ESPADAS", ImageIO.read(new File(f+"7s.gif")));
            d.put("OITO-ESPADAS", ImageIO.read(new File(f+"8s.gif")));
            d.put("NOVE-ESPADAS", ImageIO.read(new File(f+"9s.gif")));
            d.put("DEZ-ESPADAS", ImageIO.read(new File(f+"ts.gif")));
            d.put("VALETE-ESPADAS", ImageIO.read(new File(f+"js.gif")));
            d.put("DAMA-ESPADAS", ImageIO.read(new File(f+"qs.gif")));
            d.put("REI-ESPADAS", ImageIO.read(new File(f+"ks.gif")));

            d.put("ficha1", ImageIO.read(new File(f+"ficha 1$.png")));
            d.put("ficha5", ImageIO.read(new File(f+"ficha 5$.png")));
            d.put("ficha10", ImageIO.read(new File(f+"ficha 10$.png")));
            d.put("ficha20", ImageIO.read(new File(f+"ficha 20$.PNG")));
            d.put("ficha50", ImageIO.read(new File(f+"ficha 50$.PNG")));
            d.put("ficha100", ImageIO.read(new File(f+"ficha 100$.PNG")));


            d.put("dealer", ImageIO.read(new File(f+"blackjackBKG.png")));
            d.put("azul", ImageIO.read(new File(f+"deck1.gif")));
            d.put("vermelho", ImageIO.read(new File(f+"deck2.gif")));
        }
        catch (IOException e) {
            System.out.println("Erro carregando arquivo de imagens" + e.toString());
            System.exit(-1);
        }
    }


    /**
     * Retorna uma imagem ja carregada
     * @param x nome da carta, ou "ficha"
     * @param y naipe da carta, ou o numero da ficha
     * @return a imamgem carregada
     */
    public static Image get (String x, String y) {
        if (x.equals("ficha")) {
            return d.get("ficha" + y);
        } else {
          return d.get(x + "-" + y);
        }
    }

    /**
     * Retorna uma imagem ja carregada
     * @param x dealer, azul ou vermelho
     * @return a imagem carregada
     */
    public static Image get(String x) {
        return d.get(x);
    }
}