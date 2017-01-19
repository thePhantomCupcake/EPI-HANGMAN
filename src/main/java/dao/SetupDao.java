package dao;


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

            // Create a new user and save it
            User bob = new User("tester", "lastPass");
            entityManager.persist(bob);

            entityManager.setFlushMode(FlushModeType.COMMIT);
            entityManager.flush();
        }

    }
}
