package com.clpstudio.tvshowtimespent.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.clpstudio.tvshowtimespent.R;

public class AboutActivity extends AppCompatActivity {

    /**
     * The toolbar
     */
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupToolbar();

    }

    /**
     * Setting up the toolbar
     */
    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setTitle(getString(R.string.about_activity_name));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(mToolbar);
    }


}
