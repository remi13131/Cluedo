package Cluedo;

public class Card {
    String name;
    String type;

    public Card() { }
    
    public Card(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Card "+ type +" "+ name ;
    }
    
    
}
