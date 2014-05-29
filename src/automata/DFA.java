package automata;

import static automata.FA.Lambda;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import utils.Triple;

/* Implements a DFA (Deterministic Finite Atomaton).
 */
public class DFA extends FA {

    public DFA() {
        this.states = null;
        this.alphabet = null;
        this.transitions = null;
        this.initial = null;
        this.final_states = null;
    }
    /*	
     * 	Construction
     */
    // Constructor

    public DFA(Set<State> states, Set<Character> alphabet, Set<Triple<State, Character, State>> transitions,
            State initial, Set<State> final_states) throws IllegalArgumentException {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.initial = initial;
        this.final_states = final_states;
        if(!this.states.containsAll(this.final_states)){
           throw new java.lang.IllegalArgumentException("Los estados finales no estan incluidos en los estados");  
        }
        if(!transitionsAreCorrect()){
            throw new java.lang.IllegalArgumentException("Las transiciones son incorrectas");
        }
        if(!states.contains(initial)){
            throw new java.lang.IllegalArgumentException("El estado inicial no pertenece a los estados");
        }
        if(!isDeterministic()){
            throw new java.lang.IllegalArgumentException("No es deterministico");
        }
    }

    /*
     *	State querying 
     */
    @Override
    public Set<State> states() {
        return states;
    }

    @Override
    public Set<Character> alphabet() {
        // TODO
        return alphabet;
    }

    @Override
    public State initial_state() {
        // TODO
        return initial;
    }

    @Override
    public Set<State> final_states() {
        // TODO
        return final_states;
    }

    @Override
    public State delta(State from, Character c) {
        assert states().contains(from);
        assert alphabet().contains(c);
        for (Triple t : transitions) {
            if (((State) t.first()).equals(from) && ((Character) t.second()).equals(c)) {
                return (State) t.third();
            }
        }
        return null;
    }

    @Override
    public String to_dot() {
       assert rep_ok();
       StringBuilder ret = new StringBuilder();
        ret.append("digraph {\n");
        ret.append("inic[shape=point]; \n");
        ret.append("inic->");
        ret.append("\""+initial.name()+"\"");
        ret.append(";\n");
        for (Triple tr : transitions) {
            ret.append("\""+((State) tr.first()).name()+"\""); //desde
            ret.append("->");
            ret.append("\""+((State) tr.third()).name()+"\""); //hasta
            String aux = " [label=\"" + tr.second() + "\"];\n";
            ret.append(aux);
        }
        for (State s : final_states) {
            ret.append("\""+s.name()+"\"");
            ret.append("[shape=doublecircle];\n");
        }
        ret.append("}");
        return ret.toString();
    }

    /*
     *  Automata methods
     */
    @Override
    public boolean accepts(String string) {
        assert rep_ok();
        assert string!=null;
        assert verify_string(string);
        char[] carac = string.toCharArray();
        State avanzo = initial;
        for (int i = 0; i < carac.length; i++) {
            avanzo = delta(avanzo, carac[i]);
            if (avanzo == null) {
                return false;
            }
        }
        boolean ret = final_states.contains(avanzo);
        return ret;

    }

    /**
     * Converts the automaton to a NFA.
     *
     * @return NFA recognizing the same language.
     */
    public NFA toNFA() {
        assert rep_ok();
        return new NFA(states, alphabet, transitions, initial, final_states);
    }

    /**
     * Converts the automaton to a NFALambda.
     *
     * @return NFALambda recognizing the same language.
     */
    public NFALambda toNFALambda() {
        assert rep_ok();
// TODO
        return new NFALambda(states, alphabet, transitions, initial, final_states);
    }

    /**
     * Checks the automaton for language emptiness.
     *
     * @returns True iff the automaton's language is empty.
     */
    public boolean is_empty() {
        assert rep_ok();
        ciclos = new HashSet<>();
        return !llegueFinal(initial, false);
    }

    /**
     * Checks the automaton for language infinity.
     *
     * @returns True iff the automaton's language is finite.
     */
    public boolean is_finite() {
        assert rep_ok();
        ciclos = new HashSet<>();
        marcados = new HashSet<>();
        containsCycles(initial);
        marcados = new HashSet<>();
        return !ciclosHastaElfinal(initial, false);
    }

