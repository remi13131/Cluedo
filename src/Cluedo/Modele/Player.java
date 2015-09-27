package Cluedo.Modele;

import Cluedo.Modele.Card;
import java.util.ArrayList;

public class Player {
    int nbr;
    String name;
    ArrayList<Card> Hand=new ArrayList<Card>();
    ArrayList<Card> Clues=new ArrayList<Card>();
    boolean Game_over = false;

    public Player() {}
    
    public Player(int nbr, String name) {
        this.nbr = nbr;
        this.name=name;
    }
    
    public void show(){
        System.out.println("Your Hand: \n"+this.Hand);
        System.out.println("Your Clues: \n"+this.Clues);
        if(!this.Game_over) System.out.println("You have not made an accusation yet.");
        else System.out.println("You a out of the game. You can only give clues to others.");
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
