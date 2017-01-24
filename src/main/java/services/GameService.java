package services;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import models.GameState;
import models.Profile;
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

@Singleton
public class GameService {
    @Inject
    Provider<EntityManager> entityManagerProvider;

    @UnitOfWork
    public GameState getGame(String username) {

        if (username != null) {

            EntityManager entityManager = entityManagerProvider.get();

            GameState state;
            try {
                TypedQuery<GameState> q = entityManager.createQuery("SELECT x FROM GameState x WHERE username = :usernameParam", GameState.class);
                state = q.setParameter("usernameParam", username).getSingleResult();
            } catch (Exception e) {
                state = null;
            }

            if (state == null) {
                state = new GameState(username, "", "");
            }

            return state;
        }

        return null;
    }

    @Transactional
    public boolean saveGame(GameState gameState) {

        EntityManager entityManager = entityManagerProvider.get();

        GameState state;
        try {
            TypedQuery<GameState> q = entityManager.createQuery("SELECT x FROM GameState x WHERE username = :usernameParam", GameState.class);
            state = q.setParameter("usernameParam", gameState.username).getSingleResult();
        } catch (Exception e) {
            state = null;
        }

        if (state == null) {
            entityManager.persist(gameState);
        } else {
            state.guesses = gameState.guesses;
            state.word = gameState.word;
        }

        return false;
    }

}
