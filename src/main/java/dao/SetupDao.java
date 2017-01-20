package dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.User;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import javax.persistence.FlushModeType;

public class SetupDao {

    @Inject
    Provider<EntityManager> entityManagerProvider;

    @Transactional
    public void setup() {

        EntityManager entityManager = entityManagerProvider.get();

        Query q = entityManager.createQuery("SELECT x FROM User x");
        List<User> users = (List<User>) q.getResultList();

        if (users.size() == 0) {

            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            byte[] encodedhash = "lastPass".getBytes(StandardCharsets.UTF_8);

            User bob = new User("tester", new String(encodedhash));
            entityManager.persist(bob);

            entityManager.setFlushMode(FlushModeType.COMMIT);
            entityManager.flush();
        }

    }
}
