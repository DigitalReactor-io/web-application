package io.digitalreactor.analytics.web.dao;

import java.util.HashMap;

public class UserDao {

    private HashMap<String, String> userStorage;

    public UserDao() {
        userStorage = new HashMap<String, String>();
    }

    public void add(String userName, String password) {
        userStorage.put(userName, password);
    }
}
