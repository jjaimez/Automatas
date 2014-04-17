package automata;

import static automata.FA.Lambda;
import java.util.Set;

import utils.Triple;

public class NFA extends FA {

    /*
     *  Construction
     */
    // Constructor
    public NFA(
            Set<State> states,
            Set<Character> alphabet,
            Set<Triple<State, Character, State>> transitions,
            State initial,
            Set<State> final_states)
            throws IllegalArgumentException {
        // TODO
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
        // TODO
        return null;
    }

    @Override
    public String to_dot() {
        assert rep_ok();
        // TODO
        return null;
    }

    /*
     *  Automata methods
     */
    @Override
    public boolean accepts(String string) {
        assert rep_ok();
        assert string != null;
        assert verify_string(string);
        // TODO
        return false;
    }

    /**
     * Converts the automaton to a DFA.
     *
     * @return DFA recognizing the same language.
     */
    public DFA toDFA() {
        assert rep_ok();
        // TODO
        return null;
    }

    @Override
    public boolean rep_ok() {
        return (!alphabet.contains(Lambda)) && states.contains(initial) && transitionsAreCorrect();
        // TODO: Check that the alphabet does not contains lambda.
        // TODO: Check that initial and final states are included in 'states'.
        // TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.

    }

    private boolean transitionsAreCorrect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
