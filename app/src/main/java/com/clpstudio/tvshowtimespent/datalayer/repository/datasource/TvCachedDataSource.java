package com.clpstudio.tvshowtimespent.datalayer.repository.datasource;

import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;
import com.clpstudio.tvshowtimespent.datalayer.network.model.TvShow;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.abstraction.ITvDataSource;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by clapalucian on 11/9/16.
 */

public class TvCachedDataSource implements ITvDataSource {

    @Inject
    public TvCachedDataSource() {
    }

    @Override
    public Observable<TvShow> getTvShowById(String id) {
        return null;
    }

    @Override
    public Observable<ApiModel> getTvShowByName(String name) {
        return null;
    }
}
