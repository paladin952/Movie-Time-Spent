package com.clpstudio.tvshowtimespent.datalayer.datasource.abstraction;

import com.clpstudio.tvshowtimespent.datalayer.network.listener.ApiListener;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;

/**
 * Created by clapalucian on 11/9/16.
 */

public interface ITvDataSource {

    ApiModel getTvShowById(String id, ApiListener<ApiModel> listener);

    ApiModel getTvShowByName(String name, ApiListener<ApiModel> listener);

}
