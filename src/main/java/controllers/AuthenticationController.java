package controllers;

import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.session.Session;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.UserDao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Singleton
public class AuthenticationController {

    @Inject
    UserDao userDao;

    public Result login(Context context) {

        return Results.html();

    }

    public Result loginPost(@Param("username") String username,
                            @Param("password") String password,
                            @Param("rememberMe") Boolean rememberMe,
                            Context context) {

        boolean isUserNameAndPasswordValid = userDao.isUserAndPasswordValid(username, password);

        if (isUserNameAndPasswordValid) {
            Session session = context.getSession();
            session.put("username", username);

            if (rememberMe != null && rememberMe) {
                session.setExpiryTime(24 * 60 * 60 * 1000L);
            }

            context.getFlashScope().success("login.loginSuccessful");

            return Results.redirect("/");

        } else {

            // something is wrong with the input or password not found.
            context.getFlashScope().put("username", username);
            context.getFlashScope().put("rememberMe", rememberMe);
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

        boolean isUserNew = userDao.addUser(username, password);

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
