package com.clpstudio.tvshowtimespent.general.dagger;

import com.clpstudio.tvshowtimespent.datalayer.repository.TvRepository;
import com.clpstudio.tvshowtimespent.datalayer.repository.abstraction.ITvRepository;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.TvCachedDataSource;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.TvOnlineDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by clapalucian on 11/9/16.
 */

@Module(includes = {ApplicationModule.class})
public class DataModule {

    @Provides
    @Singleton
    public ITvRepository providesTvRepository(TvOnlineDataSource onlineDataSource, TvCachedDataSource cachedDataSource) {
        return new TvRepository(onlineDataSource, cachedDataSource);
    }

}
