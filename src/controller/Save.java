/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */

package controller;

import java.io.*;
import java.util.*;


/**
 * Classe para o salvamento de uma partida
 */
public class Save {
	public static void main (ArrayList<String> args) throws IOException{
		PrintWriter out = null;
		//Scanner s = null;
		Locale l;
		try {
			out = new PrintWriter (new FileWriter("partida_salva.txt"));
			l = new Locale.Builder().setLanguage("pt").setScript("Latn").setRegion("BR").build();
		}
		finally {
			// por enquanto nada 
		}
		for (int i = 0; i < args.size(); i++) {
			out.format(l, args.get(i));
			out.format(l, "\n");
		}
		out.close();
	}
}
