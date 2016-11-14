package com.clpstudio.tvshowtimespent.general.mvp.dagger;

import com.clpstudio.tvshowtimespent.datalayer.repository.TvRepository;
import com.clpstudio.tvshowtimespent.general.dagger.ApplicationModule;
import com.clpstudio.tvshowtimespent.presentation.searchscreen.ISearchScreenPresenter;
import com.clpstudio.tvshowtimespent.presentation.searchscreen.SearchScreenPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApplicationModule.class})
public class MvpModule {

    @Provides
    public ISearchScreenPresenter providesSearchScreenPresenter(TvRepository tvRepository) {
        return new SearchScreenPresenterImpl(tvRepository);
    }


}
