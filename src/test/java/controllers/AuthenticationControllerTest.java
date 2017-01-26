package controllers;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import ninja.NinjaTest;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

public class AuthenticationControllerTest extends NinjaTest {
    @Before
    public void setup() {
        Map<String, String> headers = Maps.newHashMap();

        Map<String, String> formParameters = Maps.newHashMap();
        formParameters.put("username", "bob@gmail.com");
        formParameters.put("password", "secret");

        ninjaTestBrowser.makePostRequestWithFormParameters(getServerAddress()
                + "register", headers, formParameters);
    }

    @Test
    public void testLoginLogout() {

        Map<String, String> headers = Maps.newHashMap();

        // Login
        Map<String, String> formParameters = Maps.newHashMap();
        formParameters.put("username", "bob@gmail.com");
        formParameters.put("password", "secret");

        String result = ninjaTestBrowser.makePostRequestWithFormParameters(getServerAddress()
                + "login", headers, formParameters);

        // Logout
        String result1 = ninjaTestBrowser.makeRequest(getServerAddress() + "logout", headers);

        assertTrue(result.contains("Login successful."));
        assertTrue(result1.contains("Logout successful."));
    }

}
