package com.clpstudio.tvshowtimespent.datalayer.repository.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by clapalucian on 12/20/16.
 */

public class UserRepository {

    private static Map<String, String> users = new HashMap<>();

    @Inject
    public UserRepository() {
    }

    static {
        users.put("user1", "user1");
        users.put("user2", "user2");
        users.put("user3", "user3");
        users.put("user4", "user4");
        users.put("user5", "user5");
        users.put("user6", "user6");
        users.put("user7", "user7");
    }

    public Observable<Boolean> login(String username, String password) {
        return Observable.fromCallable(() -> users.containsKey(username) && users.get(username).equals(password));
    }

}
