package services;

import com.google.inject.Inject;
import com.google.inject.persist.*;
import com.google.inject.Provider;
import models.User;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jodan on 2017/01/20.
 */
public class AuthenticationService {
    @Inject
    Provider<EntityManager> entityManagerProvider;

    @UnitOfWork
    public boolean login(String username, String password) {

        if (username != null && password != null) {

            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            EntityManager entityManager = entityManagerProvider.get();

            TypedQuery<User> q = entityManager.createQuery("SELECT x FROM User x WHERE username = :usernameParam", User.class);

            User user;
            try {
                user = q.setParameter("usernameParam", username).getSingleResult();
            }
            catch (Exception e){
                user = null;
            }

            if (user != null) {
                System.out.println("Username: " + username + " || Input Hash: " + new String(encodedhash) + " || " + "Stored Hash: " + user.password);
                if (user.password.equals(new String(encodedhash))) {

                    return true;
                }

            }

        }

        return false;

    }

    @Transactional
    public boolean register(String username, String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        EntityManager entityManager = entityManagerProvider.get();

        TypedQuery<User> q = entityManager.createQuery("SELECT x FROM User x WHERE username = :usernameParam", User.class);

        User user;
        try {
            user = q.setParameter("usernameParam", username).getSingleResult();
        }
        catch (Exception e){
            user = null;
        }

        if (user == null) {
            User newUser = new User(username, new String(encodedhash));
            entityManager.persist(newUser);

            entityManager.setFlushMode(FlushModeType.COMMIT);
            entityManager.flush();
            return true;
        }

        return false;
    }

}
