package automata;

import static automata.FA.Lambda;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import utils.Triple;

/* Implements a DFA (Deterministic Finite Atomaton).
 */
public class DFA extends FA {

    private boolean isDeterministic;

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
        Iterator it = transitions.iterator();
        while (it.hasNext()) {
            Triple t = (Triple) it.next();
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
        ret.append(initial.name());
        ret.append(";\n");
        for (Triple tr : transitions) {
            ret.append(((State) tr.first()).name()); //desde
            ret.append("->");
            ret.append(((State) tr.third()).name()); //hasta
            String aux = " [label=\"" + tr.second() + "\"];\n";
            ret.append(aux);
        }

        for (State s : final_states) {
            ret.append(s.name());
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
        boolean repOk = rep_ok();
        boolean distNul = string != null;
        boolean verify = verify_string(string);
        assert repOk;
        assert distNul;
        assert verify;
        return repOk && distNul && verify;

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
        return states.isEmpty();
    }

    /**
     * Checks the automaton for language infinity.
     *
     * @returns True iff the automaton's language is finite.
     */
    public boolean is_finite() {
        assert rep_ok();
// TODO
        return false;
    }

    /**
     * Returns a new automaton which recognizes the complementary language.
     *
     * @returns a new DFA accepting the language's complement.
     */
    public DFA complement() {
        assert rep_ok();
        HashSet<State> finaStates = new HashSet();
        finaStates.addAll(states);
        finaStates.removeAll(final_states);
        DFA complemento = new DFA(states, alphabet, transitions, initial, finaStates);
        return complemento;
    }

    /**
     * Returns a new automaton which recognizes the kleene closure of language.
     *
     * @returns a new DFA accepting the language's complement.
     */
    public DFA star() {
        assert rep_ok();
// TODO
        return null;
    }

    
    // luego de hacer el producto carteciano quedan Set<Set<State>>, une los nombres de los
    //estados que estan dentro de los sets y deja un Set<State>
    public static Set<State> nameUnion(Set<Set<State>> setSet) {
        Iterator<Set<State>> it = setSet.iterator();
        Set<State> s = new HashSet();
        while (it.hasNext()) {
            Iterator<State> it2 = it.next().iterator();
            LinkedList<State> list = new LinkedList<State>();
            while (it2.hasNext()) {
                list.add(it2.next());
            }
            State s2 = new State(list.get(0).name() + list.get(1).name());
            s.add(s2);
        }
        return s;
    }

    //realiza el producto carteciano entre conjuntos
    private static Set<Set<State>> cartesianProduct(int index, Set<State>... sets) {
        Set<Set<State>> ret = new HashSet<Set<State>>();
        if (index == sets.length) {
            ret.add(new HashSet<State>());
        } else {
            for (Object obj : sets[index]) {
                for (Set<State> set : cartesianProduct(index + 1, sets)) {
                    set.add((State) obj);
                    ret.add(set);
                }
            }
        }
        return ret;
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

        return null;
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
        if ((other.alphabet().size() == alphabet.size()) && (other.alphabet().containsAll(other.alphabet))) {
            Set<Set<State>> newStates = cartesianProduct(0, other.states(), states);
            Set<Set<State>> newFinal = cartesianProduct(0, final_states, other.final_states());
            State newInital = new State(other.initial_state().name() + initial);
            //faltaria las tranciciones
            
        }
        return null;
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
    private boolean isDeterministic() {
        Iterator<Triple<State, Character, State>> it = transitions.iterator();
        Iterator<Triple<State, Character, State>> it1 = transitions.iterator();
        Triple<State, Character, State> t1;
        Triple<State, Character, State> t;
        boolean aux = true;
        while (it.hasNext() && aux) {
            t = it.next();
            while (it1.hasNext() && aux) {
                t1 = it1.next();
                if (((State) (t.first())).equals((State) t1.first()) && !((State) t.third()).equals((State) t1.third()) && (Character) t1.second() == (Character) t.second()) {
                    aux = false;
                }
                return false;
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
    private File writeDotSourceToFile(String str) throws java.io.IOException {
        File temp;
        try {
            temp = File.createTempFile("graph_", ".dot.tmp", new File("/tmp"));
            FileWriter fout = new FileWriter(temp);
            fout.write(str);
            fout.close();
        } catch (Exception e) {
            System.err.println("Error: I/O error while writing the dot source to temp file!");
            return null;
        }
        return temp;
    }
}
