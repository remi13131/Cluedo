package Cluedo;

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
}
