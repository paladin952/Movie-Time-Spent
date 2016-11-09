package com.clpstudio.tvshowtimespent.datalayer.repository.datasource;

import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.abstraction.ITvDataSource;
import com.clpstudio.tvshowtimespent.datalayer.network.listener.ApiListener;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;

/**
 * Created by clapalucian on 11/9/16.
 */

public class TvCachedDataSource implements ITvDataSource {

    @Override
    public void getTvShowById(String id, ApiListener<ApiModel> listener) {
    }

    @Override
    public void getTvShowByName(String name, ApiListener<ApiModel> listener) {
    }
}