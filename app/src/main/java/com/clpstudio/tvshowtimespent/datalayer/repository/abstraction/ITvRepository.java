package com.clpstudio.tvshowtimespent.datalayer.repository.abstraction;

/**
 * Created by clapalucian on 11/9/16.
 */

public interface ITvRepository {

    void getTvShowById(String id);

    void getTvShowByName(String name);

}
