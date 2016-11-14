package com.clpstudio.tvshowtimespent.datalayer.repository;

import com.clpstudio.tvshowtimespent.datalayer.network.listener.OnSuccess;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;
import com.clpstudio.tvshowtimespent.datalayer.network.model.TvShow;
import com.clpstudio.tvshowtimespent.datalayer.repository.abstraction.ITvRepository;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.TvCachedDataSource;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.TvOnlineDataSource;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;


public class TvRepository implements ITvRepository {

    private TvOnlineDataSource onlineDataSource;
    private TvCachedDataSource cachedDataSource;

    @Inject
    public TvRepository(TvOnlineDataSource onlineDataSource, TvCachedDataSource cachedDataSource) {
        this.onlineDataSource = onlineDataSource;
        this.cachedDataSource = cachedDataSource;
    }

    @Override
    public Observable<TvShow> getTvShowById(String id) {
        return onlineDataSource.getTvShowById(id);
    }

    @Override
    public Observable<ApiModel> getTvShowByName(String name) {
       return onlineDataSource.getTvShowByName(name);
    }

    @Override
    public Observable<List<String>> getSuggestions() {
        return cachedDataSource.getSuggestions();
    }

    @Override
    public void addSuggestion(String suggestion, OnSuccess<Object> listener) {
        cachedDataSource.addSuggestion(suggestion, listener);
    }

}