    /**
     * Returns a new automaton which recognizes the complementary language.
     *
     * @returns a new DFA accepting the language's complement.
     */
    public DFA complement() {
        assert rep_ok();
        HashSet<State> finaStates = new HashSet();
        DFA dfaCompleto=this.completarDelta();
        finaStates.addAll(dfaCompleto.states);
        finaStates.removeAll(dfaCompleto.final_states);
        DFA complemento = new DFA(dfaCompleto.states, dfaCompleto.alphabet, dfaCompleto.transitions, dfaCompleto.initial, finaStates);
        return complemento;
    }

    /**
     * Returns a new automaton which recognizes the kleene closure of language.
     *
     * @returns a new DFA accepting the language's complement.
     */
    public DFA star() {
        assert rep_ok();
        HashSet<Triple<State, Character, State>> trans = (HashSet<Triple<State, Character, State>>) transitions;
        HashSet<State> finales = (HashSet<State>) final_states;
        HashSet<State> estados= (HashSet<State>) states;
        State inicial = new State("q0i");
        estados.add(inicial);
        finales.add(inicial);
        for (char c : alphabet) {
            State to = delta(initial, c);
            if (to != null) {
                Triple<State, Character, State> tr = new Triple<>(inicial, c, to);
                trans.add(tr);
                for (State fin : final_states) {
                    Triple<State, Character, State> aux = new Triple<>(fin, c, to);
                    trans.add(aux);
                }
            }
        }
        return new DFA(estados, alphabet, trans, inicial, finales);
    }

    /**
     * Returns a new automaton which recognizes the union of both languages, the
     * one accepted by 'this' and the one represented by 'other'.
     *
     * @returns a new DFA accepting the union of both languages.
     */
    public DFA union(DFA other) {
        assert rep_ok();
        assert other.rep_ok();
         HashSet<State> q =new HashSet<>(); 
         HashSet<Triple<State, Character, State>> trans = new HashSet<>();
         HashSet<Character> alfabeto =(HashSet<Character>)alphabet; 
         alfabeto.addAll(other.alphabet);
        for(State s: states){
            State stateAux= new State(s.name()+"-1");
            q.add(stateAux);
        }
        for(State s: other.states){
            State stateAux= new State(s.name()+"-2");
            q.add(stateAux);
        }
        for(Triple<State,Character,State> t: transitions){
           Triple<State,Character,State> transAux= new Triple<>(new State(t.first().name()+"-1"),t.second(),new State(t.third().name()+"-1"));
            trans.add(transAux);
        }
        for(Triple<State,Character,State> t: other.transitions){
           Triple<State,Character,State> transAux= new Triple<>(new State(t.first().name()+"-2"),t.second(),new State(t.third().name()+"-2"));
            trans.add(transAux);
        }
        State ini= new State("i");
        q.add(ini);
        for(Character chara: alphabet){
            State to= delta(initial, chara);
            if(to!=null){
                Triple<State,Character,State> tri= new Triple<>(ini,chara,new State(to.name()+"-1"));
                trans.add(tri);                
            }
        }
        for(Character chara: other.alphabet){
            State to= other.delta(other.initial, chara);
            if(to!=null){
                Triple<State,Character,State> tri= new Triple<>(ini,chara,new State(to.name()+"-2"));
                trans.add(tri);
            }
        }
        HashSet<State> finales = new HashSet<>();
        for(State s: final_states){
            State stateAux= new State(s.name()+"-1");
            finales.add(stateAux);
        }for(State s: other.final_states){
            State stateAux= new State(s.name()+"-2");
            finales.add(stateAux);
        }
        if(final_states.contains(new State(initial.name()+"-1")) || other.final_states.contains(new State(other.initial.name()+"-2"))){
            finales.add(ini);
        }
        return new NFA(q, alfabeto, trans, ini, finales).toDFA();
    }

    /**
     * Returns a new automaton which recognizes the intersection of both
     * languages, the one accepted by 'this' and the one represented by 'other'.
     *
     * @returns a new DFA accepting the intersection of both languages.
     */
    public DFA intersection(DFA other) {
        assert rep_ok();
        assert other.rep_ok();
        assert alphabet.equals(other.alphabet);
        return (((this.complement()).union(other.complement())).complement());
    }

    @Override
    public boolean rep_ok() {
        boolean ret = (!alphabet.contains(Lambda)) && states.contains(initial) && transitionsAreCorrect() && isDeterministic();
        Iterator<State> it = final_states.iterator();
        while (it.hasNext()) {
            if (!states.contains(it.next())) {
                return false;
            }
        }
        return ret;
        // TODO: Check that the alphabet does not contains lambda.
        // TODO: Check that initial and final states are included in 'states'.
        // TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
        // TODO: Check that the transition relation is deterministic.
    }

    // TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
    private boolean transitionsAreCorrect() {
        boolean ret = true;
        for (Triple t : transitions) {
            ret = ret && states.contains((State) t.first());
            ret = ret && states.contains((State) t.third());
            ret = ret && alphabet.contains((Character) t.second());
        }
        return ret;
    }

// TODO: Check that the transition relation is deterministic.
    public boolean isDeterministic() {
        Iterator<Triple<State, Character, State>> it = transitions.iterator();
        Iterator<Triple<State, Character, State>> it1 = transitions.iterator();
        Triple<State, Character, State> t1;
        Triple<State, Character, State> t;
        boolean aux = true;
        while (it.hasNext() && aux) {
            t = it.next();
            it1 = transitions.iterator();
            while (it1.hasNext() && aux) {
                t1 = it1.next();
                if (((State) (t.first())).equals((State) t1.first()) && !((State) t.third()).equals((State) t1.third()) && (Character) t1.second() == (Character) t.second()) {

                    aux = false;
                    return false;
                }

            }

        }
        return true;
    }

    /**
     * Writes the source of the graph in a file, and returns the written file as
     * a File object.
     *
     * @param str Source of the graph (in dot language).
     * @return The file (as a File object) that contains the source of the
     * graph.
     */
    public File writeDotSourceToFile(String str, String ruta) throws java.io.IOException {
        File temp;
        try {
            temp = new File(ruta);
            FileWriter fout = new FileWriter(temp);
            fout.write(str);
            fout.close();
        } catch (Exception e) {
            System.err.println("Error: I/O error while writing the dot source to temp file!");
            return null;
        }
        return temp;
    }

    //dfs recursivo
    HashSet<State> marcados = new HashSet<>();
    HashSet<State> ciclos = new HashSet<>();

    public void containsCycles(State state) {
        if (state != null) {
            marcados.add(state);
            State aux = null;
            Iterator<Character> itChar = alphabet.iterator();
            while (itChar.hasNext()) {
                aux = delta(state, itChar.next());
                if (aux != null) {
                    if (!marcados.contains(aux)) {
                        containsCycles(aux);
                    } else {
                        ciclos.add(aux);
                    }
                }
            }

        }//end while
    }//end if

    public boolean ciclosHastaElfinal(State state, boolean cycle) {
        if (state != null) {
            marcados.add(state);
            boolean esCiclo = cycle; //el error consitía en que no almacenaba el valor anterior de si
            //poseía un ciclo o no, un ejemplo que antes no pasaba está al final del metodo y ahora si lo pasa
            if (ciclos.contains(state)) {
                esCiclo = true;
            }
            State aux = null;
            Iterator<Character> itChar = alphabet.iterator();
            while (itChar.hasNext() && !cycle) {
                aux = delta(state, itChar.next());
                if (aux != null) {
                    if (final_states.contains(aux) && esCiclo) {
                        return true;
                    }
                    if (!marcados.contains(aux)) {
                        return ciclosHastaElfinal(aux, esCiclo);
                    }
                }
            }
        }
        return cycle;
    }
    
    
    /*
    Ejemplo que antes NO pasaba el test y ahora si
    digraph {
	inic[shape=point];
    inic->q0;
    q0->q1 [label="a"];
    q1->q2 [label="a"];
    q2->q1 [label="b"];
    q1->q3 [label="b"];
    q3->q4 [label="b"];
    q2->q4[label="c"];
    q4[shape=doublecircle];
    q1[shape=doublecircle];
    }
    */

    private boolean llegueFinal(State state, boolean llegue){
        if (state != null) {
            marcados.add(state);
            State aux = null;
            Iterator<Character> itChar = alphabet.iterator();
            while (itChar.hasNext()) {
                aux = delta(state, itChar.next());
                if (aux != null) {
                    if (final_states.contains(aux)) {
                        return true;
                    }
                    if (!marcados.contains(aux)) {
                        containsCycles(aux);
                    } 
                }
            }

        }//end while
        return llegue;
    }
    
    private DFA completarDelta(){
        State trampa= new State("trampa");
        HashSet<State> estados= (HashSet<State>)states;
        estados.add(trampa);
        HashSet<Triple<State,Character,State>> trans= (HashSet<Triple<State, Character, State>>)transitions;
        for(State s: estados){
            for(Character c: alphabet){
                State aux= delta(s, c);
                if(aux==null){
                    Triple<State,Character,State> add= new Triple<>(s,c,trampa);
                    trans.add(add);
                }
            }
        }
        return (new DFA(estados, alphabet, trans, initial, final_states));
    }
}
