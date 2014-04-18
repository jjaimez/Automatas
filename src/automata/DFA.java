package automata;

import static automata.FA.Lambda;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import utils.Triple;

/* Implements a DFA (Deterministic Finite Atomaton).
 */
public class DFA extends FA {

    private boolean isDeterministic;

    /*	
     * 	Construction
     */
    // Constructor
    public DFA(Set<State> states, Set<Character> alphabet, Set<Triple<State, Character, State>> transitions,
            State initial, Set<State> final_states) throws IllegalArgumentException {
        this.states=states;
        this.alphabet=alphabet;
        this.transitions=transitions;
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
        // TODO
        return null;
    }

    /*
     *  Automata methods
     */
    @Override
    public boolean accepts(String string) {

        return false;
    }

    /**
     * Converts the automaton to a NFA.
     *
     * @return NFA recognizing the same language.
     */
    public NFA toNFA() {
        // TODO
        return null;
    }

    /**
     * Converts the automaton to a NFALambda.
     *
     * @return NFALambda recognizing the same language.
     */
    public NFALambda toNFALambda() {
        // TODO
        return null;
    }

    /**
     * Checks the automaton for language emptiness.
     *
     * @returns True iff the automaton's language is empty.
     */
    public boolean is_empty() {
        return states.isEmpty();
    }

    /**
     * Checks the automaton for language infinity.
     *
     * @returns True iff the automaton's language is finite.
     */
    public boolean is_finite() {
        // TODO
        return false;
    }

    /**
     * Returns a new automaton which recognizes the complementary language.
     *
     * @returns a new DFA accepting the language's complement.
     */
    public DFA complement() {
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
        // TODO
        return null;
    }

    /**
     * Returns a new automaton which recognizes the union of both languages, the
     * one accepted by 'this' and the one represented by 'other'.
     *
     * @returns a new DFA accepting the union of both languages.
     */
    public DFA union(DFA other) {
        // TODO
        return null;
    }

    /**
     * Returns a new automaton which recognizes the intersection of both
     * languages, the one accepted by 'this' and the one represented by 'other'.
     *
     * @returns a new DFA accepting the intersection of both languages.
     */
    public DFA intersection(DFA other) {
        // TODO
        return null;
    }

    @Override
    public boolean rep_ok() {
        boolean ret= (!alphabet.contains(Lambda)) && states.contains(initial) && transitionsAreCorrect() && isDeterministic();
        Iterator<State> it= final_states.iterator();
        while(it.hasNext()){
                if(!states.contains(it.next()))
                    return false;
           
        }
        return ret;
        // TODO: Check that the alphabet does not contains lambda.
        // TODO: Check that initial and final states are included in 'states'.
        // TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
        // TODO: Check that the transition relation is deterministic.
    }

    // TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
    private boolean transitionsAreCorrect() {
        boolean ret=true;
        for(Triple t: transitions){
            ret=ret&&states.contains((State)t.first());
            ret=ret&&states.contains((State)t.third());
            ret= ret&&alphabet.contains((Character)t.second());
        }
        return ret;
    }

// TODO: Check that the transition relation is deterministic.
   private boolean isDeterministic() {
       Iterator<Triple<State,Character,State>> it= transitions.iterator();
       Iterator<Triple<State,Character,State>>  it1= transitions.iterator();
       Triple<State,Character,State> t1;
       Triple<State,Character,State> t;
       boolean aux=true;
        while(it.hasNext() && aux){
            t=it.next();
            while(it1.hasNext()&&aux){
                t1=it1.next();
                if(((State)(t.first())).equals((State)t1.first()) && !((State)t.third()).equals((State)t1.third()) && (Character)t1.second()==(Character)t.second())
                    aux=false;
                    return false;
            }
                               
                        }
        return true;
    }
}
