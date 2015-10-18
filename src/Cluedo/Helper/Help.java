package Cluedo.Helper;

import Cluedo.Modele.*;
import java.util.*;

public class Help {
    
    public ArrayList<Card> Cards = new ArrayList<>();
    
    public Help() {
        Cards.add(new Card("Scarlett", "Suspect"));
        Cards.add(new Card("Plum", "Suspect"));
        Cards.add(new Card("Peacack", "Suspect"));
        Cards.add(new Card("Green", "Suspect"));
        Cards.add(new Card("Mustard", "Suspect"));
        Cards.add(new Card("White", "Suspect"));
        Cards.add(new Card("Candlestick", "Weapons"));
        Cards.add(new Card("Dagger", "Weapons"));
        Cards.add(new Card("Pipe", "Weapons"));
        Cards.add(new Card("Revolver", "Weapons"));
        Cards.add(new Card("Rope", "Weapons"));
        Cards.add(new Card("Spanner", "Weapons"));
        Cards.add(new Card("Kitchen", "Rooms"));
        Cards.add(new Card("Ballroom", "Rooms"));
        Cards.add(new Card("Conservatory", "Rooms"));
        Cards.add(new Card("Dining", "Rooms"));
        Cards.add(new Card("Billard", "Rooms"));
        Cards.add(new Card("Library", "Rooms"));
        Cards.add(new Card("Lounge", "Rooms"));
        Cards.add(new Card("Hall", "Rooms"));
        Cards.add(new Card("Study", "Rooms"));
    }
    
    public Card nameToCard(String name){
        int i;
        for (Card card : this.Cards) {
            if(name.toLowerCase().equals(card.getName().toLowerCase())) return card;
        }
        return null;
    }
    
    public static void messageAccueil(){
        System.out.println("CLUEDO 0.1\n\n" +
                           "Réalisé par : \n\tJaouad Ouled Moussa\tAla Gharbi\tLamia Elhani\tRémi Dipaola\n" +
                           "\tLicense 3 Informatique - Faculté Jean Perrin de Lens - Université d'Artois");
        
    }
    
    public static void welcome(){
        System.out.println("\n\n################################\n"+
                           "# WELCOME TO THE CLUEDO GAME ! #\n"+
                           "################################\n\n");
        
    }
    
    public static void help(){
	System.out.println("\nsolo\n"+
                           "\tStart a quick solo match (Human + Computer).\n\n"+
                           "referee\n"+
                           "\tStart refereeing a match.\n\n"+
                           "register\n"+
                           "\tRegister to a match. Its sytax is the following:\n"+
                           "\t\tregister <type > [<name > [, <addr >]]\n"+
                           "\t\t<type > : either 'computer ' or 'human '.\n"+
                           "\t\t<name > : player name.\n"+
                           "\t\t<addr > : IP address of the referee.\n\n"+
                           "exit\n"+
                           "\tExit program.\n\n"+
                           "help\n"+
                           "\tShow this message.\n");
    }
    
    public static void help_menu_game(){
	System.out.println("\nCommands available during a match:\n\n"+
                           "\t* show\n\t\tShow information about the state of the match.\n\n"+
                           "\t* move <type > <card1 > <card2 > <card3 >\n"+
                           "\t\t<type > : Either -suggest- or -accuse- \n"+
                           "\t\t<cardN > : A card.\n\n"+
                           "\t* exit\n"+
                           "\t\tEnd match.\n");
	
    }
    
    public static void quit(){
        System.out.println("Thanks for playing! Goodbye and see you soon!");
        System.exit(0);
    }
}
