package Cluedo.Modele;

public class Crime {

    Card Suspect;
    Card Room;
    Card Weapon;
    
    boolean resolved;

    public Crime() {
        this.Suspect = new Card();
        this.Room = new Card();
        this.Weapon = new Card();

        this.resolved = false;
    }
    
    public Card getSuspect() {
        return Suspect;
    }

    public void setSuspect(Card Suspect) {
        this.Suspect = Suspect;
    }

    public Card getRoom() {
        return Room;
    }

    public void setRoom(Card Room) {
        this.Room = Room;
    }

    public Card getWeapon() {
        return Weapon;
    }

    public void setWeapon(Card Weapon) {
        this.Weapon = Weapon;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
    
}
