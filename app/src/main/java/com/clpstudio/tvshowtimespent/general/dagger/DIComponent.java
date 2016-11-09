package com.clpstudio.tvshowtimespent.general.dagger;

import com.clpstudio.tvshowtimespent.TvShowApplication;
import com.clpstudio.tvshowtimespent.presentation.activities.DetailActivity;
import com.clpstudio.tvshowtimespent.presentation.activities.FavoritesActivity;
import com.clpstudio.tvshowtimespent.presentation.activities.MainActivity;
import com.clpstudio.tvshowtimespent.presentation.activities.SearchActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {
                ApplicationModule.class,
                NetworkModule.class,
        }
)
@Singleton
public interface DiComponent {
    TvShowApplication inject(TvShowApplication inject);

    MainActivity inject(MainActivity activity);

    DetailActivity inject(DetailActivity activity);

    SearchActivity inject(SearchActivity activity);

    FavoritesActivity inject(FavoritesActivity activity);
}
