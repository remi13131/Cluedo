package Cluedo.TheGame;

import Cluedo.Helper.*;
import Cluedo.Modele.*;
import Cluedo.Networking.*;

import java.io.IOException;
import java.net.InetAddress;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Referee {
    Help Help = new Help();
    
    int portNumber, numClients, timeout;
    RegServer server;
    
    int nbPlayers = 0, Turn, Round;
    boolean resolved, noMorePlayers;
    
    ArrayList<Card> Cards, Suspects, Weapons, Rooms, Deck;
    ArrayList<Player> Players;
    
    Crime Crime;
    
    public void Start(int portNumber, int numClients, int timeout) throws IOException {
        System.out.println("\nThis server name and IP address are : "+InetAddress.getLocalHost());
        Utils.saisieUtilisateur("\nPress Enter to start Registration phase. This proccess will automatically end after "+(timeout/1000)+" seconds.\n");
        
        this.portNumber = portNumber;
        this.numClients = numClients;
        this.timeout = timeout;
        
        this.waitPlayersConnection();
        
        if(nbPlayers>0)
            //Lancement d'une partie Solo
            this.newGame();
        else System.out.println("\nNo players joigned the Game Server during Registration phase.");
        
        server.sendAll(ComServer.END_MSG);
        server.close();
       
        Utils.saisieUtilisateur("\nPress Enter to go back to Principal Menu.\n");
    }
    
    public void waitPlayersConnection() throws IOException{
        System.out.println("Starting server.");
        
        server = new RegServer(portNumber, numClients, timeout);
        System.out.println("Waiting for connections...");
        server.open();
        
        nbPlayers = server.getNumClients();
    }
    
    public void newGame() throws IOException {
        //Initialisation des variables du jeu        
        resolved=false;
        noMorePlayers=false;

        Turn=0;
        Round=0;

        this.Joueurs(); //Définition du nombre de joueurs
        this.Carte(); //Ajout des Cartes dans le jeu 
        this.Shuffle(); //Mélange des Listes de Carte Rooms, Player, Weapons
        this.Crime(); //Definit un nouveau Crime aléatoire avec une carte Room, une cart Suspect, une carte Weapon, supprime du jeu ces cartes

        this.Distribuer(); //Mélnger le jeu et Donner des cartes à tous les joueurs

        this.playGame(); //Lancer la partie
    }
    
    public void Joueurs() throws IOException{
        Players = new ArrayList<>();
        String playerName;
        int i;
        for(i=0; i<nbPlayers; i++) {
            playerName=server.receive(i);
            playerName += " (Player "+i+")";
            Players.add(new Player(i, playerName));
            server.send(i, "ack "+i);
        }
        for(i=0; i<nbPlayers; i++) server.sendAll("addplayer "+Players.get(i).getName());
        
    }
    
    public void playGame() throws IOException {
        server.sendAll("\nReclusive millionaire Samuel Black’s been murdered in his mansion! \n"
                     + "Now, it’s up to you to crack the case!\n"
                     + "Question everything to unravel the mystery. "
                     + "Who did it? Where? And with what weapon?\n");
        
        //Tant que le mystère n'est pas résolu, ou qu'il y a encore des joueurs pouvant donner des accusations, on passe au tour suivant
        while(!resolved && !noMorePlayers) this.nextTurn();
        
        //Fin de la partie, affichage du texte de résolution / non résolution de l'égnigme, retour au menu proincipal
        if(resolved) server.sendAll("\n"
                                  + "##################################\n"
                                  + "# BRAVO ! You solved the crime ! #\n"
                                  + "##################################");
        
        if(noMorePlayers) server.sendAll("No one could solve the Crime. \nThe Crime was made by "+Crime.getSuspect().getName()+", in the "+Crime.getRoom().getName()+", with the "+Crime.getWeapon().getName()+".\n"
                                           + "Play again, maybe you will get more Luck next time ! :)\n");
        
        server.sendAll("You have played "+Round+" Rounds.");
    }
    
    public void Carte()
    {
        Cards=Help.Cards;
        Deck=new ArrayList<>();
        Suspects=new ArrayList<>();
        Weapons=new ArrayList<>();
        Rooms=new ArrayList<>();
        
        for(Card card : Cards){
            if(card.getType().equals("Rooms")) Rooms.add(card);
            if(card.getType().equals("Weapons")) Weapons.add(card);
            if(card.getType().equals("Suspect")) Suspects.add(card);
        }
    }

    public void Shuffle(){
            Collections.shuffle(Suspects);
            Collections.shuffle(Weapons);
            Collections.shuffle(Rooms);
    }

    public void Crime(){
        Crime=new Crime();
        
        Crime.setSuspect(Suspects.get(0));
        Suspects.remove(0);
        Crime.setWeapon(Weapons.get(0));
        Weapons.remove(0);
        Crime.setRoom(Rooms.get(0));
        Rooms.remove(0);
    }

    public void Distribuer() throws IOException{
        int i=0;
        int j=0;
        
        Deck.addAll(Suspects);
        Deck.addAll(Weapons);
        Deck.addAll(Rooms);
        
        Collections.shuffle(Deck);
        
        while (i<Deck.size()){
            for(j=0;j<Players.size();j++){
                Players.get(j).getHand().add(Deck.get(i));
                i+=1;
            }
        }
        for(i=0; i<nbPlayers; i++) server.send(i, "start "+Players.get(i).handToString());
        server.sendAll("\nCards have been dealed.\n");
    }	

    public void nextTurn() throws IOException {
        System.out.println("\n");

        ArrayList<String> Ordre;
        boolean nextPlayer = true;
   
        //On vérifie que le joueur courant peut encore donner des accusations
        if(!Players.get(Turn).isGame_over()){
            //On attend l'entrée du joueur courant par TraiterCommande qui s'assure de son formattage correct
            Ordre = this.TraiterCommande();
            //Traitement du premier argument de la commande entrée par le joueur courant
            switch (Ordre.get(0).toLowerCase()){
                case "move" : 
                    //Traitement du deuxième argument de la commande entrée par le joueur courant
                    switch(Ordre.get(1).toLowerCase()){
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
                    server.send(Turn, Players.get(Turn).show());
                    nextPlayer=false;
                break;
                                        
                case "exit" :
                    server.close(Turn);
                    server.sendAll("error exit "+Turn);
                    noMorePlayers=true;
                break;
                    
                case "help" : 
                    nextPlayer=false; 
                    Help.help_menu_game();
                break;
                    
                default: break;
            }

        } else server.sendAll(Players.get(Turn).getName()+" is out of the game. Turn skipped.");
        //Tour terminé, on passe la variable Turn au numéro du prochain Joueur
        if(nextPlayer){
            if(Turn==nbPlayers-1)Turn=0;
            else Turn++;
            Round++;
        }
    }
        
    public ArrayList<String> TraiterCommande() throws IOException {
        
        ArrayList<String> Ordre=new ArrayList<String>();
        String msg;
        
        /* ICI EN COMMENTAIRES DES AFFICHAGES UTILES POUR LES TESTS.
        System.out.println("Choix Disponibles :\n"+Cards+"\n");
        System.out.println("player 0"+Players.get(0).Hand);*/
        System.out.println("Player 1 "+Players.get(0).getHand());
        System.out.println("Player 2 "+Players.get(1).getHand());
        System.out.println("Crime : "+Crime.getSuspect().getName()+" "+Crime.getRoom().getName()+" "+Crime.getWeapon().getName());
        
        for (int i = 0; i < server.getNumClients(); ++i)
            if(Players.get(Turn).getNbr()==i) server.send(i, "play");
            else server.send(i, "\n" + Players.get(Turn).getName() + " is playing.");
            
        //On attend l'entrée du joueur
        msg  = server.receive(Turn);
        System.out.format("%1$s: %2$s\n", Players.get(Turn).getName(), msg);

        //On Split pour tester chaque argument et on stocke dans Ordre
        String[] commande=msg.split(" ");
        for(int i=0;i<commande.length;i++) Ordre.add(commande[i]);

        //Vérification que la commande entrée contient des mots autorisés
        if(Ordre.get(0).toLowerCase().equals("exit")||Ordre.get(0).toLowerCase().equals("help")||Ordre.get(0).toLowerCase().equals("show"))
            return Ordre;
        else{
            //La commande doit posséder 5 arguments si autre que "exit", "help" ou "show"
           
            if((Ordre.size()!=5) || 
               (!Ordre.get(0).toLowerCase().equals("move")) || 
               (!Ordre.get(1).toLowerCase().equals("suggest")&&!Ordre.get(1).toLowerCase().equals("accuse"))) {
                server.send(Turn, "error Bad command provided.\n");
                Help.help_menu_game();
                return TraiterCommande();
            }

            int roomCards=0;
            int suspectCards=0;
            int weaponCards=0;
            //On compte le nombre de cartes Room, Weapon et Suspect dans la commande
            for(int i=2;i<=4;i++){
                for(Card card : Cards) {
                    if(card.getName().toLowerCase().equals(Ordre.get(i).toLowerCase())&&card.getType().equals("Rooms")) roomCards++;
                    if(card.getName().toLowerCase().equals(Ordre.get(i).toLowerCase())&&card.getType().equals("Suspect")) suspectCards++;
                    if(card.getName().toLowerCase().equals(Ordre.get(i).toLowerCase())&&card.getType().equals("Weapons")) weaponCards++;
                }
            }

            //On vérifie qu'il n'y a bien qu'UNE SEULE carte de chaque (Room, Weapon, Suspect)
            if(!(roomCards==1 && suspectCards==1 && weaponCards==1)){
                server.sendAll("error Bad Cards given. Please provide ONE Room, ONE Suspect and ONE Weapon available.");
                server.sendAll("error Available Cards:\n"+Cards+"\n");
                return TraiterCommande();
            }
        }
        return Ordre;
    }

    public void Suggest(ArrayList<String> Ordre) throws IOException
    { 
        ArrayList<Card>Info=new ArrayList<Card>();
        Player PlayersPossess=new Player();
        
        boolean end = false;
        int NumTestedPlayer=Turn;
        
        if(NumTestedPlayer==(Players.size()-1)) NumTestedPlayer = 0;
        else  NumTestedPlayer = NumTestedPlayer+1;

        Card Room=new Card();
        Card Weapon=new Card();
        Card Suspect=new Card();
        
        for (int i=2;i<=4;i++){
            for(Card card : Cards) {
                if(card.getName().toLowerCase().equals(Ordre.get(i).toLowerCase())&&card.getType().equals("Suspect")) Suspect=card;
                if(card.getName().toLowerCase().equals(Ordre.get(i).toLowerCase())&&card.getType().equals("Rooms")) Room=card;
                if(card.getName().toLowerCase().equals(Ordre.get(i).toLowerCase())&&card.getType().equals("Weapons")) Weapon=card;
            }
        }
        
        server.sendAll("move "+Turn+" suggest "+Suspect.getName()+" "+Room.getName()+" "+Weapon.getName());
        
        //Tant que le joueur suivant n'a pas une des cartes demandées, ou que tous les jouerus ont été interrogés, interroger le joueur suivant
        while(!end) {
            for (int i=2;i<=4;i++){
                for(Card carte : Players.get(NumTestedPlayer).getHand()){
                    if(carte.getName().toLowerCase().equals(Ordre.get(i).toLowerCase())) {
                        //Une des cartes demandées étaient dans la main d'un joueur
                        Info.add(carte);
                        PlayersPossess = Players.get(NumTestedPlayer);
                        end = true;
                    }
                }
            }          
            
            if(Info.size()<1) {
                //Le joueur interrogé n'avait aucune des cartes demandées
                if(NumTestedPlayer==(Players.size()-1)) NumTestedPlayer = 0;
                else  NumTestedPlayer = NumTestedPlayer+1;
                if(NumTestedPlayer == Turn) end = true;
                server.sendAll("info noshow "+NumTestedPlayer+" "+Turn);
            }
        }
        
        //Si après avoir interrogés les autres joueurs, quelqu'un possédait ces cartes
        if(Info.size()>=1){
            //On demande au joueur en question de choisir une des cartes qu'il possède et qui est demandée pour la montrer
            String infoString = "";
            int i;
            for(i=0; i<Info.size(); i++) infoString += Info.get(i).getName()+" ";
            server.sendAll(PlayersPossess.getName()+" is choosing a card to show.");
            server.send(PlayersPossess.getNbr(), "ask "+infoString.trim());
            String answerClient;
            answerClient = server.receive(PlayersPossess.getNbr());
            String answerClientTab[] = answerClient.split(" ");
            Card carte = Help.nameToCard(answerClientTab[1]);
            for(i=0; i<nbPlayers; i++) {
                if(i!=Turn) server.send(i, "info show "+PlayersPossess.getNbr()+" "+Turn);
                else if(i!=PlayersPossess.getNbr()) server.send(i, "info show "+PlayersPossess.getNbr()+" "+carte.getName());
            }
            
            if(!Players.get(Turn).isKnownClue(carte)) Players.get(Turn).getClues().add(carte);
        }
        
    }
    
    public void Accuse(ArrayList<String> Ordre) throws IOException{
        int nbCorrect=0;
        Card Room=new Card();
        Card Weapon=new Card();
        Card Suspect=new Card();
        for (int i=2;i<=4;i++){
            //On récupère les cartes sur lesquelles portent l'accusation
            for(Card card : Cards) {
                if(card.getName().toLowerCase().equals(Ordre.get(i).toLowerCase())&&card.getType().equals("Suspect")) Suspect=card;
                if(card.getName().toLowerCase().equals(Ordre.get(i).toLowerCase())&&card.getType().equals("Rooms")) Room=card;
                if(card.getName().toLowerCase().equals(Ordre.get(i).toLowerCase())&&card.getType().equals("Weapons")) Weapon=card;
            }
            //On teste pour savoir combien de cartes sur lesquelles portent l'accusation font partie du Crime 
            if(Ordre.get(i).toLowerCase().equals(Crime.getSuspect().getName().toLowerCase())) nbCorrect++;
            if(Ordre.get(i).toLowerCase().equals(Crime.getRoom().getName().toLowerCase())) nbCorrect++;
            if(Ordre.get(i).toLowerCase().equals(Crime.getWeapon().getName().toLowerCase())) nbCorrect++;
        }
        
        server.sendAll("move "+Turn+" accuse "+Suspect.getName()+" "+Room.getName()+" "+Weapon.getName());
        
        //Si il y a exactement trois cartes correspondantes, le Crime est résolu !
        if(nbCorrect == 3){
            server.sendAll("The crime was made by "+Suspect.getName()+", in the "+Room.getName()+", with the "+Weapon.getName()+".");
            server.sendAll(Players.get(Turn).getName()+" solved the mystery !!");
            resolved = true;
        }
        ////Sinon, le joueur portant l'accusation regarde quelle était l'égnigme, et est éliminé
        else {
            server.send(Turn, "The crime was made by "+Crime.getSuspect().getName()+", in the "+Crime.getRoom().getName()+", with the "+Crime.getWeapon().getName()+". \n");
            server.sendAll("Too bad,"+ Players.get(Turn).getName()+ "is wrong! His game is over but he still has to show clues to other players!");
            Players.get(Turn).setGame_over(true);
            noMorePlayers=true;
            for(Player player : Players) if(!player.isGame_over()) noMorePlayers=false;
        }
                
    }
}
