/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

/**
 *
 * @author nico
 */
import java.util.Stack;

/**
 * LL1
 *
 * G -> E$ E -> TK K-> +TK | empty T -> FH H-> *FH | empty F -> (E) | a
 *
 *
 *
 *
 *
 * ========================================= a + * ( ) $
 * -------------------------------------------------------------------- G| G->E$
 * | | | G->E$ | | |
 * -------------------------------------------------------------------- E| E->TK
 * | | | E->TK | | |
 * -------------------------------------------------------------------- K| |
 * K->+TK | | | K-> | K-> |
 * -------------------------------------------------------------------- T| T->FH
 * | | | T->FH | | |
 * -------------------------------------------------------------------- H| | H->
 * | H->*FH | | H-> | H-> |
 * -------------------------------------------------------------------- F| F->a
 * | | | F->(E) | | |
 * --------------------------------------------------------------------
 * ===============================================================================
 */
public class LL1_Parser {
//input

    public String entrada;
    private int indice = -1;
    //Stack
    Stack<String> stack = new Stack<String>();
    //Table of rules
    String[][] tabla = {
        {"E$", null, null, "E$", null, null, null},
        {"TR", null, null, "TR", null, null, null},
        {null, "+TR", null, null, null, "", null},
        {"FY", null, null, "FY", null, null, null},
        {null, null, "", null, null, "", ".FY"},
        {"LY", null, null, "LY", null, null, null},
        {"LY", null, "*G", "LY", null, "", ""},
        {"a", null, null, "(E)", null, null, null}
    };
    String[] nonTerminales = {"E", "R", "T", "Y", "L", "F", "G", "S"};
    String[] terminales = {"a", "+", "*", "(", ")", "$", "."};

    public LL1_Parser(String ent) {
        this.entrada = ent;
    }

    private void pushRule(String rule) {
        for (int i = rule.length() - 1; i >= 0; i--) {
            char ch = rule.charAt(i);
            System.out.println("push rule:" + ch);
            String str = String.valueOf(ch);
            push(str);
        }
    }

    //algorithm
    public void algorithm() {
        push(this.entrada.charAt(0) + "");//
        System.out.println("push: " + this.entrada.charAt(0));
        push("S");
        //Read one token from input
        String token = read();
        System.out.println("token: " + token);
        String top = null;
        do {
            top = this.pop();
            System.out.println("tope: " + top);
            //if top is non-terminal
            if (isNonTerminal(top)) {
                String rule = this.getRule(top, token);
                this.pushRule(rule);
            } else if (isTerminal(top)) {
                if (!top.equals(token)) {
                    error("this token is not corrent , By Grammer rule . Token : (" + token + ")");
                } else {
                    System.out.println("Matching: Terminal :( " + token + " )");
                    token = read();
                    //top=pop();
                }
            } else {
                error("Never Happens , Because top : ( " + top + " )");
            }
            if (token.equals("$")) {
                break;
            }
            //if top is terminal
        } while (true);//out of the loop when $
        //accept
        if (token.equals("$")) {
            System.out.println("Input is Accepted by LL1");
        } else {
            System.out.println("Input is not Accepted by LL1");
        }
    }

    private boolean isTerminal(String s) {
        for (int i = 0; i < this.terminales.length; i++) {
            if (s.equals(this.terminales[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean isNonTerminal(String s) {
        for (int i = 0; i < this.nonTerminales.length; i++) {
            if (s.equals(this.nonTerminales[i])) {
                return true;
            }
        }
        return false;
    }

    private String read() {
        indice++;
        char ch = this.entrada.charAt(indice);
        String str = String.valueOf(ch);
        return str;
    }

    private void push(String s) {
        this.stack.push(s);
    }

    private String pop() {
        return this.stack.pop();
    }

    private void error(String message) {
        System.out.println(message);
        throw new RuntimeException(message);
    }

    public String getRule(String non, String term) {
        int row = getnonTermIndex(non);
        int column = getTermIndex(term);
        String rule = this.tabla[row][column];
        if (rule == null) {
            error("There is no Rule by this , Non-Terminal(" + non + ") ,Terminal(" + term + ") ");
        }
        return rule;
    }

    private int getnonTermIndex(String non) {
        for (int i = 0; i < this.nonTerminales.length; i++) {
            if (non.equals(this.nonTerminales[i])) {
                return i;
            }
        }
        error(non + " is not NonTerminal");
        return -1;
    }

    private int getTermIndex(String term) {
        for (int i = 0; i < this.terminales.length; i++) {
            if (term.equals(this.terminales[i])) {
                return i;
            }
        }
        error(term + " is not Terminal");
        return -1;
    }

    //main
    public static void main(String[] args) {
        // TODO code application logic here
        LL1_Parser parser = new LL1_Parser("(a)+(a)$");//i*i+(i+i)$
        parser.algorithm();

    }
}
