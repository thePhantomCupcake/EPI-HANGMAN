package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dao.GameDao;
import models.GameState;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jodan on 2017/01/20.
 */

@Singleton
public class GameService {
    @Inject
    private GameDao gameDao;

    public GameState getGame(String username) {

        if (username != null) {
            GameState state = gameDao.getGameState(username);

            if (state == null) {
                String url = "http://randomword.setgetgo.com/get.php";
                StringBuffer response = getWord(url);

                state = new GameState(username, response.toString(), "");
                gameDao.saveGame(state);
            }
            return state;
        }
        return null;
    }

    public void saveGame(GameState gameState) {
        GameState state = gameDao.getGameState(gameState.username);

        if (state != null) {
            state.guesses = gameState.guesses;
            state.word = gameState.word;
        }

        gameDao.saveGame(gameState);
    }

    public void newGame(String username) {
        GameState state = gameDao.getGameState(username);

        String url = "http://randomword.setgetgo.com/get.php";

        if (state == null) {
            StringBuffer response = getWord(url);

            state = new GameState(username, response.toString(), "");
        } else {
            StringBuffer response = getWord(url);

            state.guesses = "";
            state.word = response.toString();
        }

        gameDao.saveGame(state);
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
