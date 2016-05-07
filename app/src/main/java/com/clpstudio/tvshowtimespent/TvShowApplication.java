package com.clpstudio.tvshowtimespent;

import android.app.Application;

import com.crittercism.app.Crittercism;

/**
 * Created by CLP STUDIO
 */
public class TvShowApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initCrittercism();
    }

    /**
     * Setup the crittercism tool
     */
    private void initCrittercism() {
        if(!BuildConfig.DEBUG){
            Crittercism.initialize(getApplicationContext(), getString(R.string.crittercism_app_id));
        }
    }
}
