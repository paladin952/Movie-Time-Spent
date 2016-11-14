package com.clpstudio.tvshowtimespent.datalayer.repository.datasource;

import com.clpstudio.tvshowtimespent.datalayer.network.listener.OnSuccess;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiModel;
import com.clpstudio.tvshowtimespent.datalayer.network.model.TvShow;
import com.clpstudio.tvshowtimespent.datalayer.persistance.SuggestionDAO;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.abstraction.ITvDataSource;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by clapalucian on 11/9/16.
 */

public class TvCachedDataSource implements ITvDataSource {

    private SuggestionDAO suggestionDAO;

    @Inject
    public TvCachedDataSource(SuggestionDAO suggestionDAO) {
        this.suggestionDAO = suggestionDAO;
    }

    @Override
    public Observable<TvShow> getTvShowById(String id) {
        return null;
    }

    @Override
    public Observable<ApiModel> getTvShowByName(String name) {
        return null;
    }

    public Observable<List<String>> getSuggestions() {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                suggestionDAO.open();
                List<String> res = suggestionDAO.getAllShows();
                suggestionDAO.close();
                subscriber.onNext(res);
            }
        });
    }

    public void addSuggestion(String suggestion, OnSuccess<Object> listener) {
        Executors.newCachedThreadPool().submit(() -> {
            suggestionDAO.open();
            suggestionDAO.addSuggestion(suggestion);
            suggestionDAO.close();

            listener.onSuccess(null);
            return null;
        });
    }
}
