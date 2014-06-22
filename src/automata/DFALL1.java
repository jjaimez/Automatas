package automata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DFALL1 {

    private int indice = -1;
    private char lookAhead;
    private char[] palabra;
    private ER er;
    private NFALambda nfa;

    public DFALL1(String palabra) {
        palabra = palabra + "#";
        this.palabra = palabra.toCharArray();
        er = new ER();
        if (!palabra.isEmpty()) {
            indice = 0;
            lookAhead = this.palabra[0];
        }

    }

    public DFA ejecutar() {

        return S().toDFA();
    }
    /*
     SD(S → E #)={(, a}
     SD(E → T R)={(, a}
     SD(R → + T R)={	+}
     SD(R → ε)={#, )}
     SD(T → F Y)={(, a}
     SD(Y → . F Y)={	.}
     SD(Y → ε)={+, #, )}
     SD(F → L G)={(, a}
     SD(G → * G)={*}
     SD(G → ε)={., +, #, )}
     SD(L → ( E ))={	(}
     SD(L → a)={a}
     */

    public NFALambda S() {
        Pattern pat = Pattern.compile("[a-z]|\\(");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));

        if (mat.matches()) {
            NFALambda e = E(nfa);
            Match('#');
            return e;
        } else {
            System.err.print("No hay regla S");
            return null;
        }

    }

    public NFALambda E(NFALambda aux) {
        Pattern pat = Pattern.compile("[a-z]|\\(");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (mat.matches()) {
            NFALambda t = T(aux);
            NFALambda r = R(t);
            return r;
        } else {
            System.err.print("No hay regla E");
            return null;
        }
    }

    public NFALambda T(NFALambda aux) {
        Pattern pat = Pattern.compile("[a-z]|\\(");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (mat.matches()) {
            NFALambda f = F(aux);
            NFALambda y = Y(f);
            return y;
        } else {
            System.err.print("No hay regla T");
            return null;
        }
    }

    public NFALambda F(NFALambda aux) {
        Pattern pat = Pattern.compile("[a-z]|\\(");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (mat.matches()) {
            NFALambda l = L(aux);
            NFALambda g = G(l);
            return g;
        } else {
            System.err.print("No hay regla F");
            return null;
        }
    }

    public NFALambda L(NFALambda aux) {
        Pattern pat = Pattern.compile("\\(|[a-z]");
        Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (mat.matches()) {
            if (lookAhead == '(') {
                Match('(');
                NFALambda e = E(aux);
                Match(')');
                return e;
            } else {
                NFALambda base = er.casoBase(lookAhead);
                Match(lookAhead);
                return base;

            }
        } else {
            System.err.print("No hay regla L");
            return null;

        }
    }

    public NFALambda R(NFALambda aux) {
        //Pattern pat = Pattern.compile("\\+|#|\\)");
        //Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (String.valueOf(lookAhead).equals("+") || String.valueOf(lookAhead).equals("#") || String.valueOf(lookAhead).equals(")")) {
            if (lookAhead == '+') {
                Match('+');
                NFALambda t = T(aux);
                NFALambda r = R(t);
                aux.toDFA().union(r.toDFA());
                System.out.println(aux.to_dot());
                System.out.println(r.to_dot());
                NFALambda n = r.union(aux);
                System.out.println("se colgó");
                return n;

            } else {
                return aux;
            }
        } else {
            System.err.print("No hay regla E_");
            return null;
        }
    }

    public NFALambda Y(NFALambda aux) {
        // Pattern pat = Pattern.compile(".|\\+|#|\\)");
        // Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (String.valueOf(lookAhead).equals(".") || String.valueOf(lookAhead).equals("+") || String.valueOf(lookAhead).equals("#") || String.valueOf(lookAhead).equals(")")) {
            if (lookAhead == '.') {

                Match('.');
                NFALambda f = F(aux);
                NFALambda y = Y(f);
                return aux.concate(y);

            } else {
                return aux;
            }
        } else {
            System.err.print("No hay regla Y");
            return null;
        }
    }

    public NFALambda G(NFALambda aux) {
        // Pattern pat = Pattern.compile("*|.|#|\\+|\\)");
        // Matcher mat = pat.matcher(String.valueOf(lookAhead));
        if (String.valueOf(lookAhead).equals(".") || String.valueOf(lookAhead).equals("+") || String.valueOf(lookAhead).equals("#") || String.valueOf(lookAhead).equals(")") || String.valueOf(lookAhead).equals("*")) {
            if (lookAhead == '*') {
                // nfa2=nfa1.concate(nfa2.estrella());
                Match('*');
                NFALambda g = G(aux);
                return aux.estrella();
            } else {
                return aux;
            }
        } else {
            //System.out.println(lookAhead);
            System.err.print("No hay regla G");
            return null;
        }
    }

    private String Match(char simbolo) {
        if (lookAhead == simbolo) {
            indice++;
            String ret = String.valueOf(lookAhead);

            if (simbolo != '#') {
                lookAhead = palabra[indice];

            } else {

            }

            return ret;
        } else {
            System.err.print("No machea");
            return null;
        }
    }

//    public static void main(String[] args) throws Exception {
//        DFALL1 ll1 = new DFALL1("a+(b+c)*.g");
//        System.out.println(ll1.ejecutar().to_dot());
//    }
}
