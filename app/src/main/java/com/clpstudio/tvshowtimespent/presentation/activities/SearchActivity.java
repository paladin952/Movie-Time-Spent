package com.clpstudio.tvshowtimespent.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.TvShowApplication;
import com.clpstudio.tvshowtimespent.datalayer.network.model.TvShow;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

public class SearchActivity extends AppCompatActivity {

    @InjectExtra
    String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ((TvShowApplication) getApplication()).getDiComponent().inject(this);
        Dart.inject(this);


    }
}
