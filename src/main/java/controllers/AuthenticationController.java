package controllers;

import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.session.Session;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import services.AuthenticationService;

@Singleton
public class AuthenticationController {

    @Inject
    AuthenticationService authService;

    public Result login(Context context) {
        return Results.html();
    }

    public Result loginPost(@Param("username") String username,
                            @Param("password") String password,
                            Context context) {

        boolean isUserNameAndPasswordValid = authService.login(username, password);

        if (isUserNameAndPasswordValid) {
            Session session = context.getSession();
            session.put("username", username);

            context.getFlashScope().success("login.loginSuccessful");

            return Results.redirect("/");
        } else {
            context.getFlashScope().put("username", username);
            context.getFlashScope().error("login.errorLogin");

            return Results.redirect("/login");
        }

    }

    public Result register(Context context) {

        return Results.html();

    }

    public Result registerPost(@Param("username") String username,
                               @Param("password") String password,
                               Context context) {

        boolean isUserNew = authService.register(username, password);

        if (isUserNew) {
            Session session = context.getSession();
            session.put("username", username);

            context.getFlashScope().success("login.loginSuccessful");

            return Results.redirect("/");
        } else {

            context.getFlashScope().put("username", username);
            context.getFlashScope().error("register.errorRegister");

            return Results.redirect("/register");
        }

    }

    public Result logout(Context context) {
        context.getSession().clear();
        context.getFlashScope().success("login.logoutSuccessful");

        return Results.html();
    }

}
