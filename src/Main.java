
import automata.DFA;
import automata.FA;
import automata.State;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Triple;

public class Main {

    public static void main(String[] args) {
        HashSet<State> hashStates = new HashSet<>();
        HashSet<Triple<State, Character, State>> hashTrans = new HashSet<>();
        HashSet<Character> alfabeto = new HashSet<>();
        State inicio = null;
        try {
            FA fa = FA.parse_form_file("/home/nico/Escritorio/autoa");
            DFA dfa1 = (DFA) fa;
            System.out.println(dfa1.complement().to_dot());
            fa = FA.parse_form_file("/home/nico/Escritorio/autob");
            DFA dfa2 = (DFA) fa;
              //  System.out.println(dfa1.verify_string("abbb")+ "=true"); //falso
            //System.out.println(dfa2.accepts("abbab")+ "=falso"); //falso
            //System.out.println(dfa1.accepts("abbbbb")+ "=true"); //falso

            String fileOutputPath1 = "/home/nico/Escritorio/auto1.jpg";
            String fileOutputPath2 = "/home/nico/Escritorio/auto2.jpg";
            String tParam = "-Tjpg";
            String tOParam = "-o";

            String[] cmd = new String[5];
            cmd[0] = "dot";
            cmd[1] = tParam;
            cmd[2] = "/home/nico/Escritorio/autoa.dot";
            cmd[3] = tOParam;
            cmd[4] = fileOutputPath1;

            Runtime rt = Runtime.getRuntime();
            rt.exec(cmd);
            
            cmd[2] = "/home/nico/Escritorio/autob.dot";
            cmd[4] = fileOutputPath2;
            rt.exec(cmd);
            System.out.println(dfa1.to_dot());
            System.out.println(dfa2.to_dot());
            DFA union= dfa1.union(dfa2);
            System.out.println(union.to_dot());
            union.writeDotSourceToFile(union.to_dot(),"/home/nico/Escritorio/union.dot");
            cmd[2] = "/home/nico/Escritorio/union.dot";
            cmd[4] = "/home/nico/Escritorio/union.jpg";
            rt.exec(cmd);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
