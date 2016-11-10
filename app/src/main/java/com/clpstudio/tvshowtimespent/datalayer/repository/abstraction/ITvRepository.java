package com.clpstudio.tvshowtimespent.datalayer.repository.abstraction;

import com.clpstudio.tvshowtimespent.datalayer.network.listener.ApiListener;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;

/**
 * Created by clapalucian on 11/9/16.
 */

public interface ITvRepository {

    void getTvShowById(String id, ApiListener<ApiModel> listener);

    void getTvShowByName(String name, ApiListener<ApiModel> listener);

}
