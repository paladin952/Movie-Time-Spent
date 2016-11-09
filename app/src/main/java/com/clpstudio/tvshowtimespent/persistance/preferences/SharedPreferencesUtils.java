package com.clpstudio.tvshowtimespent.persistance.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class SharedPreferencesUtils implements ISharedPreferences {

    /**
     * The shared preferences object
     */
    private SharedPreferences mSharedPreferences;

    /**
     * The gson object for serialization
     */
    private Gson mGson;

    public SharedPreferencesUtils(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mGson = new Gson();
    }

    /**
     * Set the value of the preference
     *
     * @param key   The key
     * @param value The value
     */
    private void setPreferenceValue(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }


    /**
     * Set the value of the preference
     *
     * @param key   The key
     */
    private boolean getPreferenceValue(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * get the value of the preference
     *
     * @param key   The key
     */
    private int getPreferenceValue(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * get the value of the preference
     *
     * @param key   The key
     */
    private String getPreferenceValue(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * Set the value of the preference
     *
     * @param key   The key
     * @param value The value
     */
    public void setPreferenceValue(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

}
