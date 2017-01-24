package conf;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

import ninja.lifecycle.Start;
import ninja.utils.NinjaProperties;

import com.google.inject.Inject;


public class StartupActions {

    private NinjaProperties ninjaProperties;

    @Inject
    private EntityManager entityManager;

    @Inject
    public StartupActions(NinjaProperties ninjaProperties) {
        this.ninjaProperties = ninjaProperties;
    }

    @Start(order = 100)
    public void generateDummyDataWhenInTest() {

    }

}
