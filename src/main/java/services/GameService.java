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
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
                String url = "http://randomword.setgetgo.com/get.php";
                StringBuffer response = getWord(url);
                ;

                state = new GameState(username, response.toString(), "");
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

    @Transactional
    public void newGame(String username) {

        EntityManager entityManager = entityManagerProvider.get();

        GameState state;
        try {
            TypedQuery<GameState> q = entityManager.createQuery("SELECT x FROM GameState x WHERE username = :usernameParam", GameState.class);
            state = q.setParameter("usernameParam", username).getSingleResult();
        } catch (Exception e) {
            state = null;
        }

        if (state == null) {
            String url = "http://randomword.setgetgo.com/get.php";
            StringBuffer response = getWord(url);

            state = new GameState(username, response.toString(), "");
        } else {
            String url = "http://randomword.setgetgo.com/get.php";
            StringBuffer response = getWord(url);

            state.guesses = "";
            state.word = response.toString();
        }
    }

    private StringBuffer getWord(String url) {

        StringBuffer response = new StringBuffer();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            con.setRequestProperty("User-Agent", "MOZILLA/5.0");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


}
