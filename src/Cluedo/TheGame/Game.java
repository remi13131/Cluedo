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
        String command[];
        //Boucle Principale
        while(!choix.toLowerCase().equals("exit")){
            choix = Utils.saisieUtilisateur("\nType \"help\" for some information.");
            choix = choix.toLowerCase().replaceAll("\\s+", " ");
            command = choix.split(" ");
            switch(command[0]){
                case "solo": 
                    new Solo().Start(); //On lance une partie Solo
                    Help.welcome();
                break;
                
                case "referee": 
                    new Referee().Start(12345, 4, 10000);
                break;
                    
                case "register":
                    String playerName="";
                    String playerType="human";
                    String IPAddress="localhost";
                    if(command.length > 1) playerType=command[1];
                    if(command.length > 2) playerName=command[2];
                    if(command.length > 3) IPAddress=command[3];
                    try {
                        new Register(playerName).Start(IPAddress, 12345);
                    }
                    catch (Exception e) {
                        System.out.println("Connexion closed.");
                    }
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
