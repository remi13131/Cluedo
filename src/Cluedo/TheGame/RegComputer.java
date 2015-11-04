package Cluedo.TheGame;

import Cluedo.Helper.*;
import Cluedo.Modele.*;
import Cluedo.Networking.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegComputer {
    Help Help = new Help();
    
    int portNumber;
    String hostName;
    boolean end = false;
    
    Player player = new Player();
    ArrayList<String> players = new ArrayList<>();
    
    public RegComputer() {
        player.setName("Computer");
    }
    
    public void Start(String hostName, int portNumber) throws IOException {
        this.hostName=hostName;
        this.portNumber=portNumber;
        String serverMessage;
        String userCommand;
        String argCommand[];
        
        Scanner console = new Scanner(System.in);
        
        Client client = new Client();
        client.open(this.hostName, this.portNumber);
        client.send(player.getName());

        serverMessage = client.receive();
        argCommand=serverMessage.split(" ");
        
        int i;
        if(!argCommand[0].equals("ack")) {
            client.send(ComServer.END_MSG);
            client.close();
        }
        
        player.setNbr(Integer.parseInt(argCommand[1]));
        System.out.println("Congratulation "+ player.getName() +"! \nYou are now connected to the Game server with Player number "+ player.getNbr() +". Have a great time playing Cluedo :-)");
        
        player.setName(player.getName()+" (Player "+player.getNbr()+")");
        
        serverMessage = client.receive();
        argCommand=serverMessage.split(" ");
        String playerName;
        while(argCommand[0].equals("addplayer")) {
            playerName = "";
            for(i=1; i<argCommand.length; i++) playerName += argCommand[i]+" ";
            players.add(playerName.trim());
            serverMessage = client.receive();
            argCommand=serverMessage.split(" ");
        }
        
        if(players.size()==0)
            for(i=0; i<RegServer.maxConnOpen;i++) players.add("anonymous (Player "+i+")");

        player.initCluesMapString(players);
        
        while(!argCommand[0].equals("start")) {
            serverMessage = client.receive();
            argCommand=serverMessage.split(" ");
        }
        
        for(i=1; i<argCommand.length; i++)
            for(Card card : Help.Cards)
                if(card.getName().toLowerCase().equals(argCommand[i].toLowerCase())) player.getHand().add(card);

        do {
                serverMessage = client.receive();
                argCommand=serverMessage.toLowerCase().split(" ");
                
                if (serverMessage.equals(ComServer.END_MSG)) break;
                
                switch(argCommand[0]) {

                    case "play":
                        client.flushOut();
                        System.out.println(player.getName()+", it is you turn to play.");
                        System.out.println(player.getName()+" > ");
                        userCommand = console.nextLine();
                        client.send(userCommand);
                    break;

                    case "move":
                        Card Suspect = new Card();
                        Card Room = new Card();
                        Card Weapon = new Card();
                        for (i=3;i<argCommand.length;i++){
                            for(Card card : Help.Cards) {
                                if(card.getName().toLowerCase().equals(argCommand[i].toLowerCase())&&card.getType().equals("Suspect")) Suspect=card;
                                if(card.getName().toLowerCase().equals(argCommand[i].toLowerCase())&&card.getType().equals("Rooms")) Room=card;
                                if(card.getName().toLowerCase().equals(argCommand[i].toLowerCase())&&card.getType().equals("Weapons")) Weapon=card;
                            }
                        }
                        if(argCommand[2].equals("suggest"))
                            System.out.println("\n"+players.get(Integer.parseInt(argCommand[1]))+": \"I suggest it was "+Suspect.getName()+", in the "+Room.getName()+", with the "+Weapon.getName()+".\"\n");
                        if(argCommand[2].equals("accuse"))
                            System.out.println("\n"+players.get(Integer.parseInt(argCommand[1]))+": \"I accuse "+Suspect.getName()+", in the "+Room.getName()+", with the "+Weapon.getName()+".\"\n");
                    break;

                    case "ask": 
                        client.send("respond "+chooseCard(argCommand));
                    break;

                    case "info":
                        switch(argCommand[1])
                        {
                            case "show":
                                Card card = Help.nameToCard(argCommand[3]);
                                if(card!=null) {
                                    if(!player.isKnownClue(card)) {
                                        System.out.println("\n"+players.get(Integer.parseInt(argCommand[2]))+" show you  "+card+". You keep it in your Clues.");
                                        player.getClues().add(card);
                                    }
                                    else System.out.println("\n"+players.get(Integer.parseInt(argCommand[2]))+" show you  "+card+". You already knew this Clue.");
                                }
                                else System.out.println("\n"+players.get(Integer.parseInt(argCommand[2]))+" showed a card to "+players.get(Integer.parseInt(argCommand[3])));

                            break;

                            case "noshow":
                                if(Integer.parseInt(argCommand[3]) == player.getNbr()) 
                                    System.out.println(players.get(Integer.parseInt(argCommand[2]))+" \"I cannot disprove your suggestion.\"");
                                else 
                                    System.out.println(players.get(Integer.parseInt(argCommand[2]))+" couldn't show any card to Player "+players.get(Integer.parseInt(argCommand[3])));
                            break;
                            default: break;
                        }
                    break;

                    case "error":
                        
                        switch(argCommand[1]){
                        
                            case "exit" :
                                System.out.println("Player "+players.get(Integer.parseInt(argCommand[2]))+" left the game.");
                                System.out.println("End of the game");
                            break;
                                
                            case "invalid" :
                                System.out.println("Invalid command provided.\n");
                                System.out.println(Help.help_menu_game());
                                System.out.println("\nAvailable Cards:\n"+Help.Cards+"\n");
                            break;
                                
                            case "forbidden" :
                                System.out.println("Invalid move : Bad Cards given. Please provide ONE Room, ONE Suspect and ONE Weapon available.");
                                System.out.println("Available Cards:\n"+Help.Cards+"\n");
                            break;
                                
                            case "other" : System.out.println("Error : "+serverMessage.substring(serverMessage.indexOf(' ')+1).substring(serverMessage.indexOf(' ')+1));
                            break;
                                
                            default: System.out.println("Error : "+serverMessage.substring(serverMessage.indexOf(' ')+1)); break;    
                        }
                        
                    break;

                    default: System.out.println(serverMessage); break;
                }

        }while(!end);
        
        client.send(ComServer.END_MSG);
        client.close();
    }
    
    public String chooseCard(String cartes[]) throws IOException{  
        //Fonction pour demander à un joueur p quelle quelle carte contenue dans Info veut-il montrer.
        boolean validAnswer=false;
        ArrayList<Card> Cards = new ArrayList<>();

        int i;
        for(i=0; i<cartes.length; i++) if(Help.nameToCard(cartes[i])!=null) Cards.add(Help.nameToCard(cartes[i]));

        String str="";
        do {
            i=0;
            System.out.println("\t"+player.getName()+", Which card do you want to show ? ");
            for(Card carte : Cards) System.out.println("\t"+ i++ +") "+carte);
            
            //On attend l'entrée du joueur
            System.out.println(player.getName()+" > ");
            Scanner sc = new Scanner(System.in);
            str  = sc.nextLine();

            if(Utils.isValidInt(str)) 
               if(Integer.parseInt(str)>=0 && Integer.parseInt(str)<Cards.size())
                   validAnswer=true;
            
            if(!validAnswer) System.out.println("\n\tUnknown choice."+"\n\t"+player.getName()+", Which card do you want to show ?");
        } while(!validAnswer);

        System.out.println("\nYou showed card "+Cards.get(Integer.parseInt(str)).getName()+".");
        
        return Cards.get(Integer.parseInt(str)).getName();
    }
}
