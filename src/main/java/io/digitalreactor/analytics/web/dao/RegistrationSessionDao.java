package io.digitalreactor.analytics.web.dao;

import java.util.HashMap;
import java.util.UUID;

public class RegistrationSessionDao {
    private HashMap<String, String> sessionStorage;

    public RegistrationSessionDao() {
        this.sessionStorage = new HashMap<String, String>();
    }

    public String makeSession(String userName) {
        String uuid = UUID.randomUUID().toString();
        sessionStorage.put(uuid, userName);

        return uuid;
    }
}
