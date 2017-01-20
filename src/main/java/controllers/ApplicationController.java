
package controllers;

import java.util.List;
import java.util.Map;

import ninja.*;

import com.google.common.collect.Maps;
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
