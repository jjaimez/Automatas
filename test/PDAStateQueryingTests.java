import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import utils.Triple;
import automata.DFA;
import automata.PDA;
import automata.State;
import utils.Pair;
import utils.Quadruple;


public class PDAStateQueryingTests {

	private PDA dummy_pda;
	
	@Before
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setUp() throws Exception {
		Set<State> states = new HashSet<State>();
		Set<Character> alpha = new HashSet<Character>();
                Set<Character> alphaStack = new HashSet<Character>();
		HashSet<Quadruple<State, Character,Character, Pair<State,String>>> transitions = new HashSet<>();
		State initial;
		Set<State> finals = new HashSet<State>();
		
		State s0 = new State("s0");
		State s1 = new State("s1");
		State s2 = new State("s2");
		states.add(s0);
		states.add(s1);
		states.add(s2);
		alpha.add('a');
		alpha.add('b');
                
		transitions.add(new Quadruple<>(s0, 'a', '@', new Pair<State,String>(s1,"a")));
		transitions.add(new Quadruple<>(s1, 'a', 'a', new Pair<State,String>(s1,"aa")));
                transitions.add(new Quadruple<>(s1, 'b', 'a', new Pair<State,String>(s2,"_")));
                transitions.add(new Quadruple<>(s2, 'b', 'b', new Pair<State,String>(s2,"_")));



		initial = s0;
		finals.add(s2);
		
		dummy_pda = new PDA(states, alpha,alphaStack, transitions, initial, finals);
	}
	
	@Test
	public void test1() {
		assertTrue(dummy_pda.states().size() == 3);	
	}
	
	@Test
	public void test2() {
		assertTrue(dummy_pda.states().contains(dummy_pda.initial_state()));	
	}
	
	@Test
	public void test3() {
		assertTrue(dummy_pda.states().containsAll(dummy_pda.final_states()));	
	}
	
	
	@Test
	public void test4() {
		Set<Character> _set = new HashSet<Character>();
		_set.add('a');
		_set.add('b');
		assertTrue(dummy_pda.alphabet().equals(_set));	
	}	
	
	
	
		
		
}
