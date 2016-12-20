package com.clpstudio.tvshowtimespent.bussiness.login;

import com.clpstudio.tvshowtimespent.datalayer.persistance.preferences.ISharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by clapalucian on 12/20/16.
 */

@Singleton
public class Session {

    private ISharedPreferences sharedPreferences;

    private String currentUser = "";

    @Inject
    public Session(ISharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        setCurrentUser(sharedPreferences.getCurrentUser());
    }


    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
        sharedPreferences.setCurrentUser(currentUser);
    }

    public boolean isLoggedIn() {
        return !currentUser.equals("");
    }
}
