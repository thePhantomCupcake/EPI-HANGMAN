package conf;

import controllers.GameController;
import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import ninja.utils.NinjaProperties;
import com.google.inject.Inject;
import controllers.ApplicationController;
import controllers.AuthenticationController;

public class Routes implements ApplicationRoutes {

    @Inject
    NinjaProperties ninjaProperties;

    /**
     * Using a (almost) nice DSL we can configure the router.
     *
     * The second argument NinjaModuleDemoRouter contains all routes of a
     * submodule. By simply injecting it we activate the routes.
     *
     * @param router
     *            The default router of this application
     */
    @Override
    public void init(Router router) {

        // puts test data into db:
        if (!ninjaProperties.isProd()) {
            router.GET().route("/setup").with(ApplicationController.class, "setup");
        }

        router.GET().route("/webjars/{fileName: .*}").with(AssetsController.class, "serveWebJars");

        // Authorization
        router.GET().route("/").with(ApplicationController.class, "index");
        router.GET().route("/login").with(AuthenticationController.class, "login");
        router.POST().route("/login").with(AuthenticationController.class, "loginPost");
        router.GET().route("/logout").with(AuthenticationController.class, "logout");

        router.GET().route("/register").with(AuthenticationController.class, "register");
        router.POST().route("/register").with(AuthenticationController.class, "registerPost");

        // Assets (pictures / javascript)
        router.GET().route("/assets/webjars/{fileName: .*}").with(AssetsController.class, "serveWebJars");
        router.GET().route("/assets/{fileName: .*}").with(AssetsController.class, "serveStatic");

        //Game
        router.GET().route("/get-game").with(GameController.class, "game");
        router.POST().route("/save-game").with(GameController.class, "saveGame");
        router.GET().route("/new-game").with(GameController.class, "newGame");
        router.POST().route("/add-win").with(GameController.class,"addCorrectlyGuessed");

        //Index
        router.GET().route("/.*").with(ApplicationController.class, "index");


    }

}
