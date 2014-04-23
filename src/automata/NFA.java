package automata;

import static automata.FA.Lambda;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import utils.Triple;

public class NFA extends FA {

    /*
     *  Construction
     */
    // Constructor
    public NFA(Set<State> states, Set<Character> alphabet, Set<Triple<State, Character, State>> transitions,
            State initial, Set<State> final_states) throws IllegalArgumentException {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.initial = initial;
        this.final_states = final_states;
        if (!this.states.containsAll(this.final_states)) {
            throw new java.lang.IllegalArgumentException("Los estados finales no estan incluidos en los estados");
        }
        if (!transitionsAreCorrect()) {
            throw new java.lang.IllegalArgumentException("Las transiciones son incorrectas");
        }
        if (!states.contains(initial)) {
            throw new java.lang.IllegalArgumentException("El estado inicial no pertenece a los estados");
        }

    }

    /*
     *	State querying 
     */
    @Override
    public Set<State> states() {
        // TODO
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
    public Set<State> delta(State from, Character c) {
        assert states().contains(from);
        assert alphabet().contains(c);
        Set<State> s = new HashSet();
        for (Triple t : transitions) {
            if (((State) t.first()).equals(from) && ((Character) t.second()).equals(c)) {
                s.add((State) t.third());
            }
        }
        return s;
    }

    public Set<State> deltaComma(State from, Character c) {
        Set<State> ret = new HashSet();
        String aux = from.name().replaceAll("\"", "");
        String[] separar = aux.split(",");
        for (int i = 0; i < separar.length; i++) {
            State s = new State(separar[i].substring(0, separar[i].length()));
            if (!delta(s, c).isEmpty()) {
                ret.addAll(delta(s, c));
            }
        }
        return ret;
    }

    @Override
    public String to_dot() {
        assert rep_ok();
        StringBuilder ret = new StringBuilder();
        ret.append("digraph {\n");
        ret.append("inic[shape=point]; \n");
        ret.append("inic->");
        ret.append("\"" + initial.name() + "\"");
        ret.append(";\n");
        for (Triple tr : transitions) {
            ret.append("\"" + ((State) tr.first()).name() + "\""); //desde
            ret.append("->");
            ret.append("\"" + ((State) tr.third()).name() + "\""); //hasta
            String aux = " [label=\"" + tr.second() + "\"];\n";
            ret.append(aux);
        }

        for (State s : final_states) {
            ret.append("\"" + s.name() + "\"");
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
        assert string != null;
        assert verify_string(string);
        DFA dfa = this.toDFA();
        return dfa.accepts(string);
    }

    public static State nameUnion(Set<State> set) {
        if ((set.isEmpty())) {
            return null;
        } else {
            Iterator<State> it = set.iterator();
            if (set.size() == 1) {
                return it.next();
            } else {
                String concate = "";
                while (it.hasNext()) {
                    State s2 = it.next();
                    if (it.hasNext()) {
                        concate += (s2.name() + ",");
                    } else {
                        concate += (s2.name());
                    }
                }
                State s = new State(concate);
                return s;
            }
        }
    }

    /**
     * Converts the automaton to a DFA.
     *
     * @return DFA recognizing the same language.
     */
    public DFA toDFA() {
        assert rep_ok();
        Set<State> newStates = new HashSet();
        newStates.add(initial);
        LinkedList<State> list = new LinkedList<State>();
        list.add(initial);
        Set<State> newFinals = new HashSet();
        Set<Triple<State, Character, State>> newTrans = new HashSet();
        while (!list.isEmpty()) {
            State s = list.pop();
            Iterator<Character> iterator = alphabet.iterator();
            while (iterator.hasNext()) {
                Character c = iterator.next();
                if (s.containsComma()) {
                    State state = nameUnion(deltaComma(s, c));
                    if (state != null) {
                        Triple<State, Character, State> t = new Triple<State, Character, State>(s, c, state);
                        if (newStates.add(state)) {
                            list.add(state);
                        }
                        newTrans.add(t);
                    }
                } else {
                    State state = nameUnion(delta(s, c));
                    if (state != null) {
                        Triple<State, Character, State> t = new Triple<State, Character, State>(s, c, state);
                        if (newStates.add(state)) {
                            list.add(state);
                        }
                        newTrans.add(t);
                    }
                }
            }
        }
        Iterator it = newStates.iterator();
        while (it.hasNext()) {
            boolean existe = false;
            State estados = (State) it.next();
            if (estados != null) {
                if (estados.containsComma()) {
                    String[] separar = estados.name().split(",");
                    for (int i = 0; i < separar.length && !existe; i++) {
                        State s = new State(separar[i].substring(0, separar[i].length()));
                        if (final_states.contains(s)) {
                            existe = true;
                            newFinals.add(estados);
                        }
                    }
                } else {
                    if (final_states.contains(estados)) {
                        newFinals.add(estados);
                    }
                }
            }
        }
        return new DFA(newStates, alphabet, newTrans, initial, newFinals);
    }

    @Override
    public boolean rep_ok() {
        return (!alphabet.contains(Lambda)) && states.contains(initial) && transitionsAreCorrect();
        // TODO: Check that the alphabet does not contains lambda.
        // TODO: Check that initial and final states are included in 'states'.
        // TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.

    }

    // Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
    private boolean transitionsAreCorrect() {
        boolean ret = true;
        for (Triple t : transitions) {
            ret = ret && states.contains((State) t.first());
            ret = ret && states.contains((State) t.third());
            ret = ret && alphabet.contains((Character) t.second());
        }
        return ret;
    }
}
