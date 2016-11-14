package com.clpstudio.tvshowtimespent.presentation.searchscreen;

import android.util.Log;

import com.clpstudio.tvshowtimespent.datalayer.repository.TvRepository;
import com.clpstudio.tvshowtimespent.general.mvp.abstraction.BaseMvpPresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by clapalucian on 11/12/16.
 */

public class SearchScreenPresenterImpl extends BaseMvpPresenter<ISearchScreenPresenter.View> implements ISearchScreenPresenter {

    private TvRepository tvRepository;

    @Inject
    public SearchScreenPresenterImpl(TvRepository tvRepository) {
        this.tvRepository = tvRepository;
    }

    @Override
    public void search(String query) {
        tvRepository.getTvShowByName(query)
                .subscribe(apiModel -> {
                    Log.d("luci", apiModel.toString());

                    if (!apiModel.getApiResults().isEmpty()) {
                        addSuggestion(query);
                    }
                });
    }

    @Override
    public void refreshSuggestions() {
        tvRepository.getSuggestions()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> {
                    if (view() != null) {
                        view().refreshSuggestions(strings);
                    }
                });
    }

    private void addSuggestion(String suggestion) {
        tvRepository.addSuggestion(suggestion, data -> {
            refreshSuggestions();
        });
    }


}
