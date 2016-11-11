package com.clpstudio.tvshowtimespent.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.TvShowApplication;
import com.clpstudio.tvshowtimespent.datalayer.repository.datasource.TvOnlineDataSource;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import javax.inject.Inject;

public class SearchActivity extends AppCompatActivity {

    @InjectExtra
    String searchText;

    @Inject
    TvOnlineDataSource tvOnlineDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ((TvShowApplication) getApplication()).getDiComponent().inject(this);
        Dart.inject(this);
    }

    private void search() {
        tvOnlineDataSource.getTvShowByName(searchText)
                .subscribe(apiModel -> {
                    if (apiModel != null && !apiModel.getApiResults().isEmpty()) {

                    }else {
                        //TODO show error
                    }
                });
    }
}
