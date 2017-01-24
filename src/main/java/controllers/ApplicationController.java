
package controllers;

import filters.MyFilter;
import ninja.*;

import com.google.inject.Inject;

import dao.SetupDao;

public class ApplicationController {

    @Inject
    SetupDao setupDao;

    public ApplicationController() {

    }

    public Result setup() {

        setupDao.setup();

        return Results.ok();

    }

    @FilterWith(MyFilter.class)
    public Result index() {

        return Results.html();

    }
}
