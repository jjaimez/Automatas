
import automata.DFA;
import automata.FA;
import automata.NFA;
import automata.State;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Triple;

public class Main {

    public static void main(String[] args) throws Exception {
        HashSet<State> hashStates = new HashSet<>();
        HashSet<State> hashFinales = new HashSet<>();
        HashSet<Triple<State, Character, State>> hashTrans = new HashSet<>();
        HashSet<Character> alfabeto = new HashSet<>();
        State inicio = new State("q0");
        State s1 = new State("q1");
        State s2 = new State("q2");
        State s3 = new State("q3");
        State s4 = new State("q4");
        hashStates.add(s1);
        hashStates.add(inicio);
        hashStates.add(s2);
        hashStates.add(s3);
        hashStates.add(s4);
        hashFinales.add(s2);
        hashFinales.add(s4);
        alfabeto.add('0');
        alfabeto.add('1');
        hashTrans.add(new Triple<State, Character, State>(inicio, '1', s1));
        hashTrans.add(new Triple<State, Character, State>(inicio, '1', s3));
        hashTrans.add(new Triple<State, Character, State>(s1, '1', s2));
        hashTrans.add(new Triple<State, Character, State>(s2, '1', s2));
        hashTrans.add(new Triple<State, Character, State>(s2, '0', s2));
        hashTrans.add(new Triple<State, Character, State>(s3, '1', s4));
        NFA nfa = new NFA(hashStates, alfabeto, hashTrans, inicio, hashFinales);
        DFA dfa = nfa.toDFA();
        System.out.println(dfa.to_dot());

        
         FA fa = FA.parse_form_file("/home/nico/Escritorio/autoa");
         DFA dfa1 = (DFA) fa;
         DFA union= dfa1.union(dfa1);
         System.out.print(union.to_dot());
         union.writeDotSourceToFile(union.to_dot(),"/home/nico/Escritorio/union.dot" );
         String fileOutputPath1 = "/home/nico/Escritorio/union";
            String tParam = "-Tjpg";
            String tOParam = "-o";
            String[] cmd = new String[5];
            cmd[0] = "dot";
            cmd[1] = tParam;
            cmd[2] = "/home/nico/Escritorio/union.dot";
            cmd[3] = tOParam;
            cmd[4] = fileOutputPath1;

            Runtime rt = Runtime.getRuntime();
            rt.exec(cmd);
            

//        try {
//            FA fa = FA.parse_form_file("/home/nico/Escritorio/autoa");
//            DFA dfa1 = (DFA) fa;
//            System.out.println(dfa1.complement().to_dot());
//            fa = FA.parse_form_file("/home/nico/Escritorio/autob");
//            DFA dfa2 = (DFA) fa;
//              //  System.out.println(dfa1.verify_string("abbb")+ "=true"); //falso
//            //System.out.println(dfa2.accepts("abbab")+ "=falso"); //falso
//            //System.out.println(dfa1.accepts("abbbbb")+ "=true"); //falso
//
//            String fileOutputPath1 = "/home/nico/Escritorio/auto1.jpg";
//            String fileOutputPath2 = "/home/nico/Escritorio/auto2.jpg";
//            String tParam = "-Tjpg";
//            String tOParam = "-o";
//
//            String[] cmd = new String[5];
//            cmd[0] = "dot";
//            cmd[1] = tParam;
//            cmd[2] = "/home/nico/Escritorio/autoa.dot";
//            cmd[3] = tOParam;
//            cmd[4] = fileOutputPath1;
//
//            Runtime rt = Runtime.getRuntime();
//            rt.exec(cmd);
//            
//            cmd[2] = "/home/nico/Escritorio/autob.dot";
//            cmd[4] = fileOutputPath2;
//            rt.exec(cmd);
//            System.out.println(dfa1.to_dot());
//            System.out.println(dfa2.to_dot());
//            DFA union= dfa1.union(dfa2);
//            System.out.println(union.to_dot());
//            union.writeDotSourceToFile(union.to_dot(),"/home/nico/Escritorio/union.dot");
//            cmd[2] = "/home/nico/Escritorio/union.dot";
//            cmd[4] = "/home/nico/Escritorio/union.jpg";
//            rt.exec(cmd);
//        } catch (Exception ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
