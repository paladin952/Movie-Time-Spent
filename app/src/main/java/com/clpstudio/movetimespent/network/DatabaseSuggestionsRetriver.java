package com.clpstudio.movetimespent.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.clpstudio.movetimespent.Utils.UrlConstants;
import com.clpstudio.movetimespent.model.TvShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by lclapa on 10/26/2015.
 */
public class DatabaseSuggestionsRetriver {

    /**
     * The context
     */
    private Context mContext;

    /**
     * The constructor
     *
     * @param context The constructor
     */
    public DatabaseSuggestionsRetriver(Context context) {
        mContext = context;
    }

    public List<TvShow> getTvShowsSuggetions(String name) {
        String url = UrlConstants.SEARCH_TV_BASE + name + UrlConstants.API_KEY;
        final List<TvShow> data = new ArrayList<>();

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(url, null, future, future);
        VolleyRequest.getInstance(mContext).addToRequestQueue(request);

        JSONObject response = null;
        try {
            future.get(10, TimeUnit.SECONDS); //block for maximum 10 seconds
            response = future.get();
        } catch (InterruptedException e) {
            //error
        } catch (ExecutionException e) {
            //error
        } catch (TimeoutException e) {
            //error
        }

        if (response != null) {
            try {
                JSONArray jsonArray = response.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    int id = json.getInt("id");
                    String title = json.getString("original_name");
                    data.add(new TvShow(id, title));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
