/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nico
 */
public class LL1 {

    private int indice = -1;
    private char lookAhead;
    private char[] palabra;

    public LL1(String palabra) {

        this.palabra = palabra.toCharArray();
        if (!palabra.isEmpty()) {
            indice = 0;
            lookAhead = this.palabra[0];
            System.out.println(S());
        }
    }
    /* 
     SD(S → E $)={(, a}
     SD(E → T R)={(, a}
     SD(R → + T R)={	+}
     SD(R → ε)={$, )}
     SD(T → F Y)={(, a}
     SD(Y → . F Y)={	.}
     SD(Y → ε)={+, $, )}
     SD(F → L G)={(, a}
     SD(G → * G)={*}
     SD(G → ε)={., +, $, )}
     SD(L → ( E ))={	(}
     SD(L → a)={a}
     */

    public String S() {

        Pattern pat = Pattern.compile("[a-z]|\\(");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));

        if (mat.matches()) {
            return (E() + Match('$'));
        } else {
            System.err.print("No hay regla S");
            return null;
        }

    }

    public String E() {
        Pattern pat = Pattern.compile("[a-z]|\\(");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (mat.matches()) {
            return (T() + R());
        } else {
            System.err.print("No hay regla E");
            return null;
        }
    }

    public String T() {
        Pattern pat = Pattern.compile("[a-z]|\\(");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (mat.matches()) {
            return (F() + Y());
        } else {
            System.err.print("No hay regla T");
            return null;

        }
    }

    public String F() {
        Pattern pat = Pattern.compile("[a-z]|\\(");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (mat.matches()) {
            return (L() + G());
        } else {
            System.err.print("No hay regla F");
            return null;
        }
    }

    public String L() {
        Pattern pat = Pattern.compile("\\(|[a-z]");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (mat.matches()) {
            if (lookAhead == '(') {
                return (Match('(') + E() + Match(')'));
            } else {

                return (Match(lookAhead));
            }

        } else {
            System.err.print("No hay regla L");
            return null;

        }
    }

    public String R() {
        Pattern pat = Pattern.compile("\\+|$|\\)");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (mat.matches()) {
            if (lookAhead == '+') {
                return (Match('+') + T() + R());
            } else {
                //epsilon
                return ("");
            }
        } else {
            System.out.println(String.valueOf(lookAhead));
            System.err.print("No hay regla E_");
            return null;

        }
    }

    public String Y() {
        Pattern pat = Pattern.compile(".|\\+|$|\\)");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (mat.matches()) {
            if (lookAhead == '.') {
                return (Match('.') + F() + Y());

            } else {
                //epsilon
                return ("");
            }
        } else {
            System.err.print("No hay regla");
            return null;

        }
    }

    public String G() {
        Pattern pat = Pattern.compile("\\*|.|$|\\+|\\)");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (mat.matches()) {
            if (lookAhead == '*') {
                return (Match('*') + G());
            } else {
                //epsilon
                return ("");
            }
        } else {
            System.err.print("No hay regla");
            return null;
        }
    }

    private String Match(char simbolo) {
        if (lookAhead == simbolo) {
            indice++;
            String ret = String.valueOf(lookAhead);
            System.out.println(palabra.length);
            if (indice-2 == palabra.length) {

                lookAhead = palabra[indice];
            }
            return ret;
        } else {
            System.err.print("No machea");
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        LL1 ll1 = new LL1("a+a$");
    }
}
