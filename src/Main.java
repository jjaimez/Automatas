
import automata.DFA;
import automata.FA;
import automata.State;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Triple;

public class Main {

	public static void main(String[] args) {
                HashSet<State> hashStates= new HashSet<>() ;
                HashSet<Triple<State, Character, State>> hashTrans= new HashSet<>();
                HashSet<Character> alfabeto= new HashSet<>();
                State inicio= null;
            try {
                FA fa= FA.parse_form_file("/home/nico/Escritorio/hello");

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
                        }

}
