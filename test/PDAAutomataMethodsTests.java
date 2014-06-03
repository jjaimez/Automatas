import static org.junit.Assert.*;

import org.junit.Test;

import automata.DFA;
import automata.FA;


public class PDAAutomataMethodsTests { 

	// Tests for DFA1
	
	@Test
	public void test1() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("test/pda");
		assertTrue(dfa.accepts("abbb"));
	}
        
        @Test
	public void test2() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("test/pda");
		assertFalse(dfa.accepts("baaab"));
	}

        	@Test
	public void test3() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("test/pda");
		assertFalse(dfa.accepts("aa"));
	}
        
              	@Test
	public void test4() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("test/pda");
		assertTrue(dfa.accepts("ab"));
	}
	
	
}
