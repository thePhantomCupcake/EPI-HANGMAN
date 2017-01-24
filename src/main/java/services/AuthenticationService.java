package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.*;
import com.google.inject.Provider;
import models.Profile;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jodan on 2017/01/20.
 */
@Singleton
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

            Profile profile;
            try {
                TypedQuery<Profile> q = entityManager.createQuery("SELECT x FROM Profile x WHERE username = :usernameParam", Profile.class);
                profile = q.setParameter("usernameParam", username).getSingleResult();
            }
            catch (Exception e){
                profile = null;
            }

            if (profile != null) {
                System.out.println("Username: " + username + " || Input Hash: " + new String(encodedhash) + " || " + "Stored Hash: " + profile.password);
                if (profile.password.equals(new String(encodedhash))) {

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

        Profile profile;
        try {
            TypedQuery<Profile> q = entityManager.createQuery("SELECT x FROM Profile x WHERE username = :usernameParam", Profile.class);
            profile = q.setParameter("usernameParam", username).getSingleResult();
        }
        catch (Exception e){
            profile = null;
        }

        if (profile == null) {
            Profile newProfile = new Profile(username, new String(encodedhash));
            entityManager.persist(newProfile);

            entityManager.setFlushMode(FlushModeType.COMMIT);
            entityManager.flush();
            return true;
        }

        return false;
    }

}
