package Cluedo.Helper;

import java.util.*;
import java.util.regex.*;

public class Utils {
    
    public static String saisieUtilisateur(){
        //Permet à l'utilisateur d'entrer son choix dans l'invite de commande
        System.out.println("\nType \"help\" for some information.");
        Scanner sc = new Scanner(System.in);
        System.out.println(" > ");
        String str = sc.nextLine();
        return str;
    }
    
    public static boolean isValidInt(String toTest) {
        Pattern p = Pattern.compile("(\\d)+");
        Matcher m = p.matcher(toTest);
        if (m.lookingAt() && m.start() == 0 && m.end() == (toTest.length())) return true;
        return false;
    }
}
