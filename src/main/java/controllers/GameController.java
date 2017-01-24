package controllers;

/**
 * Created by jodan on 2017/01/20.
 */

import com.google.inject.Inject;
import models.GameState;
import ninja.*;
import ninja.session.Session;
import services.GameService;

public class GameController {
    @Inject
    GameService gameService;

    public Result game(Session session) {
        GameState gameState = gameService.getGame(session.get("username"));

        return Results.json().render(gameState);
    }
}
