/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import static automata.FA.Lambda;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import utils.Triple;

/**
 *
 * @author jacinto
 */
public class Grep {

    public void find(String path) throws Exception {
        FileReader in = null;
        boolean afnd = false;

        HashSet<State> hashStates = new HashSet<>();
        HashSet<Triple<State, Character, State>> hashTrans = new HashSet<>();
        HashSet<Character> hashAl = new HashSet<>();
        HashSet<State> hashFinal = new HashSet<>();
        State inicio = null;

        Tipo tipo = Tipo.DFA;
        StringBuilder sb = new StringBuilder();

        try {
            FileInputStream fis = new FileInputStream(new java.io.File(path).getAbsoluteFile() + ".dot");
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            dis.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        String ret = sb.toString();
        ret = ret.replaceAll("\n", ";");
        String[] renglones = ret.split(";");
        for (int i = 0; i < renglones.length; i++) {
            char[] palabras = renglones[i].toCharArray();
            for (int j = 0; j < palabras.length /*MENOS LA LONGUTUD DEL DFA*/; j++) {
               StringBuffer cadena = new StringBuffer();
                for (int x = j; x < j/*LA LONGUTUD DEL DFA*/; x++) {
                    cadena = cadena.append(palabras[x]);
                }
                //IF (DFA.ACEPTS(cadena.toString())) THEN RETURN RENGLON,J
            }

        }

    }
}
