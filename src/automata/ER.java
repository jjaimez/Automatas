/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import utils.NumeroAleatorio;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import utils.Triple;

/**
 *
 * @author jacinto
 */
public class ER {

    public NFALambda concate(NFALambda nfal1, NFALambda nfal2) {
        boolean cambio = true;
        NumeroAleatorio na = new NumeroAleatorio(7);
        while (cambio) {
            LinkedList lll = new LinkedList();
            for (State s2 : nfal2.states) {
                lll.add(s2);
            }
            while (!lll.isEmpty()) {
                State s2 = (State) lll.pop();
                if (nfal1.states.contains(s2)) {
                    boolean incluido = true;
                    while (incluido) {
                        String string = na.generate();
                        State s3 = new State(s2.name() + string);
                        if ((!nfal1.states.contains(s3)) && (!nfal2.states.contains(s3))) {
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
        newStates.addAll(nfal1.states);
        newStates.addAll(nfal2.states);
        Set<Character> newAlphabet = new HashSet();
        newAlphabet.addAll(nfal1.alphabet);
        newAlphabet.addAll(nfal2.alphabet);
        Set<Triple<State, Character, State>> newTransitions = new HashSet();
        newTransitions.addAll(nfal1.transitions);
        newTransitions.addAll(nfal2.transitions);
        State newInitial = new State(nfal1.initial.name());
        Set<State> newFinal_states = new HashSet();
        newFinal_states.addAll(nfal2.final_states);
        for (State s : nfal1.final_states) {
            newTransitions.add(new Triple(s, NFALambda.Lambda, nfal2.initial));
        }
        return new NFALambda(newStates, newAlphabet, newTransitions, newInitial, newFinal_states);
    }

   public NFALambda estrella(NFALambda nfal1) {
        NumeroAleatorio na = new NumeroAleatorio(7);
       
        Set<State> newStates = new HashSet();
        newStates.addAll(nfal1.states);
        Set<Character> newAlphabet = new HashSet();
        newAlphabet.addAll(nfal1.alphabet);
        Set<Triple<State, Character, State>> newTransitions = new HashSet();
        newTransitions.addAll(nfal1.transitions);
        boolean contiene = true;
        State newInitial = null;
        while (contiene) {
            contiene = false;
            newInitial = new State(na.generate());
            if (nfal1.states.contains(newInitial)) {
                contiene = true;
            }
        }
        Set<State> newFinal_states = new HashSet();
        contiene = true;
        State finalS = null;
        while (contiene) {
            contiene = false;
            finalS = new State(na.generate());
            if (nfal1.states.contains(finalS)) {
                contiene = true;
            }
        }
        newFinal_states.add(finalS);
        for (State s : nfal1.final_states) {
            newTransitions.add(new Triple(s, NFALambda.Lambda, finalS));
             newTransitions.add(new Triple(s, NFALambda.Lambda, nfal1.initial));
        }
        newTransitions.add(new Triple(newInitial, NFALambda.Lambda, nfal1.initial));
        newStates.add(newInitial);
        newStates.add(finalS);
        newTransitions.add(new Triple(newInitial, NFALambda.Lambda, finalS));
        return new NFALambda(newStates, newAlphabet, newTransitions, newInitial, newFinal_states);
    }
    
      public NFALambda union(NFALambda nfal1, NFALambda nfal2) {
        boolean cambio = true;
        NumeroAleatorio na = new NumeroAleatorio(7);
        while (cambio) {
            LinkedList lll = new LinkedList();
            for (State s2 : nfal2.states) {
                lll.add(s2);
            }
            while (!lll.isEmpty()) {
                State s2 = (State) lll.pop();
                if (nfal1.states.contains(s2)) {
                    boolean incluido = true;
                    while (incluido) {
                        String string = na.generate();
                        State s3 = new State(s2.name() + string);
                        if ((!nfal1.states.contains(s3)) && (!nfal2.states.contains(s3))) {
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
        newStates.addAll(nfal1.states);
        newStates.addAll(nfal2.states);
        Set<Character> newAlphabet = new HashSet();
        newAlphabet.addAll(nfal1.alphabet);
        newAlphabet.addAll(nfal2.alphabet);
        Set<Triple<State, Character, State>> newTransitions = new HashSet();
        newTransitions.addAll(nfal1.transitions);
        newTransitions.addAll(nfal2.transitions);
        boolean contiene = true;
        State newInitial = null;
        while (contiene) {
            contiene = false;
            newInitial = new State(na.generate());
            if (nfal1.states.contains(newInitial) || nfal2.states.contains(newInitial)) {
                contiene = true;
            }
        }
        Set<State> newFinal_states = new HashSet();
        contiene = true;
        State finalS = null;
        while (contiene) {
            contiene = false;
            finalS = new State(na.generate());
            if (nfal1.states.contains(finalS) || nfal2.states.contains(finalS)) {
                contiene = true;
            }
        }
        newFinal_states.add(finalS);
        for (State s : nfal1.final_states) {
            newTransitions.add(new Triple(s, NFALambda.Lambda, finalS));
        }
        for (State s : nfal2.final_states) {
            newTransitions.add(new Triple(s, NFALambda.Lambda, finalS));
        }
        newTransitions.add(new Triple(newInitial, NFALambda.Lambda, nfal2.initial));
        newTransitions.add(new Triple(newInitial, NFALambda.Lambda, nfal1.initial));
        newStates.add(newInitial);
        newStates.add(finalS);
        return new NFALambda(newStates, newAlphabet, newTransitions, newInitial, newFinal_states);
    }
}
