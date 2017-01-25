package controllers;

/**
 * Created by jodan on 2017/01/20.
 */

import com.fasterxml.jackson.core.JsonParser;
import com.google.inject.Inject;
import models.GameState;
import ninja.*;
import ninja.params.Param;
import ninja.session.Session;
import services.GameService;

public class GameController {
    @Inject
    GameService gameService;

    public Result game(Session session) {
        GameState gameState = gameService.getGame(session.get("username"));
        System.out.println("Fetched game for: " + gameState.username);

        return Results.json().render(gameState);
    }

    public Result saveGame(@Param("username") String username,
                           @Param("guesses") String guesses,
                           @Param("word") String word,
                           Context context) {
        GameState newState = new GameState(username, word, guesses);

        gameService.saveGame(newState);

        return Results.json().render(newState);
    }

    public Result newGame(Session session) {
        gameService.newGame(session.get("username"));
        GameState state = gameService.getGame(session.get("username"));

        return Results.json().render(state);
    }
}
