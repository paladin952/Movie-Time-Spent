package com.clpstudio.tvshowtimespent.general.dagger;

import android.app.Application;
import android.content.Context;

import com.clpstudio.tvshowtimespent.TvShowApplication;
import com.clpstudio.tvshowtimespent.datalayer.persistance.preferences.ISharedPreferences;
import com.clpstudio.tvshowtimespent.datalayer.persistance.preferences.SharedPreferencesUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by clapalucian on 11/9/16.
 */

@Module
public class ApplicationModule {

    Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    TvShowApplication providesTvShowApplication() {
        return (TvShowApplication) application;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public ISharedPreferences providesSharedPreferences(Context context) {
        return new SharedPreferencesUtils(context);
    }

}
