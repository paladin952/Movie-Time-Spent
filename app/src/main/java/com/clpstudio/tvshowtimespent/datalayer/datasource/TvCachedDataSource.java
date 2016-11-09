package com.clpstudio.tvshowtimespent.datalayer.datasource;

import com.clpstudio.tvshowtimespent.datalayer.datasource.abstraction.ITvDataSource;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;

/**
 * Created by clapalucian on 11/9/16.
 */

public class TvCachedDataSource implements ITvDataSource {

    @Override
    public ApiModel getTvShowById(String id) {
        return null;
    }

    @Override
    public ApiModel getTvShowByName(String name) {
        return null;
    }
}
