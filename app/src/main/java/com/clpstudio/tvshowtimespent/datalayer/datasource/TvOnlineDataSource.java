package com.clpstudio.tvshowtimespent.datalayer.datasource;

import android.content.Context;

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.datalayer.datasource.abstraction.ITvDataSource;
import com.clpstudio.tvshowtimespent.datalayer.network.interfaces.IMovieDbService;
import com.clpstudio.tvshowtimespent.datalayer.network.listener.ApiListener;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TvOnlineDataSource implements ITvDataSource {

    private IMovieDbService onlineApiService;
    private Context context;

    @Inject
    public TvOnlineDataSource(IMovieDbService onlineApiService, Context context) {
        this.onlineApiService = onlineApiService;
        this.context = context;
    }


    @Override
    public ApiModel getTvShowById(String id, ApiListener<ApiModel> listener) {

        return null;
    }

    @Override
    public ApiModel getTvShowByName(String name, ApiListener<ApiModel> listener) {
        onlineApiService
                .getTvShowsByName(name, context.getString(R.string.api_key))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    return Observable.empty();
                })
        .onErrorResumeNext(throwable1 -> Observable.empty())
                .subscribe(apiModel -> {
                    if (apiModel == null) {
                        return;
                    }

                });
        return null;
    }
}
