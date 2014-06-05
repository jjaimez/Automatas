/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author jacinto
 */
//clase usada para generar numeros aleatorios para los nombres de los estados
public class NumeroAleatorio {

    int indice;

    public NumeroAleatorio(int i) {
        indice = i;
    }

    public String generate() {
        indice = ((17 * indice + 43) % 100);
        return String.valueOf(indice);
    }
}
