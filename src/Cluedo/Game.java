package Cluedo;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

    Help Help = new Help();
    
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
        Help.messageAccueil();
        String choix="";
        
        while(!choix.toLowerCase().equals("exit")){
            choix = this.menuPrincipal();
            
            switch(choix.toLowerCase()){
                case "solo": 
                    this.Solo();
                break;
                
                case "referee": 
                    System.out.println("\nNot implemented yet.\n");
                break;
                    
                case "register": 
                    System.out.println("\nNot implemented yet.\n");
                break;
                     
                case "help": 
                    Help.help();
                break;
                                       
                case "exit" : 
                    Help.quit(); 
                break;
                
                default: System.out.println("\nChoix non valide.\n"); break;
            }  
        }
    }
    
    public String menuPrincipal(){
        Scanner sc = new Scanner(System.in);
        System.out.println(" > ");
        String str = sc.nextLine();
        return str;
    }
    
    public void Solo() {
        Players = new ArrayList<Player>();
    
        Cards=new ArrayList<Card>();
        Crime=new ArrayList<Card>();
        Suspects=new ArrayList<Card>();
        Weapons=new ArrayList<Card>();
        Rooms=new ArrayList<Card>();
        Deck=new ArrayList<Card>();
        
        resolved=false;
        noMorePlayers=false;
        
        Players.add(new Player(0));
        Players.add(new Player(1));
        Players.add(new Player(2));
        
        Players.get(0).name="Player "+0;
        Players.get(1).name="Player "+1;
        Players.get(2).name="Player "+2;
        
        nbPlayers = 3;
        Turn=0;
        Round=1;       
        
        this.Carte();
        this.Shuffle();
        this.Crime();
        
        this.Distribuer();
        
        while(!resolved && !noMorePlayers){
            this.nextTurn();
        }
        
        if(resolved) System.out.println("BRAVO ! Vous avez résolu l'égnime !");
        if(noMorePlayers) System.out.println("No one could solve the Crime. \nThe Crime was made by "+Crime.get(0).name+", in the "+Crime.get(2).name+", with the "+Crime.get(1).name+". More Luck next time !");
        
        System.out.println("\nRetour au menu Principal.\n");
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
    }	

    public void nextTurn(){
        System.out.println("\n");

        
        ArrayList<String> Ordre;
        boolean nextPlayer = true;
        
        if(!Players.get(Turn).Game_over){
            Ordre = this.TraiterCommande();

            switch (Ordre.get(0).toLowerCase()){
                case "move" : 
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
                    System.out.println("Your Clues: "+Players.get(Turn).Clues);
                    if(!Players.get(Turn).Game_over) System.out.println("You have not made an accusation yet.");
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

        } else System.out.println(Players.get(Turn).name+" has made a false accusation. Turn skipped.");
        
        if(nextPlayer){
            if(Turn==nbPlayers-1)Turn=0;
            else Turn++;
        }
    }
        
    public ArrayList<String> TraiterCommande(){
        ArrayList<String> Ordre=new ArrayList<String>();
        
        System.out.println("Choix Disponibles :"+Cards+"\n");
        System.out.println("player 0"+Players.get(0).Hand);
        System.out.println("Player 1 "+Players.get(1).Hand);
        System.out.println("Player 2 "+Players.get(2).Hand);
        System.out.println("Crime : "+Crime.get(0).name+Crime.get(1).name+Crime.get(2).name);
        
        Scanner sc = new Scanner(System.in);

        System.out.println("\n"+Players.get(Turn).name+" >");

        String str = sc.nextLine();
        String[] commande=str.split(" ");
        for(int i=0;i<commande.length;i++) Ordre.add(commande[i]);
        
        if(Ordre.get(0).toLowerCase().equals("exit")||Ordre.get(0).toLowerCase().equals("help")||Ordre.get(0).toLowerCase().equals("show"))
            return Ordre;
        else{
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
            
            for(int i=2;i<=4;i++){
                for(Card card : Cards) {
                    if(card.name.equals(Ordre.get(i))&&card.type.equals("Rooms")) roomCards++;
                    if(card.name.equals(Ordre.get(i))&&card.type.equals("Suspect")) suspectCards++;
                    if(card.name.equals(Ordre.get(i))&&card.type.equals("Weapons")) weaponCards++;
                }
            }
            
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
            
        while(!end) {
            for (int i=2;i<=4;i++){
                for(Card carte : Players.get(NumTestedPlayer).Hand){
                    if(carte.name.equals(Ordre.get(i))) {
                        Info.add(carte);
                        PlayersPossess = Players.get(NumTestedPlayer);
                        end = true;
                    }
                }
            }
            
            if(Info.size()<1) {
                System.out.println("\t"+Players.get(NumTestedPlayer).name+" : \"I cannot disprove your suggestion.\"\n");
                if(NumTestedPlayer==(Players.size()-1)) NumTestedPlayer = 0;
                else  NumTestedPlayer = NumTestedPlayer+1;
                if(NumTestedPlayer == Turn) end = true;
            }
        }
        
        
        boolean knownClue;
        
        if(Info.size()>=1){
            Card carte = chooseCard(PlayersPossess, Info);
            knownClue=false;
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
            for(Card card : Cards) {
                if(card.name.equals(Ordre.get(i))&&card.type.equals("Suspect")) Suspect=card;
                if(card.name.equals(Ordre.get(i))&&card.type.equals("Rooms")) Room=card;
                if(card.name.equals(Ordre.get(i))&&card.type.equals("Weapons")) Weapon=card;
            }
            for(Card carte : Crime)
                if(carte.name.equals(Ordre.get(i))) nbCorrect++;
        }
        
        System.out.println("\nI accuse "+Suspect.name+", in the "+Room.name+", with the "+Weapon.name+".");
        
        if(nbCorrect == 3){
            System.out.println("The crime was made by "+Suspect.name+", in the "+Room.name+", with the "+Weapon.name+".");
            System.out.println(Players.get(Turn).name+" solved the mystery !!");
            resolved = true;
        }
        else {
            System.out.println("Dommage, votre accusation est fausse. Vous êtes éliminé, mais continuez à donner vos indices aux joueurs suivants. !");
            Players.get(Turn).Game_over = true;
            noMorePlayers=true;
            for(Player player : Players) if(player.Game_over==false) noMorePlayers=false;
        }
                
    }

}
