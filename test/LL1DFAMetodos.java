
import static org.junit.Assert.*;

import org.junit.Test;

import automata.DFA;
import automata.DFALL1;
import automata.FA;
import automata.LL1;

public class LL1DFAMetodos {

    @Test
    public void test1() throws Exception {
        LL1 ll1 = new LL1("(b+c)*");
        assertTrue(ll1.ejecutar());
        DFALL1 dfall1 = new DFALL1("(b+c)*");
        DFA dfa = dfall1.ejecutar();
        assertTrue(dfa.accepts("bbbb"));
        assertTrue(dfa.accepts("ccccc"));
        assertTrue(dfa.accepts("bcbcbcbcbcbcc"));
    }

    @Test
    public void test3() throws Exception {
        LL1 ll1 = new LL1("(b+c)*.g");
        assertTrue(ll1.ejecutar());
        DFALL1 dfall1 = new DFALL1("(b+c)*.g");
        DFA dfa = dfall1.ejecutar();
        assertFalse(dfa.accepts("bbbb"));
        assertFalse(dfa.accepts("ccccc"));
        assertTrue(dfa.accepts("bcbcbcbcbcbccg"));
        assertFalse(dfa.accepts("bcbcbcbcbcbccgg"));
    }

    @Test
    public void test4() throws Exception {
        LL1 ll1 = new LL1("a.b");
        assertTrue(ll1.ejecutar());
        DFALL1 dfall1 = new DFALL1("a.b");
        DFA dfa = dfall1.ejecutar();
        assertFalse(dfa.accepts("aab"));
        assertFalse(dfa.accepts("ba"));
        assertTrue(dfa.accepts("ab"));
    }

    @Test
    public void test5() throws Exception {
        LL1 ll1 = new LL1("a+b");
        assertTrue(ll1.ejecutar());
        DFALL1 dfall1 = new DFALL1("a+b");
        DFA dfa = dfall1.ejecutar();
        assertFalse(dfa.accepts("aab"));
        assertFalse(dfa.accepts("ba"));
        assertTrue(dfa.accepts("a"));
        assertTrue(dfa.accepts("b"));

    }

    @Test
    public void test6() throws Exception {
        LL1 ll1 = new LL1("a*");
        assertTrue(ll1.ejecutar());
        DFALL1 dfall1 = new DFALL1("a*");
        DFA dfa = dfall1.ejecutar();
        assertTrue(dfa.accepts("a"));
        assertTrue(dfa.accepts("aaaaaaa"));
        assertTrue(dfa.accepts(""));

    }

    @Test
    public void test7() throws Exception {
        LL1 ll1 = new LL1("(a.b)*");
        assertTrue(ll1.ejecutar());
        DFALL1 dfall1 = new DFALL1("(a.b)*");
        DFA dfa = dfall1.ejecutar();
        assertFalse(dfa.accepts("aab"));
        assertFalse(dfa.accepts("ababa"));
        assertTrue(dfa.accepts("ab"));
        assertTrue(dfa.accepts(""));
        assertTrue(dfa.accepts("abab"));
        assertTrue(dfa.accepts("ababababababab"));

    }
    
        @Test
    public void test8() throws Exception {
        LL1 ll1 = new LL1("a+(b+c)*");
        assertTrue(ll1.ejecutar());
        DFALL1 dfall1 = new DFALL1("a+(b+c)*");
        DFA dfa = dfall1.ejecutar();
        assertFalse(dfa.accepts("abc"));
        assertTrue(dfa.accepts(""));
        assertTrue(dfa.accepts("b"));
        assertTrue(dfa.accepts("c"));
        assertTrue(dfa.accepts("bcbcbcbcbcb"));
        assertFalse(dfa.accepts("ababababababab"));

    }
    
            @Test
    public void test9() throws Exception {
        LL1 ll1 = new LL1("a+(b+c)*.g");
        assertTrue(ll1.ejecutar());
        DFALL1 dfall1 = new DFALL1("a+(b+c)*.g");
        DFA dfa = dfall1.ejecutar();
        assertFalse(dfa.accepts("abc"));
        assertTrue(dfa.accepts("g"));
        assertFalse(dfa.accepts("ag"));
        assertTrue(dfa.accepts("bg"));
        assertTrue(dfa.accepts("bcbcbcbcbcbg"));
        assertFalse(dfa.accepts("bcbcbcbcb"));

    }
}
