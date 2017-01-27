package dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.*;
import com.google.inject.Provider;
import models.Profile;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

/**
 * Created by jodan on 2017/01/20.
 */
@Singleton
public class UserDao {
    @Inject
    Provider<EntityManager> entityManagerProvider;

    @UnitOfWork
    public Profile getProfile(String username) {

        if (username != null) {
            EntityManager entityManager = entityManagerProvider.get();

            Profile profile;
            try {
                TypedQuery<Profile> q = entityManager.createQuery("SELECT x FROM Profile x WHERE username = :usernameParam", Profile.class);
                profile = q.setParameter("usernameParam", username).getSingleResult();
            } catch (Exception e) {
                profile = null;
            }

            return profile;
        }
        return null;
    }

    @Transactional
    public boolean saveProfile(Profile newProfile) {

        EntityManager entityManager = entityManagerProvider.get();

        Profile profile;
        try {
            TypedQuery<Profile> q = entityManager.createQuery("SELECT x FROM Profile x WHERE username = :usernameParam", Profile.class);
            profile = q.setParameter("usernameParam", newProfile.username).getSingleResult();
        } catch (Exception e) {
            profile = null;
        }

        if (profile == null) {
            entityManager.persist(newProfile);

            entityManager.setFlushMode(FlushModeType.COMMIT);
            entityManager.flush();
            return true;
        } else {
            profile.correctlyGuessed = newProfile.correctlyGuessed;
        }
        return false;
    }

    @Transactional
    public boolean updateProfile(Profile editedProfile) {

        EntityManager entityManager = entityManagerProvider.get();

        Profile profile;
        try {
            TypedQuery<Profile> q = entityManager.createQuery("SELECT x FROM Profile x WHERE username = :usernameParam", Profile.class);
            profile = q.setParameter("usernameParam", editedProfile.username).getSingleResult();
        } catch (Exception e) {
            profile = null;
        }

        if (profile != null) {
            if (!editedProfile.correctlyGuessed.equals(""))
                profile.correctlyGuessed = editedProfile.correctlyGuessed;

            if (!editedProfile.password.equals(""))
                profile.password = editedProfile.password;
            return true;
        }

        return false;
    }

}
