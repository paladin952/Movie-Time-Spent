package com.clpstudio.tvshowtimespent.datalayer.repository.abstraction;

import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;
import com.clpstudio.tvshowtimespent.datalayer.network.model.TvShow;

import rx.Observable;

/**
 * Created by clapalucian on 11/9/16.
 */

public interface ITvRepository {

    Observable<TvShow> getTvShowById(String id);

    Observable<ApiModel> getTvShowByName(String name);

}
