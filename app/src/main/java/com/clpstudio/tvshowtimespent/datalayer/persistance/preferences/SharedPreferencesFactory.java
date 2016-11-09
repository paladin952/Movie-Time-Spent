package com.clpstudio.tvshowtimespent.datalayer.persistance.preferences;

import android.content.Context;

public final class SharedPreferencesFactory {

    /**
     * The instance for the class
     */
    private static ISharedPreferences sInstance;

    /**
     * The private constructor
     */
    private SharedPreferencesFactory(){

    }

    /**
     * The singleton pattern for getting shared preferences
     * @param context The context
     * @return The shared preferences object
     */
    public static ISharedPreferences get(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferencesUtils(context);
        }
        return sInstance;
    }
}
