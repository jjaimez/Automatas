/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import java.util.HashSet;
import java.util.Set;
import utils.NumeroAleatorio;
import utils.Triple;

/**
 *
 * @author jacinto
 */


//Las funciones ya estan implementadas, falta usar el parse para dividir la exprecion e ir creandola
public class ER {
    
    public ER(){};

    public NFALambda casoBase(Character c) {
        NumeroAleatorio na = new NumeroAleatorio(8);
        Set<State> newStates = new HashSet();
        Set<Character> newAlphabet = new HashSet();
        newAlphabet.add(c);
        State newInitial = new State("q" + na.generate());
        State newFinal = new State("q" + na.generate());
        Set<Triple<State, Character, State>> newTransitions = new HashSet();
        newTransitions.add(new Triple(newInitial, c, newFinal));
        Set<State> newFinal_states = new HashSet();
        newFinal_states.add(newFinal);
        newStates.add(newFinal);
        newStates.add(newInitial);
        return new NFALambda(newStates, newAlphabet, newTransitions, newInitial, newFinal_states);
    }
}
