package com.clpstudio.tvshowtimespent.datalayer.datasource;

import com.clpstudio.tvshowtimespent.datalayer.datasource.abstraction.ITvDataSource;
import com.clpstudio.tvshowtimespent.datalayer.network.interfaces.IMovieDbService;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;

import javax.inject.Inject;

public class TvOnlineDataSource implements ITvDataSource {

    private IMovieDbService onlineApiService;

    @Inject
    public TvOnlineDataSource(IMovieDbService onlineApiService) {
        this.onlineApiService = onlineApiService;
    }


    @Override
    public ApiModel getTvShowById(String id) {
        return null;
    }

    @Override
    public ApiModel getTvShowByName(String name) {
        return null;
    }
}
