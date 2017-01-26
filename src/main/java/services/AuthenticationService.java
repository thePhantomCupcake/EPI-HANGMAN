package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.UserDao;
import models.Profile;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jodan on 2017/01/20.
 */
@Singleton
public class AuthenticationService {
    @Inject
    UserDao userDao;

    public boolean login(String username, String password) {

        if (username != null && password != null) {

            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            Profile profile = userDao.getProfile(username);

            if (profile != null) {
                if (profile.password.equals(new String(encodedhash))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean register(String username, String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        Profile profile = userDao.getProfile(username);

        if (profile == null) {
            Profile newProfile = new Profile(username, new String(encodedhash));
            return userDao.saveProfile(newProfile);
        }
        return false;
    }

}
