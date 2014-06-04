/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automata;

import static automata.FA.Lambda;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import utils.Pair;
import utils.Quadruple;
import utils.Triple;

/**
 *
 * @author nico
 */
public class PDA {
    Set<State> states;
    Set<Character> alphabet;
    Set<Character> alphabetStack;
    Set<Quadruple<State, Character,Character, Pair<State,String>>> transitions;
    State initial;
    Set<State> final_states;
    Deque<Character> stack = new ArrayDeque<Character>();  
 
    public PDA(Set<State> states, Set<Character> alphabet,Set<Character>alphabetStack, Set<Quadruple <State, Character, Character, Pair<State,String>>>  transitions, State initial,Set<State> final_states)  {
       this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.initial = initial;
        this.final_states = final_states; 
        this.alphabetStack= alphabetStack;
        
    }
    
    public static PDA parse_form_file(String path) throws Exception {
        FileReader in = null;
        boolean afnd = false;

        HashSet<State> hashStates = new HashSet<>();
        HashSet<Quadruple<State, Character,Character, Pair<State,String>>> hashTrans = new HashSet<>();
        
        HashSet<Character> hashAlphab = new HashSet<>();
        HashSet<Character> hashAlphabStack = new HashSet<>();
        hashAlphabStack.add('_');
        HashSet<State> hashFinal = new HashSet<>();
        Deque<Character> pila = new ArrayDeque<Character>(); 
        pila.push('@'); //marca de la cola
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
        ret = ret.replaceAll("\t", ";");
        ret = ret.replaceAll(" ", "");
        String[] sentencias = ret.split(";");
        for (int i = 0; i < sentencias.length; i++) {
            if (sentencias[i].contains("inic->")) {//inicial
                String inicial = sentencias[i].split("->")[1];
                inicio = new State(inicial);
            } else {
                if (sentencias[i].contains("->")) { //transicion
                    String[] estados = sentencias[i].split("->");
                    String desde = estados[0];
                    String hasta = estados[1].split("\\[")[0];
                    String etiqueta = estados[1].split("\"")[1];
                    String[] etiquetaArray=etiqueta.split("/");
                    Character consumo= etiquetaArray[0].charAt(0); //lo que consume
                    Character estadoPila= etiquetaArray[1].charAt(0); //el tope actual de la pila
                    String pushOPop= etiquetaArray[2];  //como quedará la pila luego de la transicion
                    State desdeS = new State(desde); //estado desde
                    State hastaS = new State(hasta); //estado hasta
                    char[] consumoDividido= pushOPop.toCharArray(); //la acciones que deberá realizar luego de la transicion
                    //System.out.println("Desde: "+ desde+" hasta: "+hasta+" consumo: "+ consumo+" estadoPila: "+estadoPila+" pushOPop: "+pushOPop);
   
                    hashStates.add(desdeS);
                    hashStates.add(hastaS);
                    hashAlphab.add(consumoDividido[0]);
                    hashAlphabStack.add(consumoDividido[0]);
                    hashTrans.add(new Quadruple(desdeS, consumo,estadoPila, new Pair<>(hastaS,pushOPop)));
                    

                } else {
                    if (sentencias[i].contains("[shape=doublecircle]")) { //estado final!
                        hashFinal.add(new State(sentencias[i].split("\\[")[0]));
                    }
                }


            }
        }
        return new PDA(hashStates, hashAlphab ,hashAlphabStack, hashTrans, inicio, hashFinal);
        
    }
    
    
    public String to_dot() {
       //assert rep_ok();
       StringBuilder ret = new StringBuilder();
        ret.append("digraph {\n");
        ret.append("inic[shape=point]; \n");
        ret.append("inic->");
        ret.append("\""+initial.name()+"\"");
        ret.append(";\n");
        for (Quadruple qd : transitions) {
            ret.append("\""+((State) qd.first()).name()+"\""); //desde
            ret.append("->");
            ret.append("\""+((State)((Pair) qd.fourth()).first()).name()+"\""); //hasta
            String aux = " [label=\"" + qd.second() +"/"+ qd.third()+"/"+((String)((Pair) qd.fourth()).second())+ "\"];\n";
            ret.append(aux);
        }
        for (State s : final_states) {
            ret.append("\""+s.name()+"\"");
            ret.append("[shape=doublecircle];\n");
        }
        ret.append("}");
        return ret.toString();
    }
    
    public boolean accepts(String string) {
        //assert string!=null;
        char[] carac = string.toCharArray();
        stack = new ArrayDeque<Character>(); //limpio la pila  
        stack.add('@'); //meto la marca de la cola 
        Pair<State,String> avanzo = new Pair<>(initial,stack.peek().toString()); //creo un par que contiene el estado y el tope actual de la pila
        for (int i = 0; i < carac.length; i++) { 
           avanzo = delta(avanzo, carac[i]); //busco las transiciones para cada simbolo del abecedario 
           
            if (avanzo == null) {
                return false; // si no hay una transicion para ese estado y ese caracter retorno false
            }
        
        }
        boolean ret = final_states.contains(avanzo.first()); // me fijo si el estado al que se llegó es final
        return ret;

    }
    
    public Pair<State,String> delta( Pair<State,String> avanzo, Character c ) {
        for (Quadruple<State,Character,Character, Pair<State,String>> t : transitions) { 
                    if (( t.first()).equals(avanzo.first()) && t.second().compareTo(c)==0 && stack.peek().equals(t.third())  ) {//comparo si el estado desde de la transicion es igual al estado que se pasa
                        //tambien me fijo si el caracter que se pasa es igual al de la transicion y si el tope de la pila, es igual a lo que debe tener la pila en la transicion
                char[] consumoDividido= t.fourth().second().toCharArray(); //divido la accion que debera realizarse sobre la pila
                if(consumoDividido.length==1){ // que sea igual a uno significa que o es un push o es un pop
                    if (consumoDividido[0] == Lambda) { //si es igual a lambda es un pop
                         stack.pop();
                    }
                    else{ // si tiene más de un elemento, es un intercambio de valores
                        stack.pop();
                        stack.push(consumoDividido[0]);
                    }
                    }else{
                        stack.pop();
                        int i=0;
                        while(i<consumoDividido.length){
                            stack.push(consumoDividido[i]);
                            i++;
                        }
                            }
                
                return new Pair<>((State)((Pair)t.fourth()).first(),(String)((Pair)t.fourth()).second());
                
            }
           }
        return null;
    }
    

    public Set<State> states() {
        return states;
    }

    public Set<Character> alphabet() {
        // TODO
        return alphabet;
    }

    public State initial_state() {
        // TODO
        return initial;
    }

    public Set<State> final_states() {
        // TODO
        return final_states;
    }

    public Set<Character> alphabetStack(){
        return alphabetStack;
    }


 
 public Character tope(){
     return stack.peek();
 }
    
}

