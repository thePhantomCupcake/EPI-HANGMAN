package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GameState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String username;
    public String guesses;
    public String word;

    public GameState() {
    }

    public GameState(String username, String word, String guesses) {
        this.username = username;
        this.guesses = guesses;
        this.word = word;
    }

}
