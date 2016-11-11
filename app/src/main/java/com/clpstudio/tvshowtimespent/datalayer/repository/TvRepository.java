package com.clpstudio.tvshowtimespent.datalayer.repository;

import com.clpstudio.tvshowtimespent.datalayer.repository.abstraction.ITvRepository;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.TvCachedDataSource;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.TvOnlineDataSource;

import javax.inject.Inject;


public class TvRepository implements ITvRepository {

    private TvOnlineDataSource onlineDataSource;
    private TvCachedDataSource cachedDataSource;

    @Inject
    public TvRepository(TvOnlineDataSource onlineDataSource, TvCachedDataSource cachedDataSource) {
        this.onlineDataSource = onlineDataSource;
        this.cachedDataSource = cachedDataSource;
    }

    @Override
    public void getTvShowById(String id) {
        onlineDataSource.getTvShowById(id);
    }

    @Override
    public void getTvShowByName(String name) {
        onlineDataSource.getTvShowByName(name);
    }

}
