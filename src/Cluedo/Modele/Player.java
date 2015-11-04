package Cluedo.Modele;

import Cluedo.Modele.Card;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    int nbr;
    String name;
    ArrayList<Card> Hand=new ArrayList<Card>();
    ArrayList<Card> Clues=new ArrayList<Card>();
    Map<String, ArrayList<Card>> CluesMap = new HashMap<String, ArrayList<Card>>();
    boolean Game_over = false;

    public Player() {}
    
    public Player(int nbr, String name) {
        this.nbr = nbr;
        this.name=name;
    }
    
    public void initCluesMap(ArrayList<Player> Players){
        for(Player p : Players)
            this.CluesMap.put(p.getName(), new ArrayList<Card>());
    }
    
    public void initCluesMapString(ArrayList<String> Players){
        for(String p : Players)
            this.CluesMap.put(p, new ArrayList<Card>());
    }
    
    public String show(){
        String showString = "";
        showString+="\nYour Hand: \n"+this.Hand;
        showString+="\nYour Clues: \n"+this.Clues;
        if(!this.Game_over) showString+="\nYou have not made an accusation yet.";
        else showString+="\nYou are out of the game. You can only give clues to others.";
        return showString;
    }
    
    public String handToString(){
        String listCards = "";
        for(Card card : this.Hand) listCards += card.name+" ";
        return listCards.trim();
    }

    public boolean isKnownClue(Card card){
        for(Card clue : this.getClues())
            if(clue.getName().equals(card.getName())) return true;
        return false;
    }
    
    public int getNbr() {
        return nbr;
    }

    public void setNbr(int nbr) {
        this.nbr = nbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getHand() {
        return Hand;
    }

    public void setHand(ArrayList<Card> Hand) {
        this.Hand = Hand;
    }

    public ArrayList<Card> getClues() {
        return Clues;
    }

    public void setClues(ArrayList<Card> Clues) {
        this.Clues = Clues;
    }

    public boolean isGame_over() {
        return Game_over;
    }

    public void setGame_over(boolean Game_over) {
        this.Game_over = Game_over;
    }
    
    
}
