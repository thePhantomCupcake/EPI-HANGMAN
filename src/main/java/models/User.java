package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;
    public String username;
    public String password;
    public boolean isAdmin;
    
    public User() {}
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
 
}
