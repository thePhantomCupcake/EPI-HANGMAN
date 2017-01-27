package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Profile {

    public String username;
    public String password;
    public String correctlyGuessed;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Profile() {
    }

    public Profile(String username, String password) {
        this.username = username;
        this.password = password;
    }
 
}
