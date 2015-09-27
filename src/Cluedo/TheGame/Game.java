package Cluedo.TheGame;

import Cluedo.Helper.*;
import Cluedo.Modele.*;

public class Game {

    // DEFINITION DES VARIABLES GLOBALES
    
    public void launchGame(){
        //Affichage du message de bienvenue
        Help.messageAccueil();
        Help.welcome();
        String choix="none";
        
        //Boucle Principale
        while(!choix.toLowerCase().equals("")){
            choix = Utils.saisieUtilisateur();
            switch(choix.toLowerCase()){
                case "solo": 
                    new Solo().Start(); //On lance une partie Solo
                    Help.welcome();
                break;
                
                case "referee": 
                    System.out.println("\nNot implemented yet.\n");
                break;
                    
                case "register": 
                    System.out.println("\nNot implemented yet.\n");
                break;
                     
                case "help": 
                    Help.help(); //affichage de l'aide
                break;
                                       
                case "exit" : 
                    Help.quit(); 
                break;
                
                default: System.out.println("\nBad command.\n"); Help.help(); break;
            }  
        }
    }
}
