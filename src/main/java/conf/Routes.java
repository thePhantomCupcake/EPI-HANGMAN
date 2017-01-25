/**
 * Copyright (C) 2012-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package conf;

import controllers.GameController;
import ninja.AssetsController;
import ninja.FilterWith;
import ninja.Router;
import ninja.SecureFilter;
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

        router.GET().route("/get-game").with(GameController.class, "game");
        router.POST().route("/save-game").with(GameController.class, "saveGame");
        router.GET().route("/new-game").with(GameController.class, "newGame");

        router.GET().route("/.*").with(ApplicationController.class, "index");


    }

}
