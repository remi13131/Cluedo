package Cluedo;

public class Help {
    
    public void messageAccueil(){
        System.out.println("CLUEDO 0.1\n\n" +
                           "Réalisé par : \n\tJaouad Ouled Moussa\tAla Gharbi\tLamia Elhani\tRémi Dipaola\n" +
                           "\tLicense 3 Informatique - Faculté Jean Perrin de Lens - Université d'Artois\n\n" +
                           "#####################################\n"+
                           "# BIENVENUE DANS LE JEU DE CLUEDO ! #\n"+
                           "#####################################\n\n");
        
    }
    
    public void help(){
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
    
    public void help_menu_game(){
	System.out.println("\nCommands available during a match:\n\n"+
                           "\t* show\n\t\tShow information about the state of the match.\n\n"+
                           "\t* move <type > <card1 > <card2 > <card3 >\n"+
                           "\t\t<type > : Either -suggest- or -accuse- \n"+
                           "\t\t<cardN > : A card.\n\n"+
                           "\t* exit\n"+
                           "\t\tEnd match.\n");
	
    }
    
    public void quit(){
        System.out.println("Merci d'avoir Joué ! Au revoir !");
        System.exit(0);
    }
}
