package com.clpstudio.tvshowtimespent.datalayer.network.interfaces;

import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;
import com.clpstudio.tvshowtimespent.datalayer.network.model.TvShow;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface IMovieDbService {

    @GET("tv/{id}")
    Observable<TvShow> getTvShowById(@Path("id")String id,
                                     @Query("api_key")String apiKey);

    @GET("search/tv")
    Observable<ApiModel> getTvShowsByName(
            @Query("query")String query,
            @Query("api_key")String apiKey);
}
