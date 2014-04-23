package automata;

import java.io.*;
import java.util.*;
import utils.Triple;

public abstract class FA {

    Set<State> states;
    Set<Character> alphabet;
    Set<Triple<State, Character, State>> transitions;
    State initial;
    Set<State> final_states;
    public static final Character Lambda = '_';

    /* Creation */
    /**
     * Parses and returns a finite automaton form the given file. The type of
     * the automaton returned is the appropriate one for the automaton
     * represented in the file (i.e. if the file contains the representation of
     * an automaton that is non-deterministic but has no lambda transitions,
     * then an instance of NFA must be returned).
     *
     * @param path Path to the file containing the specification of an FA.
     * @return An instance of DFA, NFA or NFALambda, corresponding to the
     * automaton represented in the file.
     * @throws Exception Throws an exception if there is an error during the
     * parsing process.
     */
    public static FA parse_form_file(String path) throws Exception {
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
                    State desdeS = new State(desde);
                    State hastaS = new State(hasta);
                    char character = etiqueta.charAt(0);
                    if (character == Lambda) {
                        tipo = Tipo.NFALambda;
                    }
                    hashStates.add(desdeS);
                    hashStates.add(hastaS);
                    hashAl.add(character);
                    hashTrans.add(new Triple(desdeS, character, hastaS));
                    Iterator<Triple<State, Character, State>> it = hashTrans.iterator();
                    Triple aux;
                    while (it.hasNext() && !afnd) {
                        aux = it.next();
                        if ((desdeS).equals(aux.first()) && !hastaS.equals(aux.third()) && (Character) aux.second() == character) {
                            afnd = true; //es no determinista
                            tipo = Tipo.NFA;
                        }
                    }

                } else {
                    if (sentencias[i].contains("[shape=doublecircle]")) { //estado final!
                        hashFinal.add(new State(sentencias[i].split("\\[")[0]));
                    }
                }


            }
        }

        if (tipo == Tipo.DFA) {
            return new DFA(hashStates, hashAl, hashTrans, inicio, hashFinal);
        } else {
            if (tipo == Tipo.NFA) {
                return new NFA(hashStates, hashAl, hashTrans, inicio, hashFinal);
            } else {
                return new NFALambda(hashStates, hashAl, hashTrans, inicio, hashFinal);
            }
        }
    }

    /*
     * 	State Querying
     */
    /**
     * @return the atomaton's set of states.
     */
    public abstract Set<State> states();

    /**
     * @return the atomaton's alphabet.
     */
    public abstract Set<Character> alphabet();

    /**
     * @return the atomaton's initial state.
     */
    public abstract State initial_state();

    /**
     * @return the atomaton's final states.
     */
    public abstract Set<State> final_states();

    /**
     * Query for the automaton's transition function.
     *
     * @return A state or a set of states (depending on whether the automaton is
     * a DFA, NFA or NFALambda) corresponding to the successors of the given
     * state via the given character according to the transition function.
     */
    public abstract Object delta(State from, Character c);

    /**
     * @return Returns the DOT code representing the automaton.
     */
    public abstract String to_dot();

    /*
     * 	Automata Methods 
     */
    /**
     * Tests whether a string belongs to the language of the current finite
     * automaton.
     *
     * @param string String to be tested for acceptance.
     * @return Returns true iff the current automaton accepts the given string.
     */
    public abstract boolean accepts(String string);

    /**
     * Verifies whether the string is composed of characters in the alphabet of
     * the automaton.
     *
     * @return True iff the string consists only of characters in the alphabet.
     */
    public boolean verify_string(String s) {
        boolean ret = true;
        char[] chars = s.toCharArray();
        int i = 0;
        while (i < chars.length && ret) {
            if (!alphabet().contains(chars[i])) {
                ret = false;
            }
            i++;
        }
        return ret;
    }

    /**
     * @return True iff the automaton is in a consistent state.
     */
    public abstract boolean rep_ok();
}
