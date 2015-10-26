package com.clpstudio.movetimespent.ui.activities;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.clpstudio.movetimespent.Adapters.AutocompleteAdapter;
import com.clpstudio.movetimespent.Adapters.MoviesListAdapter;
import com.clpstudio.movetimespent.Utils.UrlConstants;
import com.clpstudio.movetimespent.model.TvShow;
import com.clpstudio.movetimespent.network.VolleyRequestQueueSingletone;
import com.clpstudio.movetimespent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements AutocompleteAdapter.OnDropDownListClick{

    /**
     * Volley request queue
     */
    private RequestQueue requestQueue;

    /**
     * Api key
     */
    private String API_KEY;

    /**
     * The toolbar
     */
    private Toolbar toolbar;

    /**
     * The autocomplete edit text
     */
    private AutoCompleteTextView mAutoCompleteTextView;

    /**
     * The adapter for autocomplete edit text
     */
    private AutocompleteAdapter mAutocompleteAdapter;

    /**
     * The list of movies to calculate
     */
    private RecyclerView mMoviesList;

    private MoviesListAdapter mMoviesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkUi();
        setupToolbar();
        setupVolley();
        API_KEY = getString(R.string.api_key);
    }

    /**
     * Link the ui
     */
    private void linkUi() {
        mAutoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autocomplete_text_view);
        mAutocompleteAdapter = new AutocompleteAdapter(this, this);
        mAutoCompleteTextView.setAdapter(mAutocompleteAdapter);
        mAutoCompleteTextView.requestFocus();

        mMoviesList = (RecyclerView)findViewById(R.id.movies_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesListAdapter = new MoviesListAdapter(this);
        mMoviesList.setAdapter(mMoviesListAdapter);
    }


    /**
     * Setup volley library for http calls
     */
    private void setupVolley() {
        requestQueue = VolleyRequestQueueSingletone.getInstance(this).getRequestQueue();
    }

    /**
     * Setup the toolbar
     */
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Connect to server
     *
     * @param id String
     */
    private void findShowById(final String id) {
        final String url = UrlConstants.FIND_SHOW_BY_ID + id + "?api_key=" + API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("luci", response);
                try {
                    Log.d("luci", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String posterPath = jsonObject.getString("backdrop_path");
                    String name = jsonObject.getString("original_name");
                    String seasons = String.valueOf(jsonObject.getInt("number_of_seasons"));

                    TvShow show = new TvShow(Integer.parseInt(id), name);
                    show.setPosterUrl(posterPath);

                    mMoviesListAdapter.add(show);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("luci", "ERRROR" + error.getMessage());
            }
        });

        requestQueue.add(stringRequest);
    }

    @Override
    public void onSuggestionClick(TvShow show) {
        findShowById(String.valueOf(show.getId()));
    }
}
