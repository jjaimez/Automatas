/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automata;

import java.util.HashSet;

/**
 *
 * @author nico
 */
public class LL1 {
    
   // String [] nonTers={"E","T","F","L"};
    HashSet<Character> noTerminales;
    HashSet<Character> terminales;
    String [] terminals={".","*","(", ")", "a","b","c","d","e","f","g","h","i","j","k","l","m","n","ñ","o","p","q","r","s","t","u","v","w","x","y","z"};
    HashSet<Character[]> producciones;
    HashSet<String> produccionesOrig;
    String  prod1="E→E|T|T";
    String  prod2 ="T→T·F|F";
    String  prod3="F→F*|L";
    String  prod4 ="L→(E)|a|b|c|d|e";

    public LL1() {
        this.noTerminales = new HashSet<Character>( );

        noTerminales.add('E');
        noTerminales.add('T');
        noTerminales.add('F');
        noTerminales.add('L');
   this.terminales = new HashSet<Character>( );

        terminales.add('.');
        terminales.add('*');
         terminales.add('(');
          terminales.add(')');
        terminales.add('a');
        terminales.add('b');
        terminales.add('c');
        terminales.add('d');
        terminales.add('e');
        producciones= new HashSet<>();
        produccionesOrig= new HashSet<>();
        produccionesOrig.add(prod1);
        produccionesOrig.add(prod2);
        produccionesOrig.add(prod3);
        produccionesOrig.add(prod4);

                                
        
                
    }

    private HashSet<String[]> obtenerProducciones(){
        HashSet<String[]> ret= new HashSet<>();
        for(String prod: produccionesOrig){
            String[] a= prod.split("→");
           // System.out.println(a[1]);
            char[] b= a[1].toCharArray();
            //System.out.println("asdasda "+b[0]+b[1]);
            int i=0;
            while(i<b.length){
                if( b[i]!='|'){
                    String[] array= new String[3];
                    array[0]=a[0];
                    array[1]= "→";
                    //System.out.println(a[0]+" → "+b[i]);
                    array[2]= String.valueOf(b[i]);
                    ret.add(array);
                }
                
              i++;  
            }
        }
        return ret;
    }




private HashSet<Character> primero(Character x){
    HashSet<Character> ret= new HashSet<>();
    HashSet<Character>retViejo= new HashSet<>();
    boolean primerCiclo=true;
    if(terminales.contains(x)){
        ret.add(x);
        System.out.println("es terminal!");
        return ret;
        
    }
    else{
        while(!sonIguales(ret, retViejo)||primerCiclo){
            primerCiclo=false;
        for(Character[] prod: producciones){
            System.out.println(prod[0]+""+prod[1]+""+prod[2]);
            if(prod[0]==x){
                int i=2;
                //System.out.println("x: "+x);
                while(i<prod.length){
                    if(prod[i]!='|' && prod[i]!=x){
                        retViejo=(HashSet<Character>) clonar(ret);
                        //System.out.println(prod[i]);
                        ret.addAll(primero(prod[i]));
                    }
                    i++;
                }
                
            }
        }
       
        
        }
        
    }
    
    return ret;

}

public HashSet<Character> clonar(HashSet<Character> aClonar){
    HashSet<Character> ret= new HashSet<>();
    for(Character c: aClonar){
        ret.add(c.charValue());
    }
    return ret;
}

public boolean sonIguales (HashSet<Character> a, HashSet<Character> b){
    if(a.size() ==b.size() ){
        for(Character c: a){
            if(!b.contains(c) )
                return false;
        }
        return true;
    }
    return false;
}

public static void main(String[] args) throws Exception {
    LL1 ll1= new LL1();
   // HashSet<Character> primeros=ll1.primero('L');
   // System.out.println(primeros);
   HashSet<String[]>a=ll1.obtenerProducciones();
   for(String[] as: a){
       for(String c: as){
       System.out.print(c);
       }
       System.out.println();
   }
}
}