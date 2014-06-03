
import automata.DFA;
import automata.FA;
import automata.PDA;




public class Main {

    public static void main(String[] args) throws Exception {  
        DFA pda= (DFA)FA.parse_form_file("test/dfa4");
        DFA pda2= (DFA)FA.parse_form_file("test/dfa4");
        System.out.println(pda.lenguajesIguales(pda2));
        //System.out.println(pda.minimize().to_dot());
        //System.out.println(pda.delta(pda.initial(), 'a', pda.tope()).first().name());
        
    }
}
