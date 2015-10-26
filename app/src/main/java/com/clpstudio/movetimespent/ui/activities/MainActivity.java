package com.clpstudio.movetimespent.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.clpstudio.movetimespent.Adapters.AutocompleteAdapter;
import com.clpstudio.movetimespent.model.TvShow;
import com.clpstudio.movetimespent.network.VolleyRequest;
import com.clpstudio.movetimespent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkUi();
        setupLists();
        setupToolbar();
        setupVolley();
        API_KEY = getString(R.string.api_key);
//        connect("https://api.themoviedb.org/3/search/tv?query=mentalist&api_key=" + API_KEY);
    }

    /**
     * Link the ui
     */
    private void linkUi() {
        mAutoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autocomplete_text_view);
        mAutocompleteAdapter = new AutocompleteAdapter(this);
        mAutoCompleteTextView.setAdapter(mAutocompleteAdapter);
        mAutoCompleteTextView.requestFocus();
    }

    /**
     * Initialize the lists for storage
     */
    private void setupLists() {

    }

    /**
     * Setup volley library for http calls
     */
    private void setupVolley() {
        requestQueue = VolleyRequest.getInstance(this).getRequestQueue();
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
     * @param url String
     */
    private void connect(String url) {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("luci", response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray jsonArray = jsonObject.getJSONArray("results");
//                    for(int i =0; i < jsonArray.length(); i++){
//                        JSONObject json = jsonArray.getJSONObject(i);
//                        int id = json.getInt("id");
//                        String name = json.getString("original_name");
//                        mAutocompleteListOfShows.add(new TvShow(id, name));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("luci", "ERRROR" + error.getMessage());
//            }
//        });
//
//        requestQueue.add(stringRequest);
    }

}
