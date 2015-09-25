package Cluedo;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

    // DEFINITION DES VARIABLES GLOBALES
    
    Help Help = new Help(); //Instanciation de la classe d'aide
    
    ArrayList<Player> Players = new ArrayList<Player>();
    
    int nbPlayers;
    int Turn;
    int Round;
    boolean resolved;
    boolean noMorePlayers;
    
    ArrayList<Card> Cards=new ArrayList<Card>();
    ArrayList<Card> Crime=new ArrayList<Card>();
    ArrayList<Card> Suspects=new ArrayList<Card>();
    ArrayList<Card> Weapons=new ArrayList<Card>();
    ArrayList<Card> Rooms=new ArrayList<Card>();
    ArrayList<Card> Deck=new ArrayList<Card>();

    public void Start(){
        //Affichage du message de bienvenue
        Help.messageAccueil();
        String choix="";
        
        //Boucle du menu Principal
        while(!choix.toLowerCase().equals("exit")){
            choix = this.menuPrincipal();
            switch(choix.toLowerCase()){
                case "solo": 
                    this.Solo(); //On lance une partie Solo
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
                
                default: System.out.println("\nChoix non valide.\n"); break;
            }  
        }
    }
    
    public String menuPrincipal(){
        //Permet à l'utilisateur d'entrer son choix dans l'invite de commande
        System.out.println("Type \"help\" for some information.");
        Scanner sc = new Scanner(System.in);
        System.out.println(" > ");
        String str = sc.nextLine();
        return str;
    }
    
    public void Solo() {
        //Lancement d'une partie Solo
        
        //Initialisation des variables
        Players = new ArrayList<Player>();
    
        Cards=new ArrayList<Card>();
        Crime=new ArrayList<Card>();
        Suspects=new ArrayList<Card>();
        Weapons=new ArrayList<Card>();
        Rooms=new ArrayList<Card>();
        Deck=new ArrayList<Card>();
        
        resolved=false;
        noMorePlayers=false;
        
        nbPlayers = 2;
        Turn=0;
        Round=1;       
        
        this.Joueurs(); //Définition du nombre de joueurs
        this.Carte(); //Ajout des Cartes dans le jeu 
        this.Shuffle(); //Mélange des Listes de Carte Rooms, Player, Weapons
        this.Crime(); //Definit un nouveau Crime aléatoire avec une carte Room, une cart Suspect, une carte Weapon, supprime du jeu ces cartes, mélange le jeu
        
        this.Distribuer(); //Donner des cartes à tous les joueurs
        
        //Tant que le mystère n'est pas résolu, ou qu'il y a encore des joueurs pouvant donner des accusations, on passe au tour suivant
        while(!resolved && !noMorePlayers){
            this.nextTurn();
        }
        
        if(resolved) System.out.println("BRAVO ! Vous avez résolu l'égnime !");
        if(noMorePlayers) System.out.println("No one could solve the Crime. \nThe Crime was made by "+Crime.get(0).name+", in the "+Crime.get(2).name+", with the "+Crime.get(1).name+". More Luck next time !");
        
        System.out.println("\nRetour au menu Principal.\n");
    }
    
    public void Joueurs(){
        int i;
        for(i=0; i<nbPlayers; i++) Players.add(new Player(i, "Player "+i));
    }
    
    public void Carte()
    {
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
        
        for(Card card : Cards){
            if(card.type.equals("Rooms")) Rooms.add(card);
            if(card.type.equals("Weapons")) Weapons.add(card);
            if(card.type.equals("Suspect")) Suspects.add(card);
        }
    }

    public void Shuffle(){
            Collections.shuffle(Suspects);
            Collections.shuffle(Weapons);
            Collections.shuffle(Rooms);
    }

    public void Crime(){
        Crime.add(Suspects.get(0));
        Suspects.remove(0);
        Crime.add(Weapons.get(0));
        Weapons.remove(0);
        Crime.add(Rooms.get(0));
        Rooms.remove(0);

        Deck.addAll(Suspects);
        Deck.addAll(Weapons);
        Deck.addAll(Rooms);
        Collections.shuffle(Deck);
        System.out.println("\nReclusive millionaire Samuel Black’s been murdered in his mansion! \n"
                         + "Now, it’s up to you to crack the case!\n"
                         + "Question everything to unravel the mystery. Who did it? Where? And with what weapon?\n");
    }

    public void Distribuer(){
        int i=0;
        int j=0;
        while (i<Deck.size()){
            for(j=0;j<Players.size();j++){
                Players.get(j).Hand.add(Deck.get(i));
                i+=1;
            }
        }
        System.out.println("\nCards have been dealed.\n");
    }	

    public void nextTurn(){
        System.out.println("\n");

        ArrayList<String> Ordre;
        boolean nextPlayer = true;
        
        //On vérifie que le joueur courant peut encore donner des accusations
        if(!Players.get(Turn).Game_over){
            //On attend l'entrée du joueur courant par TraiterCommande qui s'assure de son formattage correct
            Ordre = this.TraiterCommande();
            //Traitement du premier argument de la commande entrée par le joueur courant
            switch (Ordre.get(0).toLowerCase()){
                case "move" : 
                    //Traitement du deuxième argument de la commande entrée par le joueur courant
                    switch(Ordre.get(1)){
                        case "suggest" :
                            Suggest(Ordre);
                        break;

                        case "accuse" :
                            Accuse(Ordre);
                        break;

                        default:
                        break;
                    }
                break;
                    
                case "show" : 
                    Players.get(Turn).show();
                    nextPlayer=false;
                break;
                                        
                case "exit" : 
                    noMorePlayers=true;
                break;
                    
                case "help" : 
                    nextPlayer=false; 
                    Help.help_menu_game(); 
                break;
                    
                default: break;
            }

        } else System.out.println(Players.get(Turn).name+" is out of the game. Turn skipped.");
        
        //Tour terminé, on passe la variable Turn au numéro du prochain Joueur
        if(nextPlayer){
            if(Turn==nbPlayers-1)Turn=0;
            else Turn++;
        }
    }
        
    public ArrayList<String> TraiterCommande(){
        ArrayList<String> Ordre=new ArrayList<String>();
        
        /*System.out.println("Choix Disponibles :\n"+Cards+"\n");
        System.out.println("player 0"+Players.get(0).Hand);
        System.out.println("Player 1 "+Players.get(1).Hand);
        System.out.println("Player 2 "+Players.get(2).Hand);
        System.out.println("Crime : "+Crime.get(0).name+" "+Crime.get(1).name+" "+Crime.get(2).name);*/
        
        Scanner sc = new Scanner(System.in);

        System.out.println("\n"+Players.get(Turn).name+" >");

        //On attend l'entrée du joueur
        String str = sc.nextLine();
        //On Split pour tester chaque argument et on stocke dans Ordre
        String[] commande=str.split(" ");
        for(int i=0;i<commande.length;i++) Ordre.add(commande[i]);
        
        //Vérification que la commande entrée contient des mots autorisés
        if(Ordre.get(0).toLowerCase().equals("exit")||Ordre.get(0).toLowerCase().equals("help")||Ordre.get(0).toLowerCase().equals("show"))
            return Ordre;
        else{
            //La commande doit posséder 5 arguments si autre que "exit", "help" ou "show"
            if((Ordre.size()!=5) || 
               (!Ordre.get(0).toLowerCase().equals("move")) || 
               (!Ordre.get(1).toLowerCase().equals("suggest")&&!Ordre.get(1).toLowerCase().equals("accuse"))) {
                Help.help_menu_game();
                System.out.println("Erreur commande");
                return TraiterCommande();
            }

            int roomCards=0;
            int suspectCards=0;
            int weaponCards=0;
            //On compte le nombre de cartes Room, Weapon et Suspect dans la commande
            for(int i=2;i<=4;i++){
                for(Card card : Cards) {
                    if(card.name.equals(Ordre.get(i))&&card.type.equals("Rooms")) roomCards++;
                    if(card.name.equals(Ordre.get(i))&&card.type.equals("Suspect")) suspectCards++;
                    if(card.name.equals(Ordre.get(i))&&card.type.equals("Weapons")) weaponCards++;
                }
            }
            
            //On vérifie qu'il n'y a bien qu'UNE SEULE carte de chaque (Room, Weapon, Suspect)
            if(!(roomCards==1 && suspectCards==1 && weaponCards==1)){
                System.out.println("Choix non correct. Veuillez choir un Lieu, un Suspect et une Arme disponibles.");
                return TraiterCommande();
            }
        }
        
        return Ordre;
    }

    public void Suggest(ArrayList<String> Ordre)
    { 
        ArrayList<Card>Info=new ArrayList<Card>();
        Player PlayersPossess=new Player();
        
        boolean end = false;
        int NumTestedPlayer=Players.get(Turn).nbr;
        
        if(NumTestedPlayer==(Players.size()-1)) NumTestedPlayer = 0;
        else  NumTestedPlayer = NumTestedPlayer+1;

        Card Room=new Card();
        Card Weapon=new Card();
        Card Suspect=new Card();
        
        for (int i=2;i<=4;i++){
            for(Card card : Cards) {
                if(card.name.equals(Ordre.get(i))&&card.type.equals("Suspect")) Suspect=card;
                if(card.name.equals(Ordre.get(i))&&card.type.equals("Rooms")) Room=card;
                if(card.name.equals(Ordre.get(i))&&card.type.equals("Weapons")) Weapon=card;
            }
        }
        
        System.out.println("I suggest it was "+Suspect.name+", in the "+Room.name+", with the "+Weapon.name+".\n");
        
        //Tant que le joueur suivant n'a pas une des cartes demandées, ou que tous les jouerus ont été interrogés, interroger le joueur suivant
        while(!end) {
            for (int i=2;i<=4;i++){
                for(Card carte : Players.get(NumTestedPlayer).Hand){
                    if(carte.name.equals(Ordre.get(i))) {
                        //Une des cartes demandées étaient dans la main d'un joueur
                        Info.add(carte);
                        PlayersPossess = Players.get(NumTestedPlayer);
                        end = true;
                    }
                }
            }
            
            if(Info.size()<1) {
                //Le joueur interrogé n'avait aucune des cartes demandées
                System.out.println("\t"+Players.get(NumTestedPlayer).name+" : \"I cannot disprove your suggestion.\"\n");
                if(NumTestedPlayer==(Players.size()-1)) NumTestedPlayer = 0;
                else  NumTestedPlayer = NumTestedPlayer+1;
                if(NumTestedPlayer == Turn) end = true;
            }
        }
        
        
        boolean knownClue;
        //Si après avoir interrogés les autres joueurs, quelqu'un possédait ces cartes
        if(Info.size()>=1){
            //On demande au joueur en question de choisir une des cartes qu'il possède et qui est demandée pour la montrer
            Card carte = chooseCard(PlayersPossess, Info);
            
            knownClue=false;
            //On regarde si le joueur qui suggère possédait déjà cet indice, sinon on l'ajoute à la liste des indices de ce joueur
            for(Card clue : Players.get(Turn).Clues)
                if(clue.name.equals(carte.name)){
                    System.out.println(PlayersPossess.name+" show you  "+carte+". You already knew this Clue.");
                    knownClue=true;
                }

            if(!knownClue){
                System.out.println(PlayersPossess.name+" show you  "+carte+". You Keep it in your Clues.");
                Players.get(Turn).Clues.add(carte);
            }
        }
    }
    
    public Card chooseCard(Player p, ArrayList<Card> Info){  
        //Fonction pour demander à un joueur p quelle quelle carte contenue dans Info veut-il montrer.
        boolean validAnswer=false;
        
        System.out.println("\t"+p.name+", Which card do you want to show ?");
        int i = 0;
        for(Card carte : Info) {
            System.out.println("\t"+i+") "+carte);
            i++;
        }
        
        String str="";
        Scanner sc;
        while(!validAnswer){
            sc = new Scanner(System.in);
            System.out.println("\t"+p.name+" > ");
            str = sc.nextLine();
            if(isValidInt(str)) 
               if(Integer.parseInt(str)>=0 && Integer.parseInt(str)<Info.size())
                   validAnswer=true;
            
            if(!validAnswer){
                System.out.println("\n\tUnknown choice.");
                System.out.println("\t"+p.name+", Which card do you want to show ?");
                i = 0;
                for(Card carte : Info) {
                    System.out.println("\t"+i+") "+carte);
                    i++;
                }
            }
        }
        
        return Info.get(Integer.parseInt(str));
    }
    
    private boolean isValidInt(String toTest) {
        Pattern p = Pattern.compile("(\\d)+");
        Matcher m = p.matcher(toTest);
        if (m.lookingAt() && m.start() == 0 && m.end() == (toTest.length())) return true;
        return false;
    }
    
    public void Accuse(ArrayList<String> Ordre){
        int nbCorrect=0;
        Card Room=new Card();
        Card Weapon=new Card();
        Card Suspect=new Card();
        for (int i=2;i<=4;i++){
            //On récupère les cartes sur lesquelles portent l'accusation
            for(Card card : Cards) {
                if(card.name.equals(Ordre.get(i))&&card.type.equals("Suspect")) Suspect=card;
                if(card.name.equals(Ordre.get(i))&&card.type.equals("Rooms")) Room=card;
                if(card.name.equals(Ordre.get(i))&&card.type.equals("Weapons")) Weapon=card;
            }
            //On teste pour savoir combien de cartes sur lesquelles portent l'accusation font partie du Crime 
            for(Card carte : Crime)
                if(carte.name.equals(Ordre.get(i))) nbCorrect++;
        }
        
        System.out.println("\nI accuse "+Suspect.name+", in the "+Room.name+", with the "+Weapon.name+".");
        
        //Si il y a exactement trois cartes correspondantes, le Crime est résolu !
        if(nbCorrect == 3){
            System.out.println("The crime was made by "+Suspect.name+", in the "+Room.name+", with the "+Weapon.name+".");
            System.out.println(Players.get(Turn).name+" solved the mystery !!");
            resolved = true;
        }
        //Sinon, le joueur portant l'accusation est éliminé
        else {
            System.out.println("Too bad, you are wrong! Your game is over but you still have to show clues to other players!");
            Players.get(Turn).Game_over = true;
            noMorePlayers=true;
            for(Player player : Players) if(player.Game_over==false) noMorePlayers=false;
        }
                
    }

}
