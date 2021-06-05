package controller;

import java.io.*;
import java.util.*;



public class Save {
	public static void main (String[] args) throws IOException{
		PrintWriter out = null;
		Scanner s = null;
		Locale l;
		try {
			out = new PrintWriter (new FileWriter("arquivo_teste.txt"));
			l = new Locale.Builder().setLanguage("pt").setScript("Latn").setRegion("BR").build();
		}
		finally {
			// por enquanto nada 
		}
		out.format(l, "teste");
		out.close();
	}
}
