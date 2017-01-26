package dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import models.GameState;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

/**
 * Created by jodan on 2017/01/20.
 */

@Singleton
public class GameDao {
    @Inject
    Provider<EntityManager> entityManagerProvider;

    @UnitOfWork
    public GameState getGameState(String username) {

        if (username != null) {

            EntityManager entityManager = entityManagerProvider.get();

            GameState state;
            try {
                TypedQuery<GameState> q = entityManager.createQuery("SELECT x FROM GameState x WHERE username = :usernameParam", GameState.class);
                state = q.setParameter("usernameParam", username).getSingleResult();
            } catch (Exception e) {
                state = null;
            }

            return state;
        }

        return null;
    }

    @Transactional
    public void saveGame(GameState gameState) {

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

            entityManager.setFlushMode(FlushModeType.COMMIT);
            entityManager.flush();
        } else {
            state.guesses = gameState.guesses;
            state.word = gameState.word;
        }
    }
}
