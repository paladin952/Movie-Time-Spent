package com.clpstudio.tvshowtimespent.general.dagger;

import android.content.Context;

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.datalayer.network.RetrofitServiceFactory;
import com.clpstudio.tvshowtimespent.datalayer.network.interfaces.IMovieDbService;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApplicationModule.class})
public class NetworkModule {

    @Provides
    public IMovieDbService providesApiService(Context context) {
        String url = context.getResources().getString(R.string.api_base_url);
        return RetrofitServiceFactory.createRetrofitService(IMovieDbService.class, url);
    }

}
