package dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

import com.google.inject.persist.Transactional;
import models.User;

import ninja.jpa.UnitOfWork;

import com.google.inject.Inject;
import com.google.inject.Provider;


public class UserDao {

    @Inject
    Provider<EntityManager> entityManagerProvider;

    @UnitOfWork
    public boolean isUserAndPasswordValid(String username, String password) {

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
            User user = getSingleResult(q.setParameter("usernameParam", username));

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
    public boolean addUser(String username, String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        EntityManager entityManager = entityManagerProvider.get();

        TypedQuery<User> q = entityManager.createQuery("SELECT x FROM User x WHERE username = :usernameParam", User.class);
        User user = getSingleResult(q.setParameter("usernameParam", username));

        if (user == null) {
            User newUser = new User(username, new String(encodedhash));
            entityManager.persist(newUser);

            entityManager.setFlushMode(FlushModeType.COMMIT);
            entityManager.flush();
            return true;
        }

        return false;
    }

    private static <T> T getSingleResult(TypedQuery<T> query) {
        query.setMaxResults(1);
        List<T> list = query.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }
}
