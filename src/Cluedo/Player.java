package Cluedo;

import java.util.ArrayList;

public class Player {
    int nbr;
    String name;
    ArrayList<Card> Hand=new ArrayList<Card>();
    ArrayList<Card> Clues=new ArrayList<Card>();
    boolean Game_over = false;

    public Player() {}
    
    public Player(int nbr) {
        this.nbr = nbr;
    } 
}
