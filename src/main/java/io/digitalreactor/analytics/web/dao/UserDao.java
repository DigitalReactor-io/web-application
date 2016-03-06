package io.digitalreactor.analytics.web.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserDao {

    private HashMap<String, String> userStorage;
    private HashMap<String, String> tokenStorage;

    public UserDao() {
        userStorage = new HashMap<String, String>();
        tokenStorage = new HashMap<>();
    }

    public void add(String userName, String password) {
        userStorage.put(userName, password);
    }

    public void addToken(String userName, String token) {
        tokenStorage.put(userName, token);
    }

    public List<String> getTokens(String userName) {
        String token = tokenStorage.get(userName);
        List<String> tokens = new ArrayList<>();
        tokens.add(token);

        return tokens;
    }

}
