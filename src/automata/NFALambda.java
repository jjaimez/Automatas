package automata;

import static automata.FA.Lambda;
import static automata.NFA.nameUnion;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import utils.NumeroAleatorio;

import utils.Triple;

public class NFALambda extends FA {

    /*
     *  Construction
     */
    // Constructor
    public NFALambda(
            Set<State> states,
            Set<Character> alphabet,
            Set<Triple<State, Character, State>> transitions,
            State initial,
            Set<State> final_states)
            throws IllegalArgumentException {
        this.states = states;
        this.alphabet = alphabet;
        this.alphabet.add(Lambda);
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

    public Set<State> clausure(State from) {
        assert states().contains(from);
        assert alphabet().contains(Lambda);
        LinkedList<Triple<State, Character, State>> trans = new LinkedList<Triple<State, Character, State>>();
        trans.addAll(transitions);
        Set<Triple<State, Character, State>> utilizados = new HashSet<Triple<State, Character, State>>();
        Set<State> s = new HashSet();
        s.add(from);
        while (!trans.isEmpty()) {
            Triple<State, Character, State> t = trans.pop();
            utilizados.add(t);
            if ((s.contains((State) t.first())) && ((Character) t.second()).equals(Lambda)) {
                if (s.add((State) t.third())) {
                    trans.addAll(utilizados);
                }
            }
        }
        return s;
    }

    public Set<State> clausuraExtendida(Set<State> s) {
        Set<State> ret = new HashSet();
        for (State state : s) {
            ret.addAll(clausure(state));
        }
        return ret;
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

    @Override
    public boolean accepts(String string) {
        assert rep_ok();
        assert string != null;
        assert verify_string(string);
        DFA dfa = this.toDFA();
        return dfa.accepts(string);
    }

    /**
     * Converts the automaton to a DFA.
     *
     * @return DFA recognizing the same language.
     */
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
        State newInitial = nameUnion(clausure(initial));
        newStates.add(newInitial);
        LinkedList<State> list = new LinkedList<State>();
        Set<Character> newAlp = new HashSet();
        newAlp.addAll(alphabet);
        newAlp.remove(Lambda);
        list.add(newInitial);
        Set<State> newFinals = new HashSet();
        Set<Triple<State, Character, State>> newTrans = new HashSet();
        while (!list.isEmpty()) {
            State s = list.pop();
            Iterator<Character> iterator = newAlp.iterator();
            while (iterator.hasNext()) {
                Character c = iterator.next();
                if (s.containsComma()) {
                    State state = nameUnion(clausuraExtendida(deltaComma(s, c)));
                    if (state != null) {
                        Triple<State, Character, State> t = new Triple<State, Character, State>(s, c, state);
                        if (newStates.add(state)) {
                            list.add(state);

                        }
                        newTrans.add(t);
                    }
                } else {
                    State state = nameUnion(clausuraExtendida(delta(s, c)));
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
        return new DFA(newStates, newAlp, newTrans, newInitial, newFinals);
    }

    @Override
    public boolean rep_ok() {
        return states.contains(initial) && transitionsAreCorrect();
        // TODO: Check that initial and final states are included in 'states'.
        // TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.

    }

    //Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
    private boolean transitionsAreCorrect() {
        boolean ret = true;
        for (Triple t : transitions) {
            ret = ret && states.contains((State) t.first());
            ret = ret && states.contains((State) t.third());
            ret = ret && (alphabet.contains(((Character) t.second())));
        }
        return ret;
    }

    public NFALambda concate(NFALambda nfal2) {
        boolean cambio = true;
        NumeroAleatorio na = new NumeroAleatorio(7);
        while (cambio) {
            LinkedList lll = new LinkedList();
            for (State s2 : nfal2.states) {
                lll.add(s2);
            }
            while (!lll.isEmpty()) {
                State s2 = (State) lll.pop();
                if (this.states.contains(s2)) {
                    boolean incluido = true;
                    while (incluido) {
                        String string = na.generate();
                        State s3 = new State(s2.name() + string);
                        if ((!this.states.contains(s3)) && (!nfal2.states.contains(s3))) {
                            incluido = false;
                            cambio = false;
                            nfal2.states.add(s3);
                            nfal2.states.remove(s2);
                            if (s2.equals(nfal2.initial)) {
                                nfal2.initial = s3;
                            }
                            if (nfal2.final_states.contains(s2)) {
                                nfal2.final_states.remove(s2);
                                nfal2.final_states.add(s3);
                            }
                            LinkedList ll = new LinkedList();
                            for (Triple t : nfal2.transitions) {
                                ll.add(t);
                            }
                            while (!ll.isEmpty()) {
                                Triple t = (Triple) ll.pop();
                                if (t.third().equals(s2)) {
                                    nfal2.transitions.remove(t);
                                    nfal2.transitions.add(new Triple(t.first(), t.second(), s3));
                                }
                                if (t.first().equals(s2)) {
                                    nfal2.transitions.remove(t);
                                    nfal2.transitions.add(new Triple(s3, t.second(), t.third()));
                                }
                            }
                        }
                    }
                }
            }
        } // TODO LO DE ARRIBA ES IGUAL PARA TODOS, HACE EL CAMBIO DE NOMBRE
        Set<State> newStates = new HashSet();
        newStates.addAll(this.states);
        newStates.addAll(nfal2.states);
        Set<Character> newAlphabet = new HashSet();
        newAlphabet.addAll(this.alphabet);
        newAlphabet.addAll(nfal2.alphabet);
        Set<Triple<State, Character, State>> newTransitions = new HashSet();
        newTransitions.addAll(this.transitions);
        newTransitions.addAll(nfal2.transitions);
        State newInitial = new State(this.initial.name());
        Set<State> newFinal_states = new HashSet();
        newFinal_states.addAll(nfal2.final_states);
        for (State s : this.final_states) {
            newTransitions.add(new Triple(s, NFALambda.Lambda, nfal2.initial));
        }
        return new NFALambda(newStates, newAlphabet, newTransitions, newInitial, newFinal_states);
    }

    public NFALambda estrella() {
        NumeroAleatorio na = new NumeroAleatorio(7);

        Set<State> newStates = new HashSet();
        newStates.addAll(this.states);
        Set<Character> newAlphabet = new HashSet();
        newAlphabet.addAll(this.alphabet);
        Set<Triple<State, Character, State>> newTransitions = new HashSet();
        newTransitions.addAll(this.transitions);
        boolean contiene = true;
        State newInitial = null;
        while (contiene) {
            contiene = false;
            newInitial = new State(na.generate());
            if (this.states.contains(newInitial)) {
                contiene = true;
            }
        }
        Set<State> newFinal_states = new HashSet();
        contiene = true;
        State finalS = null;
        while (contiene) {
            contiene = false;
            finalS = new State(na.generate());
            if (this.states.contains(finalS)) {
                contiene = true;
            }
        }
        newFinal_states.add(finalS);
        for (State s : this.final_states) {
            newTransitions.add(new Triple(s, NFALambda.Lambda, finalS));
            newTransitions.add(new Triple(s, NFALambda.Lambda, this.initial));
        }
        newTransitions.add(new Triple(newInitial, NFALambda.Lambda, this.initial));
        newStates.add(newInitial);
        newStates.add(finalS);
        newTransitions.add(new Triple(newInitial, NFALambda.Lambda, finalS));
        return new NFALambda(newStates, newAlphabet, newTransitions, newInitial, newFinal_states);
    }

    public NFALambda union(NFALambda nfal2) {
        boolean cambio = true;
        NumeroAleatorio na = new NumeroAleatorio(7);
        while (cambio) {
            LinkedList lll = new LinkedList();
            for (State s2 : nfal2.states) {
                lll.add(s2);
            }
            while (!lll.isEmpty()) {
                State s2 = (State) lll.pop();
                if (this.states.contains(s2)) {
                    boolean incluido = true;
                    while (incluido) {
                        String string = na.generate();
                        State s3 = new State(s2.name() + string);
                        if ((!this.states.contains(s3)) && (!nfal2.states.contains(s3))) {
                            incluido = false;
                            cambio = false;
                            nfal2.states.add(s3);
                            nfal2.states.remove(s2);
                            if (s2.equals(nfal2.initial)) {
                                nfal2.initial = s3;
                            }
                            if (nfal2.final_states.contains(s2)) {
                                nfal2.final_states.remove(s2);
                                nfal2.final_states.add(s3);
                            }
                            LinkedList ll = new LinkedList();
                            for (Triple t : nfal2.transitions) {
                                ll.add(t);
                            }
                            while (!ll.isEmpty()) {
                                Triple t = (Triple) ll.pop();
                                if (t.third().equals(s2)) {
                                    nfal2.transitions.remove(t);
                                    nfal2.transitions.add(new Triple(t.first(), t.second(), s3));
                                }
                                if (t.first().equals(s2)) {
                                    nfal2.transitions.remove(t);
                                    nfal2.transitions.add(new Triple(s3, t.second(), t.third()));
                                }
                            }
                        }
                    }
                }
            }
        } // TODO LO DE ARRIBA ES IGUAL PARA TODOS, HACE EL CAMBIO DE NOMBRE
        Set<State> newStates = new HashSet();
        newStates.addAll(this.states);
        newStates.addAll(nfal2.states);
        Set<Character> newAlphabet = new HashSet();
        newAlphabet.addAll(this.alphabet);
        newAlphabet.addAll(nfal2.alphabet);
        Set<Triple<State, Character, State>> newTransitions = new HashSet();
        newTransitions.addAll(this.transitions);
        newTransitions.addAll(nfal2.transitions);
        boolean contiene = true;
        State newInitial = null;
        while (contiene) {                    
            contiene = false;
            newInitial = new State(na.generate()+na.generate());
            if (this.states.contains(newInitial) || nfal2.states.contains(newInitial)) {
                contiene = true;
            }
        }
        Set<State> newFinal_states = new HashSet();
        contiene = true;
        State finalS = null;
        while (contiene) {
            contiene = false;
            finalS = new State(na.generate());
            if (this.states.contains(finalS) || nfal2.states.contains(finalS)) {
                contiene = true;
            }
        }
        newFinal_states.add(finalS);
        for (State s : this.final_states) {
            newTransitions.add(new Triple(s, NFALambda.Lambda, finalS));
        }
        for (State s : nfal2.final_states) {
            newTransitions.add(new Triple(s, NFALambda.Lambda, finalS));
        }
        newTransitions.add(new Triple(newInitial, NFALambda.Lambda, nfal2.initial));
        newTransitions.add(new Triple(newInitial, NFALambda.Lambda, this.initial));
        newStates.add(newInitial);
        newStates.add(finalS);
        return new NFALambda(newStates, newAlphabet, newTransitions, newInitial, newFinal_states);
    }
}
