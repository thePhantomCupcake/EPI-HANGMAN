package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import ninja.NinjaTest;

import org.junit.Test;

import com.google.inject.Injector;

import java.util.Map;

public class ApplicationControllerTest extends NinjaTest {
    @Test
    public void testFilter() {

        String result = ninjaTestBrowser.makeRequest(getServerAddress()
                + "/");

        assertTrue(result.contains("Username / Email"));
    }

}
