package Cluedo.TheGame;

import Cluedo.Helper.*;
import Cluedo.Modele.*;
import java.io.IOException;

public class Game {

    // DEFINITION DES VARIABLES GLOBALES
    
    public void launchGame() throws IOException{
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
                    new Referee().Start(12345, 4, 10000);
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
