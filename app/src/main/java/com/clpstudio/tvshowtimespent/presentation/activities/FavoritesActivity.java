package com.clpstudio.tvshowtimespent.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.TvShowApplication;

import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ((TvShowApplication) getApplication()).getDiComponent().inject(this);
        ButterKnife.bind(this);
    }
}
