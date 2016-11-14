package com.clpstudio.tvshowtimespent.presentation.searchscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.clpstudio.Henson;
import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.TvShowApplication;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchActivity extends AppCompatActivity implements ISearchScreenPresenter.View{

    @InjectExtra
    String searchText;
    @Inject
    ISearchScreenPresenter presenter;

    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static void startActivity(Activity activity, String query) {
        activity.startActivity(getStartIntent(activity, query));
    }

    private static Intent getStartIntent(Activity activity, String query) {
        return Henson.with(activity)
                .gotoSearchActivity()
                .searchText(query)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ((TvShowApplication) getApplication()).getDiComponent().inject(this);
        ButterKnife.bind(this);
        Dart.inject(this);
        presenter.bindView(this);
        setupToolbar();
        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.refreshSuggestions();
    }

    /**
     * Setup the toolbar
     */
    private void setupToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void setListeners() {
        searchView.setVoiceSearch(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                presenter.search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void refreshSuggestions(List<String> suggestions) {
        String[] array = new String[suggestions.size()];
        suggestions.toArray(array);
        searchView.setSuggestions(array);
    }
}
