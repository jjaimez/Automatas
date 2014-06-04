package automata;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import utils.Pair;
import utils.Triple;

public class Grep {

    public void find(String path) throws Exception {
        FileReader in = null;
        HashSet<Triple<Integer, Integer, Integer>> donde = new HashSet();
        DFA dfa = (DFA) FA.parse_form_file("test/dfa1"); //cambiar esto !!!!
        StringBuilder sb = new StringBuilder();
        boolean estabaAceptando;
        int aux = -1;
        try {
            FileInputStream fis = new FileInputStream(new java.io.File(path).getAbsoluteFile() + ".txt");
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) { //mientras haya renglones
                char[] palabras = line.toCharArray(); //transformo el renglon en un arreglo de caracteres
                for (int j = 0; j < palabras.length ; j++) { //desde un indice j
                    estabaAceptando = false; 
                    StringBuffer cadena = new StringBuffer();
                    for (int x = j; x < palabras.length ; x++) { //arrancando desde j
                        cadena = cadena.append(palabras[x]);
                        if (dfa.accepts(cadena.toString())) { //si acepto la cadena i..j
                            if (!estabaAceptando) { //y no la estaba aceptando
                                estabaAceptando = true;
                                aux = x; //marco el inicio desde donde la empece a aceptar
                            }
                        } else { // si no l aacepto
                            if (estabaAceptando) { //si estaba aceptandola 
                                donde.add(new Triple<Integer, Integer, Integer>(i + 1, j + 1, x));//retorno el renglon, desde donde empezo,hasta donde termino
                                estabaAceptando = false;
                                cadena = new StringBuffer();
                                j = x; //avanzo hasta x que es donde es el final de la cadena que acepto
                            }
                        }
                    }
                    if (estabaAceptando) { //reviso de vuelta por si hasta el final del renglon la aceptaba
                        if (dfa.accepts(cadena.toString())) {
                            donde.add(new Triple<Integer, Integer, Integer>(i + 1, j + 1, palabras.length));
                            estabaAceptando = false;
                        }
                    }
                }
                i++;
            }
            dis.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        String[] line = sb.toString().split("\n");
        for (Triple<Integer, Integer, Integer> p : donde) {
            System.out.println("reng: " + p.first() + " desde: " + p.second() + " hasta : " + p.third());
        }
    }
}
