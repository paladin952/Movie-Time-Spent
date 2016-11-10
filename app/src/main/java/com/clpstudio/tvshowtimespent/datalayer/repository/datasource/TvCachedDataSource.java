package com.clpstudio.tvshowtimespent.datalayer.repository.datasource;

import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.abstraction.ITvDataSource;
import com.clpstudio.tvshowtimespent.datalayer.network.listener.ApiListener;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;

import javax.inject.Inject;

/**
 * Created by clapalucian on 11/9/16.
 */

public class TvCachedDataSource implements ITvDataSource {

    @Inject
    public TvCachedDataSource() {
    }

    @Override
    public void getTvShowById(String id, ApiListener<ApiModel> listener) {
    }

    @Override
    public void getTvShowByName(String name, ApiListener<ApiModel> listener) {
    }
}
