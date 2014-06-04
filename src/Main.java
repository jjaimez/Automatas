
import automata.DFA;
import automata.ER;
import automata.FA;
import automata.Grep;
import automata.NFALambda;
import automata.PDA;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Main {

    public static void main(String[] args) throws Exception {  
       // DFA pda= (DFA)FA.parse_form_file("test/dfa5");
       // DFA pda2= (DFA)FA.parse_form_file("test/dfa6");
        //System.out.println(pda.lenguajesIguales(pda2));
       // System.out.println(pda.minimize().to_dot());
       //  System.out.println(pda2.minimize().to_dot());
        //System.out.println(pda.delta(pda.initial(), 'a', pda.tope()).first().name());
       // String in = "((3+1).*1).(0+1)";

////Pattern p = Pattern.compile("\\((.*?)\\)|(.0?)\\*");
//Pattern p = Pattern.compile("\\((.*?)\\)");
//
//Matcher m = p.matcher(in);
//m.find();
////while(m.find()) {
//    System.out.println(m.group(0));
//}
       // Grep grep= new Grep();
        //grep.find("test/texto");
        ER er = new ER();
        NFALambda dfa = er.casoBase('a');
        System.out.println(dfa.to_dot());
        }
}
