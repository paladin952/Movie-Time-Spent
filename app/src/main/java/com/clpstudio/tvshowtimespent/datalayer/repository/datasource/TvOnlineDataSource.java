package com.clpstudio.tvshowtimespent.datalayer.repository.datasource;

import android.content.Context;

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.datalayer.network.interfaces.IMovieDbService;
import com.clpstudio.tvshowtimespent.datalayer.network.listener.ApiListener;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;
import com.clpstudio.tvshowtimespent.datalayer.network.model.TvShow;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.abstraction.ITvDataSource;

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
    public void getTvShowById(String id, ApiListener<TvShow, String> listener) {
        onlineApiService
                .getTvShowById(id, context.getResources().getString(R.string.api_key))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    listener.onError("error");
                    return Observable.empty();
                })
                .subscribe(listener::onSuccess);
    }

    @Override
    public void getTvShowByName(String name, ApiListener<ApiModel, String> listener) {
        onlineApiService
                .getTvShowsByName(name, context.getString(R.string.api_key))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    listener.onError("error");
                    return Observable.empty();
                })
                .onErrorResumeNext(throwable1 -> Observable.empty())
                .subscribe(apiModel -> {
                    if (apiModel == null) {
                        listener.onError("error");
                    } else {
                        listener.onSuccess(apiModel);
                    }
                });
    }
}
