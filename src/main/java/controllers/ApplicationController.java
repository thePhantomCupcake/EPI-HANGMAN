
package controllers;

import filters.MyFilter;
import ninja.*;

import com.google.inject.Inject;

public class ApplicationController {

    public ApplicationController() {

    }

    public Result setup() {

        return Results.ok();

    }

    @FilterWith(MyFilter.class)
    public static Result index() {

        return Results.html();

    }
}
