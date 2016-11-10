package com.clpstudio.tvshowtimespent.datalayer.repository;

import com.clpstudio.tvshowtimespent.datalayer.network.listener.ApiListener;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;
import com.clpstudio.tvshowtimespent.datalayer.repository.abstraction.ITvRepository;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.TvCachedDataSource;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.TvOnlineDataSource;

import javax.inject.Inject;

/**
 * Created by clapalucian on 11/9/16.
 */

public class TvRepository implements ITvRepository {

    private TvOnlineDataSource onlineDataSource;
    private TvCachedDataSource cachedDataSource;

    @Inject
    public TvRepository(TvOnlineDataSource onlineDataSource, TvCachedDataSource cachedDataSource) {
        this.onlineDataSource = onlineDataSource;
        this.cachedDataSource = cachedDataSource;
    }

    @Override
    public void getTvShowById(String id, ApiListener<ApiModel> listener) {
        onlineDataSource.getTvShowById(id, listener);
    }

    @Override
    public void getTvShowByName(String name, ApiListener<ApiModel> listener) {
        onlineDataSource.getTvShowByName(name, listener);
    }

}
