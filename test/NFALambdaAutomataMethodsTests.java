import static org.junit.Assert.*;

import org.junit.Test;

import automata.NFALambda;
import automata.FA;


public class NFALambdaAutomataMethodsTests { 

	// Tests for NFA1
	
	@Test
	public void test1() throws Exception {
		NFALambda nfa = (NFALambda) FA.parse_form_file("test/nfalambda1");
		assertTrue(nfa.accepts("casa"));
	}
	
	@Test
	public void test2() throws Exception {
		NFALambda nfa = (NFALambda) FA.parse_form_file("test/nfalambda1");
		assertTrue(nfa.accepts("sa"));
	}
	
	@Test
	public void test3() throws Exception {
		NFALambda nfa = (NFALambda) FA.parse_form_file("test/nfalambda1");
		assertTrue(nfa.accepts(""));
	}

	@Test
	public void test4() throws Exception {
		NFALambda nfa = (NFALambda) FA.parse_form_file("test/nfalambda1");
		assertFalse(nfa.accepts("asac"));
	}
	
	// Tests for NFA2
	
	@Test
	public void test5() throws Exception {
		NFALambda nfa = (NFALambda) FA.parse_form_file("test/nfalambda2");
		assertTrue(nfa.accepts("casacasacasa"));
	}
	
	@Test
	public void test6() throws Exception {
		NFALambda nfa = (NFALambda) FA.parse_form_file("test/nfalambda2");
		assertTrue(nfa.accepts("casa"));
	}	
	
	@Test
	public void test7() throws Exception {
		NFALambda nfa = (NFALambda) FA.parse_form_file("test/nfalambda2");
		assertFalse(nfa.accepts(""));
	}
	
}
