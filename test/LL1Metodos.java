import static org.junit.Assert.*;

import org.junit.Test;

import automata.DFA;
import automata.FA;
import automata.LL1;


public class LL1Metodos { 

	
	@Test
	public void test1() throws Exception {
		LL1 ll1 = new LL1("a+(b+c)*");
		assertTrue(ll1.ejecutar());
	}

        	@Test
	public void test2() throws Exception {
		LL1 ll1 = new LL1("a+b+c)*");
		assertFalse(ll1.ejecutar());
	}

        	@Test
	public void test3() throws Exception {
		LL1 ll1 = new LL1("a+(b+c)*.g");
		assertTrue(ll1.ejecutar());
	}
        
                	@Test
	public void test4() throws Exception {
		LL1 ll1 = new LL1("a.b");
		assertTrue(ll1.ejecutar());
	}
}
