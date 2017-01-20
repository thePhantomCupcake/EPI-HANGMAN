package controllers;

import com.google.inject.Inject;
import ninja.*;

public class MyFilter implements Filter {
    public static final String USERNAME = "username";
    private final Ninja ninja;

    @Inject
    public MyFilter(Ninja ninja) {
        this.ninja = ninja;
    }

    public Result filter(FilterChain chain, Context context) {
        if (context.getSession() != null && context.getSession().get("username") != null) {
            return chain.next(context);
        } else {
            return Results.redirect("/login");
        }
    }
}