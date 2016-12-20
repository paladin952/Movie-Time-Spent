package com.clpstudio.tvshowtimespent.general.dagger;

import com.clpstudio.tvshowtimespent.TvShowApplication;
import com.clpstudio.tvshowtimespent.general.mvp.dagger.MvpModule;
import com.clpstudio.tvshowtimespent.presentation.detailscreen.DetailActivity;
import com.clpstudio.tvshowtimespent.presentation.favoritescreen.FavoritesActivity;
import com.clpstudio.tvshowtimespent.presentation.login.LoginActivity;
import com.clpstudio.tvshowtimespent.presentation.mainscreen.MainActivity;
import com.clpstudio.tvshowtimespent.presentation.searchscreen.SearchActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {
                ApplicationModule.class,
                NetworkModule.class,
                MvpModule.class
        }
)
@Singleton
public interface DiComponent {
    TvShowApplication inject(TvShowApplication inject);

    MainActivity inject(MainActivity activity);

    DetailActivity inject(DetailActivity activity);

    SearchActivity inject(SearchActivity activity);

    FavoritesActivity inject(FavoritesActivity activity);

    LoginActivity inject(LoginActivity activity);
}
