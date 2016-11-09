package com.clpstudio.tvshowtimespent;

import android.app.Application;

import com.clpstudio.tvshowtimespent.general.dagger.ApplicationModule;
import com.clpstudio.tvshowtimespent.general.dagger.DaggerDiComponent;
import com.clpstudio.tvshowtimespent.general.dagger.DiComponent;

public class TvShowApplication extends Application {

    private DiComponent diComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        diComponent = DaggerDiComponent.builder().applicationModule(new ApplicationModule(this)).build();
        diComponent.inject(this);

    }


    public DiComponent getDiComponent() {
        return diComponent;
    }

}
